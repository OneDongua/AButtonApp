package com.onedongua.abutton.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.onedongua.abutton.fragment.AccountFragment;
import com.onedongua.abutton.fragment.MapFragment;
import com.onedongua.abutton.fragment.NotificationFragment;
import com.onedongua.abutton.fragment.PostFragment;

import java.util.HashMap;
import java.util.Map;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
    private final Map<Integer, View.OnClickListener> listeners = new HashMap<>();

    public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                PostFragment postFragment = new PostFragment();
                listeners.put(1, postFragment.getOnRefreshListener());
                return postFragment;
            case 2:
                return new MapFragment();
            case 3:
                return new AccountFragment();
            case 0:
            default:
                NotificationFragment notificationFragment = new NotificationFragment();
                listeners.put(0, notificationFragment.getOnRefreshListener());
                return notificationFragment;
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public View.OnClickListener getOnRefreshListener(int position) {
        if (!listeners.containsKey(position)) return null;
        return listeners.get(position);
    }
}
