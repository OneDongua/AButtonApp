package com.onedongua.abutton.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.onedongua.abutton.adapter.HomePagerAdapter;
import com.onedongua.abutton.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private final NotificationFragment notificationFragment = new NotificationFragment();
    private final WorkFragment workFragment = new WorkFragment();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout notifications = binding.notifications;
        LinearLayout work = binding.work;
        LinearLayout notificationIndicator = binding.notificationIndicator;
        LinearLayout workIndicator = binding.workIndicator;

        ViewPager2 viewPager = binding.viewPager;
        HomePagerAdapter adapter = new HomePagerAdapter(requireActivity(), notificationFragment, workFragment);
        viewPager.setAdapter(adapter);

        notifications.setOnClickListener(v -> {
            notificationIndicator.setVisibility(View.VISIBLE);
            workIndicator.setVisibility(View.GONE);
            viewPager.setCurrentItem(0);
        });
        work.setOnClickListener(v -> {
            notificationIndicator.setVisibility(View.GONE);
            workIndicator.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(1);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View.OnClickListener getOnRefreshListener() {
        return notificationFragment.getOnRefreshListener();
    }
}