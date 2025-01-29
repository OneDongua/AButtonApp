package com.onedongua.abutton.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onedongua.abutton.databinding.ItemNotificationBinding;
import com.onedongua.abutton.model.NotificationItem;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private final List<NotificationItem> itemList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public NotificationAdapter(List<NotificationItem> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem item = itemList.get(position);
        holder.content.setText(item.getContent());
        holder.time.setText(DateFormat.getDateTimeInstance().format(new Date(item.getTime())));
        holder.from.setText(item.getFrom());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content, time, from;

        public ViewHolder(@NonNull ItemNotificationBinding binding) {
            super(binding.getRoot());
            content = binding.content;
            time = binding.time;
            from = binding.from;
        }
    }
}
