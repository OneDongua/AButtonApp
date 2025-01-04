package com.onedongua.smartcane.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.onedongua.smartcane.ui.dashboard.DashboardFragment;
import com.onedongua.smartcane.ui.home.HomeFragment;
import com.onedongua.smartcane.ui.notifications.NotificationsFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new HomeFragment();
            case 2:
                return new NotificationsFragment();
            case 0:
            default:
                return new DashboardFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
