package com.onedongua.abutton.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.onedongua.abutton.databinding.ItemGlobalNotificationBinding;
import com.onedongua.abutton.model.GlobalNotificationItem;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GlobalNotificationAdapter extends RecyclerView.Adapter<GlobalNotificationAdapter.ViewHolder> {
    private static final String TAG = "GlobalNotificationAdapter";
    private final List<GlobalNotificationItem> globalItemList;
    private final OnItemClickListener listener;
    private final Handler handler = new Handler();
    private final Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public GlobalNotificationAdapter(Context context, List<GlobalNotificationItem> itemList,
                                     OnItemClickListener listener) {
        this.context = context;
        this.globalItemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGlobalNotificationBinding binding = ItemGlobalNotificationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GlobalNotificationItem item = globalItemList.get(position);
        holder.content.setText(item.getContent());
        holder.time.setText(DateFormat.getDateTimeInstance().format(new Date(item.getTime())));
        holder.from.setText(item.getFrom());
        holder.distance.setText(String.format(Locale.CHINA, "距您%.1f米", item.getDistance()));
        LatLonPoint latLonPoint = new LatLonPoint(item.getLocation().getLatitude(), item.getLocation().getLongitude());
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100, GeocodeSearch.GPS);

        try {
            GeocodeSearch search = new GeocodeSearch(context);
            search.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
                    if (rCode == 1000) {
                        String district = regeocodeResult.getRegeocodeAddress().getDistrict();
                        String township = regeocodeResult.getRegeocodeAddress().getTownship();
                        String neighborhood = regeocodeResult.getRegeocodeAddress().getNeighborhood();
                        String building = regeocodeResult.getRegeocodeAddress().getBuilding();
                        handler.post(() -> holder.location.setText(district + township + neighborhood + building));
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

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return globalItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content, time, from, distance, location;

        public ViewHolder(@NonNull ItemGlobalNotificationBinding binding) {
            super(binding.getRoot());
            content = binding.content;
            time = binding.time;
            from = binding.from;
            distance = binding.distance;
            location = binding.location;
        }
    }
}
