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
        locationHolder.distanceText.setText(String.format(activity.getString(R.string.distance_unit),locationList.get(i).getDistance()));
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    class LocationHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private TextView labelText;
        private TextView distanceText;

        LocationHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            labelText = itemView.findViewById(R.id.label_text);
            distanceText = itemView.findViewById(R.id.distance_text);
        }
    }
}
