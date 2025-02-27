package com.onedongua.abutton.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onedongua.abutton.databinding.ItemPostBinding;
import com.onedongua.abutton.model.PostItem;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private final List<PostItem> itemList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public PostAdapter(List<PostItem> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostItem item = itemList.get(position);
        // TODO
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, time, location, commentCount;

        public ViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            title = binding.title;
            time = binding.time;
            commentCount = binding.commentCount;
            location = binding.location;
        }
    }
}
