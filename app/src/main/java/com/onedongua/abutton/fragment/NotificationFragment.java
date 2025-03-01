package com.onedongua.abutton.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onedongua.abutton.BaseFragment;
import com.onedongua.abutton.R;
import com.onedongua.abutton.adapter.NotificationAdapter;
import com.onedongua.abutton.databinding.FragmentNotificationsBinding;
import com.onedongua.abutton.manager.ServerManager;
import com.onedongua.abutton.model.NotificationData;
import com.onedongua.abutton.model.NotificationItem;
import com.onedongua.abutton.model.UserInfo;
import com.onedongua.abutton.util.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressLint("NotifyDataSetChanged")
public class NotificationFragment extends BaseFragment {
    private static final String TAG = "NotificationFragment";
    private FragmentNotificationsBinding binding;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private final List<NotificationItem> itemList = new ArrayList<>();
    private ServerManager serverManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        serverManager = ServerManager.getInstance();

        recyclerView = binding.notificationRecycler;
        adapter = new NotificationAdapter(itemList, position -> {
            print("TODO");
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        fetchNotification();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View.OnClickListener getOnRefreshListener() {
        return v -> {
            fetchNotification();
            print(R.string.refresh_success);
        };
    }

    private void refreshNotification() {
        recyclerView.setVisibility(itemList.isEmpty() ? View.GONE : View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void fetchNotification() {
        final String email;
        try {
            email = JsonUtils.fromJsonFile(new File(requireActivity().getFilesDir(), "user.json"), UserInfo.class).getEmail();
        } catch (IOException e) {
            print(R.string.unknown_error);
            Log.e(TAG, "fetchNotification: ", e);
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(serverManager.getServer() + "api/notification/" + email)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
                requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            NotificationData data = JsonUtils.fromJson(response.body().string(), NotificationData.class);
                            itemList.clear();
                            itemList.addAll(data.getNotifications());
                            requireActivity().runOnUiThread(() -> refreshNotification());
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse: ", e);
                        }
                    }
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                }
            }
        });
    }
}