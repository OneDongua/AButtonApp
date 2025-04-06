package com.onedongua.abutton;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private LocationManager locationManager;
    public Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initView();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        requestLocationUpdates();

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

    private void initView() {
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
                        titleId = R.string.title_home;
                        if (menuItem != null) menuItem.setVisible(true);
                        break;
                    case 1:
                        titleId = R.string.title_post;
                        if (menuItem != null) menuItem.setVisible(true);
                        break;
                    case 2:
                        titleId = R.string.title_map;
                        if (menuItem != null) menuItem.setVisible(true);
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
                    tab.setText(R.string.title_home);
                    tab.setIcon(R.drawable.ic_home);
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

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                MainActivity.this.location = location;
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(this, "未授予位置权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void refreshLocation() {
        requestLocationUpdates();
    }

    public static void SHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (info.signatures != null) {
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
            }
        } catch (Exception e) {
            Log.e("SHA1", "SHA1: ", e);
        }
    }

}