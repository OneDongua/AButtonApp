package com.onedongua.abutton.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.onedongua.abutton.fragment.AccountFragment;
import com.onedongua.abutton.fragment.MapFragment;
import com.onedongua.abutton.fragment.NotificationFragment;
import com.onedongua.abutton.fragment.PostFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new PostFragment();
            case 2:
                return new MapFragment();
            case 3:
                return new AccountFragment();
            case 0:
            default:
                return new NotificationFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
