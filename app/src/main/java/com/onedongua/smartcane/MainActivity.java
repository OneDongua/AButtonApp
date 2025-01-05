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

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewPager.setUserInputEnabled(position != 2);
            }
        });


        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(R.string.title_dashboard);
                    tab.setIcon(R.drawable.ic_dashboard_black_24dp);
                    break;
                case 1:
                    tab.setText(R.string.title_pair);
                    tab.setIcon(R.drawable.ic_link_black_24dp);
                    break;
                case 2:
                    tab.setText(R.string.title_map);
                    tab.setIcon(R.drawable.ic_map_black_24dp);
                    break;
            }
        }).attach();
    }

}