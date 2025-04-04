package com.onedongua.abutton.fragment;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onedongua.abutton.BaseFragment;
import com.onedongua.abutton.MainActivity;
import com.onedongua.abutton.R;
import com.onedongua.abutton.adapter.GlobalNotificationAdapter;
import com.onedongua.abutton.adapter.NotificationAdapter;
import com.onedongua.abutton.databinding.FragmentNotificationsBinding;
import com.onedongua.abutton.manager.ServerManager;
import com.onedongua.abutton.model.GlobalNotificationItem;
import com.onedongua.abutton.model.LocationData;
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

@SuppressLint("NotifyDataSetChanged")
public class NotificationFragment extends BaseFragment {
    private static final String TAG = "NotificationFragment";
    private FragmentNotificationsBinding binding;
    private RecyclerView notificationRecycler;
    private RecyclerView globalRecycler;
    private NotificationAdapter adapter;
    private GlobalNotificationAdapter globalAdapter;
    private final List<NotificationItem> itemList = new ArrayList<>();
    private final List<GlobalNotificationItem> globalItemList = new ArrayList<>();
    private final ServerManager serverManager = ServerManager.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        notificationRecycler = binding.notificationRecycler;
        adapter = new NotificationAdapter(itemList, position -> {
            print("TODO");
        });
        notificationRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        notificationRecycler.setAdapter(adapter);

        globalRecycler = binding.globalNotificationRecycler;
        globalAdapter = new GlobalNotificationAdapter(requireContext(), globalItemList, position -> {
            print("TODO");
        });
        globalRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        globalRecycler.setAdapter(globalAdapter);

        fetchNotification();
        fetchGlobalNotification();

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
            fetchGlobalNotification();
        };
    }

    private Location getLocation() {
        if (requireActivity() instanceof MainActivity) {
            return ((MainActivity) requireActivity()).location;
        }
        return null;
    }

    private void refreshNotification() {
        notificationRecycler.setVisibility(itemList.isEmpty() ? View.GONE : View.VISIBLE);
        adapter.notifyDataSetChanged();
        globalRecycler.setVisibility(globalItemList.isEmpty() ? View.GONE : View.VISIBLE);
        globalAdapter.notifyDataSetChanged();
    }

    private void fetchNotification() {
        final String email;
        try {
            UserInfo userInfo = JsonUtils.fromJsonFile(new File(requireActivity().getFilesDir(), "user.json"), UserInfo.class);
            if (userInfo == null) {
                print(R.string.not_login);
                return;
            }
            email = userInfo.getEmail();
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
                if (!response.isSuccessful() || response.body() == null) {
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                    return;
                }
                try {
                    NotificationData data = JsonUtils.fromJson(response.body().string(), NotificationData.class);
                    itemList.clear();
                    itemList.addAll(data.getNotifications());
                    Collections.reverse(itemList);
                    requireActivity().runOnUiThread(() -> {
                        refreshNotification();
                    });
                } catch (IOException e) {
                    Log.e(TAG, "onResponse: ", e);
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                }
            }
        });

    }

    private void fetchGlobalNotification() {
        binding.globalNotificationEmpty.setText(R.string.loading);
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
                    Location myLocation = getLocation();
                    while (myLocation == null) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        myLocation = getLocation();
                    }
                    for (NotificationItem item : data.getNotifications()) {
                        LocationData locationData = item.getLocation();
                        Location location = new Location("");
                        location.setAltitude(locationData.getAltitude());
                        location.setLatitude(locationData.getLatitude());
                        location.setLongitude(locationData.getLongitude());
                        float distance = location.distanceTo(getLocation());
                        if (distance < 1000) {
                            GlobalNotificationItem tempItem = new GlobalNotificationItem();
                            tempItem.setContent(item.getContent());
                            tempItem.setTime(item.getTime());
                            tempItem.setFrom(item.getFrom());
                            tempItem.setLocation(locationData);
                            tempItem.setDistance(distance);
                            globalItemList.add(tempItem);
                        }
                    }
                    Collections.sort(globalItemList, (o1, o2) ->
                            (int) (o1.getDistance() - o2.getDistance()));
                    requireActivity().runOnUiThread(() -> {
                        refreshNotification();
                        binding.globalNotificationEmpty.setText(R.string.notification_empty);
                    });
                } catch (IOException e) {
                    Log.e(TAG, "onResponse: ", e);
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                }
            }
        });
    }
}