package com.onedongua.abutton.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.onedongua.abutton.BaseFragment;
import com.onedongua.abutton.LoginActivity;
import com.onedongua.abutton.R;
import com.onedongua.abutton.SettingActivity;
import com.onedongua.abutton.databinding.FragmentAccountBinding;
import com.onedongua.abutton.model.UserInfo;
import com.onedongua.abutton.util.JsonUtils;
import com.onedongua.abutton.widget.ToggleSwitchView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends BaseFragment {
    private static final String TAG = "AccountFragment";
    private FragmentAccountBinding binding;
    private LinearLayout accountBackground;
    private LinearLayout settingsItem;
    private CircleImageView accountAvatar;
    private TextView accountName;
    private TextView accountHardware;
    private TextView accountId;
    private LinearLayout tag;
    private File userFile;
    private ToggleSwitchView toggleSwitch;
    private int mode = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userFile = new File(requireActivity().getFilesDir(), "user.json");

        accountBackground = binding.accountBackground;
        accountAvatar = binding.accountAvatar;
        accountName = binding.accountName;
        accountHardware = binding.accountHardware;
        accountId = binding.accountId;
        settingsItem = binding.settingsItem;
        toggleSwitch = binding.toggleSwitch;
        tag = binding.tag;

        accountBackground.setOnClickListener(this::onAccountBackgroundClick);
        settingsItem.setOnClickListener(this::onSettingsItemClick);

        toggleSwitch.setOnToggleSwitchListener(position -> {
            mode = position;
            refreshTag();
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAccount();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void onAccountBackgroundClick(View v) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void onAccountBackgroundClickLogined(View v) {
        new AlertDialog.Builder(requireActivity())
                .setMessage(R.string.confirm_logout)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    userFile.delete();
                    refreshAccount();
                })
                .show();
    }

    private void refreshAccount() {
        try {
            if (!userFile.exists()) {
                accountBackground.setOnClickListener(this::onAccountBackgroundClick);
                accountName.setText(R.string.account_default_name);
                accountId.setText(R.string.account_default_id);
                accountHardware.setText(R.string.account_default_hardware);
                return;
            }

            accountBackground.setOnClickListener(this::onAccountBackgroundClickLogined);

            UserInfo user = JsonUtils.fromJsonFile(userFile, UserInfo.class);
            accountName.setText(user.getName());
            accountId.setText(user.getId());
            if (!user.getHardware().isEmpty())
                accountHardware.setText(user.getHardware());

            refreshTag();
        } catch (IOException e) {
            Log.e(TAG, "refreshAccount: ", e);
        }
    }

    private void refreshTag() {
        try {
            UserInfo user = JsonUtils.fromJsonFile(userFile, UserInfo.class);
            ArrayList<String> list = new ArrayList<>();
            switch (mode) {
                case 0:
                    list = user.getTag();
                    break;
                case 1:
                case 2:
                    list = user.getDisability();
                    break;
            }
            if (list != null) {
                tag.removeAllViews();
                for (String t : list) {
                    LinearLayout tagLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_tag, null);
                    ((TextView) tagLayout.findViewById(R.id.tag_text)).setText(t);
                    tag.addView(tagLayout);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "refreshTag: ", e);
        }
    }

    private void onSettingsItemClick(View v) {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivity(intent);
    }
}