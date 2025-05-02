package com.onedongua.abutton.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onedongua.abutton.BaseFragment;
import com.onedongua.abutton.ChatActivity;
import com.onedongua.abutton.R;
import com.onedongua.abutton.adapter.ChatAdapter;
import com.onedongua.abutton.databinding.FragmentWorkBinding;
import com.onedongua.abutton.manager.ServerManager;
import com.onedongua.abutton.model.NotificationData;
import com.onedongua.abutton.model.NotificationItem;
import com.onedongua.abutton.model.UserInfo;
import com.onedongua.abutton.util.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WorkFragment extends BaseFragment {
    private static final String TAG = "WorkFragment";
    private FragmentWorkBinding binding;
    private RecyclerView workRecycler;
    private final ServerManager serverManager = ServerManager.getInstance();
    private final ArrayList<Long> itemList = new ArrayList<>();
    private final ArrayList<NotificationItem> globalItemList = new ArrayList<>();
    private ChatAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWorkBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        workRecycler = binding.workRecycler;

        adapter = new ChatAdapter(itemList, globalItemList, position -> {
            Intent intent = new Intent(requireContext(), ChatActivity.class);
            intent.putExtra("id", itemList.get(position));
            startActivity(intent);
        });
        workRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        workRecycler.setAdapter(adapter);

        fetchChats();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View.OnClickListener getOnRefreshListener() {
        return v -> {
            fetchChats();
        };
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshChats() {
        workRecycler.setVisibility(itemList.isEmpty() ? View.GONE : View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void fetchChats() {
        final String id;
        try {
            if (!isAdded() || getActivity() == null) {
                return;
            }
            UserInfo userInfo = JsonUtils.fromJsonFile(new File(requireActivity().getFilesDir(), "user.json"), UserInfo.class);
            if (userInfo == null) {
                print(R.string.not_login);
                return;
            }
            id = userInfo.getId();
        } catch (IOException e) {
            print(R.string.unknown_error);
            Log.e(TAG, "fetchNotification: ", e);
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(serverManager.getServer() + "api/chat/list/" + id)
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
                if (!response.isSuccessful() || response.body() == null) {
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                    return;
                }
                try {
                    List<Long> data = JsonUtils.fromJson(response.body().string(), new TypeReference<>() {
                    });
                    itemList.clear();
                    itemList.addAll(data);
                    Collections.reverse(itemList);
                    fetchGlobalNotification();
                } catch (IOException e) {
                    Log.e(TAG, "onResponse: ", e);
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                }
            }
        });

    }

    private void fetchGlobalNotification() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(serverManager.getServer() + "api/global/notification/")
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
                if (!response.isSuccessful() || response.body() == null) {
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                    return;
                }
                try {
                    NotificationData data = JsonUtils.fromJson(response.body().string(), NotificationData.class);
                    globalItemList.clear();
                    globalItemList.addAll(data.getNotifications());
                    requireActivity().runOnUiThread(() -> {
                        refreshChats();
                    });
                } catch (IOException e) {
                    Log.e(TAG, "onResponse: ", e);
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                }
            }
        });
    }
}