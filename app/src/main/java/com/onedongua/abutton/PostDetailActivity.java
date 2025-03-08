package com.onedongua.abutton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.onedongua.abutton.adapter.ImagePagerAdapter;
import com.onedongua.abutton.databinding.ActivityPostDetailBinding;
import com.onedongua.abutton.manager.ServerManager;
import com.onedongua.abutton.model.PostDetailData;
import com.onedongua.abutton.util.JsonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@SuppressLint("NotifyDataSetChanged")
public class PostDetailActivity extends BaseActivity {
    private static final String TAG = "PostDetailActivity";
    private ActivityPostDetailBinding binding;
    private final ServerManager serverManager = ServerManager.getInstance();
    private String postId;
    ImagePagerAdapter adapter;
    private final List<String> imageUrls = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (actionBar != null) {
            actionBar.setTitle(R.string.post_detail);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewPager2 viewPager = binding.viewPager;

        Intent intent = getIntent();
        if (intent != null) {
            postId = intent.getStringExtra("postId");
        }

        fetchPostDetail();

        adapter = new ImagePagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.indicator.setSelection(position);
                super.onPageSelected(position);
            }
        });
    }

    private void fetchPostDetail() {
        OkHttpClient client = new OkHttpClient();
        client.newCall(new Request.Builder()
                .url(serverManager.getServer() + "api/post/" + postId + "/detail")
                .build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
                runOnUiThread(() -> print(R.string.unknown_error));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        try {
                            PostDetailData data = JsonUtils.fromJson(response.body().string(), PostDetailData.class);
                            for (int i = 0; i < data.getImageCount(); i++) {
                                imageUrls.add(serverManager.getServer() + "api/post/" + postId + "/image/" + i);
                            }
                            runOnUiThread(() -> {
                                binding.title.setText(data.getTitle());
                                binding.content.setText(data.getContent());
                                binding.indicator.setCount(data.getImageCount());
                                adapter.notifyDataSetChanged();
                            });
                        } catch (IOException e) {
                            Log.e(TAG, "onResponse: ", e);
                            runOnUiThread(() -> print(R.string.unknown_error));
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
