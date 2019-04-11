package com.github.ruiadrmartins.locationsearcher.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ruiadrmartins.locationsearcher.R;
import com.github.ruiadrmartins.locationsearcher.data.Suggestion;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private Activity activity;
    private List<Suggestion> locationList;

    public LocationAdapter(Activity activity, List<Suggestion> locationList) {
        this.activity = activity;
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public LocationAdapter.LocationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(activity).inflate(R.layout.location_item, viewGroup,false);
        return new LocationHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.LocationHolder locationHolder, int i) {
        locationHolder.locationText.setText(locationList.get(i).getDistance() + " -> " + locationList.get(i).getLabel());
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    class LocationHolder extends RecyclerView.ViewHolder{

        private TextView locationText;

        LocationHolder(@NonNull View itemView) {
            super(itemView);
            locationText = itemView.findViewById(R.id.location_text);
        }
    }
}
