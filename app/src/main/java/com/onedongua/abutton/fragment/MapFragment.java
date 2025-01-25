package com.onedongua.abutton.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.MyLocationStyle;
import com.onedongua.abutton.R;
import com.onedongua.abutton.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {

    private FragmentMapBinding binding;
    private MapView mapView;
    private Button btnLocating;
    private AMap aMap;
    private boolean isLocating = false;
    private MyLocationStyle myLocationStyle;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
            });

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        initializeAMapPreSettings();

        mapView = binding.map;
        mapView.onCreate(savedInstanceState);

        btnLocating = binding.btnLocating;
        updateLocatingText();
        btnLocating.setOnClickListener(v -> {
            isLocating = !isLocating;
            updateLocatingText();
            setMyLocationEnabled(isLocating);
        });

        checkLocationPermission();

        initMap();

        return root;
    }

    @SuppressLint("SetTextI18n")
    private void updateLocatingText() {
        btnLocating.setText(getString(isLocating ? R.string.disable : R.string.enable) + getString(R.string.locate));
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void initMap() {
        aMap = mapView.getMap();
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(2000);
        myLocationStyle.showMyLocation(true); // 设置是否显示定位小蓝点
        aMap.setMyLocationStyle(myLocationStyle);

        setMyLocationEnabled(isLocating);
    }

    private void setMyLocationEnabled(boolean bool) {
        if (bool) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
            aMap.setMyLocationStyle(myLocationStyle);
            new Handler().postDelayed(() -> {
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
                aMap.setMyLocationStyle(myLocationStyle);
            }, 10000);
        }
        aMap.setMyLocationEnabled(bool);
        aMap.getUiSettings().setMyLocationButtonEnabled(bool);
        isLocating = bool;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void initializeAMapPreSettings() {
        MapsInitializer.updatePrivacyShow(requireContext(), true, true);
        MapsInitializer.updatePrivacyAgree(requireContext(), true);
        MapsInitializer.setApiKey("7bdd910be2fc977e20a5fbf1037831a3");
    }
}
