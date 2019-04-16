package com.github.ruiadrmartins.locationsearcher.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ruiadrmartins.locationsearcher.R;
import com.github.ruiadrmartins.locationsearcher.data.autocomplete.Suggestion;
import com.github.ruiadrmartins.locationsearcher.ui.DetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationHolder> {

    private Activity activity;

    public List<Suggestion> getLocationList() {
        return locationList;
    }

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
    public void onBindViewHolder(@NonNull LocationHolder locationHolder, int i) {
        locationHolder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(activity, DetailActivity.class);
            intent.putExtra(DetailActivity.LOCATION_DETAILS_KEY, locationList.get(i));
            activity.startActivity(intent);
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locationHolder.labelText.setText(Html.fromHtml(locationList.get(i).getLabel(), Html.FROM_HTML_MODE_LEGACY));
        } else {
            locationHolder.labelText.setText(Html.fromHtml(locationList.get(i).getLabel()));
        }

        Double distance = Double.valueOf(locationList.get(i).getDistance());
        if(distance > 1000) {
            locationHolder.distanceText.setText(String.format(activity.getString(R.string.distance_unit_km), distance/1000));
        } else {
            locationHolder.distanceText.setText(String.format(activity.getString(R.string.distance_unit_m), distance.intValue()));
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    class LocationHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.card_view) CardView cardView;
        @BindView(R.id.label_text) TextView labelText;
        @BindView(R.id.distance_text) TextView distanceText;

        LocationHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
