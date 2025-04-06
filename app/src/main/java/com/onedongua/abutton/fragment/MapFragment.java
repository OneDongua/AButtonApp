package com.onedongua.abutton.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.fasterxml.jackson.core.type.TypeReference;
import com.onedongua.abutton.BaseFragment;
import com.onedongua.abutton.R;
import com.onedongua.abutton.databinding.FragmentMapBinding;
import com.onedongua.abutton.manager.ServerManager;
import com.onedongua.abutton.model.NotificationData;
import com.onedongua.abutton.model.NotificationItem;
import com.onedongua.abutton.model.PostData;
import com.onedongua.abutton.util.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapFragment extends BaseFragment {
    private static final String TAG = "MapFragment";
    private FragmentMapBinding binding;
    private MapView mapView;
    private Button btnLocating;
    private AMap aMap;
    private boolean isLocating = false;
    private MyLocationStyle myLocationStyle;
    private final ServerManager serverManager = ServerManager.getInstance();
    private final List<MultiPointItem> multiPointList = new ArrayList<>();
    private final List<MultiPointItem> multiPointList2 = new ArrayList<>();
    private MultiPointOverlay multiPointOverlay;
    private MultiPointOverlay multiPointOverlay2;

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

        fetchGlobalNotification();
        fetchPosts();

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

        MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();
        overlayOptions.icon(BitmapDescriptorFactory.defaultMarker()); // 设置图标
        overlayOptions.anchor(0.5f, 0.5f); // 设置锚点
        multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);

        MultiPointOverlayOptions overlayOptions2 = new MultiPointOverlayOptions();
        overlayOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.point)); // 设置图标
        overlayOptions.anchor(0.5f, 0.5f); // 设置锚点
        multiPointOverlay2 = aMap.addMultiPointOverlay(overlayOptions);
    }

    private void setMyLocationEnabled(boolean bool) {
        /*if (bool) {
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
            aMap.setMyLocationStyle(myLocationStyle);
            new Handler().postDelayed(() -> {
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
                aMap.setMyLocationStyle(myLocationStyle);
            }, 10000);
        }*/
        aMap.setMyLocationEnabled(bool);
        aMap.getUiSettings().setMyLocationButtonEnabled(bool);
        isLocating = bool;

    }

    public View.OnClickListener getOnRefreshListener() {
        return v -> {
            fetchGlobalNotification();
        };
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
    }

    private void fetchGlobalNotification() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(serverManager.getServer() + "api/global/notification/")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
                requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (!response.isSuccessful() || response.body() == null) {
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                    return;
                }
                try {
                    NotificationData data = JsonUtils.fromJson(response.body().string(), NotificationData.class);
                    multiPointList.clear();
                    for (NotificationItem item : data.getNotifications()) {
                        CoordinateConverter converter = new CoordinateConverter(requireContext());
                        converter.from(CoordinateConverter.CoordType.GPS);
                        converter.coord(new LatLng(item.getLocation().getLatitude(), item.getLocation().getLongitude()));
                        LatLng latLng = converter.convert();
                        MultiPointItem multiPointItem = new MultiPointItem(latLng);
                        multiPointList.add(multiPointItem);
                    }
                    multiPointOverlay.setItems(multiPointList);
                } catch (IOException e) {
                    Log.e(TAG, "onResponse: ", e);
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                }
            }
        });
    }

    private void fetchPosts() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(serverManager.getServer() + "api/post")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
                requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            Map<String, PostData> data = JsonUtils.fromJson(response.body().string(),
                                    new TypeReference<HashMap<String, PostData>>() {
                                    });
                            multiPointList2.clear();
                            for (PostData item : data.values()) {
                                CoordinateConverter converter = new CoordinateConverter(requireContext());
                                converter.from(CoordinateConverter.CoordType.GPS);
                                converter.coord(new LatLng(item.getLocation().getLatitude(), item.getLocation().getLongitude()));
                                LatLng latLng = converter.convert();
                                MultiPointItem multiPointItem = new MultiPointItem(latLng);
                                multiPointList2.add(multiPointItem);
                            }
                            multiPointOverlay2.setItems(multiPointList2);
                            return;
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse: ", e);
                        }
                    }
                    requireActivity().runOnUiThread(() -> print(R.string.unknown_error));
                }
            }
        });
    }

}
