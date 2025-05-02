package com.onedongua.abutton.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onedongua.abutton.R;
import com.onedongua.abutton.databinding.ItemNotificationBinding;
import com.onedongua.abutton.model.NotificationItem;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private static final String TAG = "ChatAdapter";
    private final List<Long> itemList;
    private final List<NotificationItem> globalItemList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ChatAdapter(List<Long> itemList, List<NotificationItem> globalItemList, OnItemClickListener listener) {
        this.globalItemList = globalItemList;
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
        Long id = itemList.get(position);
        Map<Long, NotificationItem> map = new HashMap<>();
        for (NotificationItem item : globalItemList) {
            map.put(item.getTime(), item);
        }
        NotificationItem item = map.get(id);
        if (item != null) {
            holder.content.setText(item.getContent());
            holder.from.setText(item.getFrom());
        } else {
            holder.content.setText(R.string.new_chat);
            holder.from.setText(R.string.unknown);
        }
        holder.time.setText(DateFormat.getDateTimeInstance().format(id));

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
