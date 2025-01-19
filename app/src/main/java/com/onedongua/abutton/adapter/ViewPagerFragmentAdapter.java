package com.onedongua.abutton.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.onedongua.abutton.ui.dashboard.DashboardFragment;
import com.onedongua.abutton.ui.map.MapFragment;
import com.onedongua.abutton.ui.pair.PairFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new PairFragment();
            case 2:
                return new MapFragment();
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
