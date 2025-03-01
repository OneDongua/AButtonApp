package com.onedongua.abutton.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bumptech.glide.Glide;
import com.onedongua.abutton.R;
import com.onedongua.abutton.databinding.ItemPostBinding;
import com.onedongua.abutton.manager.ServerManager;
import com.onedongua.abutton.model.PostItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private static final String TAG = "PostAdapter";
    private final Context context;
    private final List<PostItem> itemList;
    private final OnItemClickListener listener;
    private final Handler handler = new Handler();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public PostAdapter(Context context, List<PostItem> itemList, OnItemClickListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostItem item = itemList.get(position);

        ServerManager serverManager = ServerManager.getInstance();
        Glide.with(context)
                .load(serverManager.getServer() + "api/post/" + item.getId() + "/image/0")
                .placeholder(R.drawable.ic_image)
                .into(holder.image);

        holder.title.setText(item.getTitle());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        holder.time.setText(dateFormat.format(new Date(item.getTime())));

        LatLonPoint latLonPoint = new LatLonPoint(item.getLocation().getLongitude(), item.getLocation().getLatitude());
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100, GeocodeSearch.AMAP);
        try {
            GeocodeSearch search = new GeocodeSearch(context);
            search.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
                    if (rCode == 1000) {
                        String city = regeocodeResult.getRegeocodeAddress().getCity();
                        String district = regeocodeResult.getRegeocodeAddress().getDistrict();
                        handler.post(() -> holder.location.setText(city + " " + district));
                    }
                }

                @Override
                public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
                }
            });
            search.getFromLocationAsyn(query);
        } catch (AMapException e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }

        holder.commentCount.setText(String.valueOf(item.getCommentCount()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, time, location, commentCount;

        public ViewHolder(@NonNull ItemPostBinding binding) {
            super(binding.getRoot());
            image = binding.image;
            title = binding.title;
            time = binding.time;
            commentCount = binding.commentCount;
            location = binding.location;
        }
    }
}
