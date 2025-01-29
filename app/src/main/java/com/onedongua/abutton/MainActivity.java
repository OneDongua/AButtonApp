package com.onedongua.abutton;

import android.os.Bundle;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.onedongua.abutton.adapter.ViewPagerFragmentAdapter;
import com.onedongua.abutton.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

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

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int titleId;
                switch (position) {
                    case 0:
                        titleId = R.string.title_notifications;
                        break;
                    case 1:
                        titleId = R.string.title_post;
                        break;
                    case 2:
                        titleId = R.string.title_map;
                        break;
                    case 3:
                        titleId = R.string.title_account;
                        break;
                    default:
                        titleId = R.string.app_name;
                }
                actionBar.setTitle(titleId);

                viewPager.setUserInputEnabled(position != 2);
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.title_notifications);
                    tab.setIcon(R.drawable.ic_notifications);
                    break;
                case 1:
                    tab.setText(R.string.title_post);
                    tab.setIcon(R.drawable.ic_post);
                    break;
                case 2:
                    tab.setText(R.string.title_map);
                    tab.setIcon(R.drawable.ic_map);
                    break;
                case 3:
                    tab.setText(R.string.title_account);
                    tab.setIcon(R.drawable.ic_account);
                    break;
            }
        }).attach();
    }
}