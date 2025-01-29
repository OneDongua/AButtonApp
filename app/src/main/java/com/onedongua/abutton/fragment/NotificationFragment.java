package com.onedongua.abutton.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.onedongua.abutton.BaseFragment;
import com.onedongua.abutton.adapter.NotificationAdapter;
import com.onedongua.abutton.data.NotificationItem;
import com.onedongua.abutton.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends BaseFragment {

    private FragmentNotificationsBinding binding;
    private final List<NotificationItem> itemList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView recyclerView = binding.notificationRecycler;
        final NotificationAdapter adapter = new NotificationAdapter(itemList, position -> {
            print("TODO");
        });

        itemList.add(new NotificationItem("Hello World", System.currentTimeMillis(), "OneDongua"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchNotification() {

    }
}