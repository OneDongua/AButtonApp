package com.onedongua.abutton.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onedongua.abutton.BaseFragment;
import com.onedongua.abutton.PostDetailActivity;
import com.onedongua.abutton.R;
import com.onedongua.abutton.adapter.PostAdapter;
import com.onedongua.abutton.databinding.FragmentPostBinding;
import com.onedongua.abutton.manager.ServerManager;
import com.onedongua.abutton.model.PostData;
import com.onedongua.abutton.model.PostItem;
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

public class PostFragment extends BaseFragment {
    private static final String TAG = "PostFragment";
    private FragmentPostBinding binding;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private final ArrayList<PostItem> itemList = new ArrayList<>();
    private final ServerManager serverManager = ServerManager.getInstance();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.postRecycler;
        adapter = new PostAdapter(requireContext(), itemList, position -> {
            Intent intent = new Intent(requireContext(), PostDetailActivity.class);
            intent.putExtra("postId", itemList.get(position).getId());
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setAdapter(adapter);

        fetchPosts();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View.OnClickListener getOnRefreshListener() {
        return v -> {
            fetchPosts();
            print(R.string.refresh_success);
        };
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshPosts() {
        recyclerView.setVisibility(itemList.isEmpty() ? View.GONE : View.VISIBLE);
        adapter.notifyDataSetChanged();
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
                            itemList.clear();
                            itemList.addAll(toPostItems(data));
                            requireActivity().runOnUiThread(() -> refreshPosts());
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

    private List<PostItem> toPostItems(Map<String, PostData> data) {
        final ArrayList<PostItem> postItems = new ArrayList<>();
        for (Map.Entry<String, PostData> entry : data.entrySet()) {
            PostData post = entry.getValue();
            postItems.add(new PostItem(
                    entry.getKey(),
                    post.getTitle(),
                    post.getModTime(),
                    post.getLocation(),
                    post.getCount()));
        }
        return postItems;
    }
}