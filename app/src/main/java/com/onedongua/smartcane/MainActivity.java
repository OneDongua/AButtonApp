package com.onedongua.smartcane;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.onedongua.smartcane.adapter.ViewPagerFragmentAdapter;
import com.onedongua.smartcane.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.tabLayout;

        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.title_dashboard);
                    tab.setIcon(R.drawable.ic_dashboard_black_24dp);
                    break;
                case 1:
                    tab.setText(R.string.title_home);
                    tab.setIcon(R.drawable.ic_home_black_24dp);
                    break;
                case 2:
                    tab.setText(R.string.title_notifications);
                    tab.setIcon(R.drawable.ic_notifications_black_24dp);
                    break;
            }
        }).attach();
    }

}