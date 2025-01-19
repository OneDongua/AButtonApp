package com.onedongua.abutton.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.MyLocationStyle;
import com.onedongua.abutton.R;
import com.onedongua.abutton.databinding.FragmentMapBinding;

public class MapFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FragmentMapBinding binding;
    private MapView mapView;
    private Button btnLocating;
    private AMap aMap;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapViewModel viewModel =
                new ViewModelProvider(this).get(MapViewModel.class);

        binding = FragmentMapBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MapsInitializer.updatePrivacyShow(requireContext(), true, true);
        MapsInitializer.updatePrivacyAgree(requireContext(), true);

        mapView = binding.map;
        mapView.onCreate(savedInstanceState);

        btnLocating = binding.btnLocating;
        btnLocating.setOnClickListener(v -> viewModel.switchLocating());
        viewModel.getLocating().observe(getViewLifecycleOwner(), bool -> {
            btnLocating.setText(getString(bool ? R.string.disable : R.string.enable)
                    + getString(R.string.locate));
            setMyLocationEnabled(bool);
        });

        checkLocationPermission();

        initMap();

        return root;
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap() {
        aMap = mapView.getMap();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.interval(2000);
        myLocationStyle.showMyLocation(true); // 设置是否显示定位小蓝点
        aMap.setMyLocationStyle(myLocationStyle);

        //setMyLocationEnabled(true);
    }

    private void setMyLocationEnabled(boolean bool) {
        aMap.setMyLocationEnabled(bool);
        aMap.getUiSettings().setMyLocationButtonEnabled(bool);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nothing
            } else {
                Toast.makeText(requireContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
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

}
