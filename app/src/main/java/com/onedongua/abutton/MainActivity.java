package com.onedongua.abutton;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.onedongua.abutton.adapter.ViewPagerFragmentAdapter;
import com.onedongua.abutton.databinding.ActivityMainBinding;

import java.security.MessageDigest;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private ViewPagerFragmentAdapter adapter;
    private int position;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPager2 viewPager = binding.viewPager;
        TabLayout tabLayout = binding.tabLayout;

        adapter = new ViewPagerFragmentAdapter(this);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                MainActivity.this.position = position;
                int titleId;
                switch (position) {
                    case 0:
                        titleId = R.string.title_notifications;
                        if (menuItem != null) menuItem.setVisible(true);
                        break;
                    case 1:
                        titleId = R.string.title_post;
                        if (menuItem != null) menuItem.setVisible(true);
                        break;
                    case 2:
                        titleId = R.string.title_map;
                        if (menuItem != null) menuItem.setVisible(false);
                        break;
                    case 3:
                        titleId = R.string.title_account;
                        if (menuItem != null) menuItem.setVisible(false);
                        break;
                    default:
                        titleId = R.string.app_name;
                        if (menuItem != null) menuItem.setVisible(false);
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

        //SHA1(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuItem = menu.findItem(R.id.action_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            View.OnClickListener listener = adapter.getOnRefreshListener(position);
            if (listener != null) listener.onClick(item.getActionView());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void SHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuilder hexString = new StringBuilder();
            for (byte b : publicKey) {
                String appendString = Integer.toHexString(0xFF & b).toUpperCase();
                if (appendString.length() == 1) hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            Log.i("SHA1", "SHA1: " + result.substring(0, result.length() - 1));
        } catch (Exception e) {
            Log.e("SHA1", "SHA1: ", e);
        }
    }

}