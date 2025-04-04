package com.onedongua.abutton.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.onedongua.abutton.fragment.NotificationFragment;
import com.onedongua.abutton.fragment.WorkFragment;

public class HomePagerAdapter extends FragmentStateAdapter {
    private final NotificationFragment notificationFragment;
    private final WorkFragment workFragment;

    public View.OnClickListener refreshListener;

    public HomePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        notificationFragment = new NotificationFragment();
        workFragment = new WorkFragment();
        refreshListener = notificationFragment.getOnRefreshListener();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return workFragment;
            case 0:
            default:
                return notificationFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Two pages: Notification and Work
    }
}