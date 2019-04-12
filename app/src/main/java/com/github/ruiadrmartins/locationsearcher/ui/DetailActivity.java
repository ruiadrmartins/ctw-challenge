package com.github.ruiadrmartins.locationsearcher.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ruiadrmartins.locationsearcher.R;
import com.github.ruiadrmartins.locationsearcher.data.autocomplete.Suggestion;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;

public class DetailActivity extends AppCompatActivity implements DetailViewInterface {

    public static final String LOCATION_DETAILS_KEY = "locationDetails";

    private LinearLayout linearLayout;

    private DetailPresenter presenter;

    private Map map = null;
    private MapFragment mapFragment = null;

    private TextView street;
    private TextView postalCode;
    private TextView coordenates;
    private TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = findViewById(R.id.detail_linear_Layout);

        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_PORTRAIT)  {
            linearLayout.setOrientation(LinearLayout.VERTICAL);
        } else {
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }

        presenter = new DetailPresenter(this, getApplication());

        street = findViewById(R.id.street_text);
        postalCode = findViewById(R.id.postal_code_text);
        coordenates = findViewById(R.id.coordinates_text);
        distance = findViewById(R.id.distance_text);

        Suggestion s = getIntent().getParcelableExtra(LOCATION_DETAILS_KEY);
        presenter.getLocationDetails(s.getLocationId());

        setPostalCode(s.getAddress().getPostalCode());
        setDistance(s.getDistance());
    }

    @Override
    public void updateMap(double latitude, double longitude) {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mapFragment.init(error -> {
            if (error == OnEngineInitListener.Error.NONE) {
                GeoCoordinate coords = new GeoCoordinate(latitude, longitude, 0.0);
                map = mapFragment.getMap();
                map.setCenter(coords, Map.Animation.NONE);
                map.setZoomLevel((map.getMaxZoomLevel() + map.getMinZoomLevel()) / 2);
                map.addMapObject(new MapMarker(coords));
            }
        });
    }

    @Override
    public void setStreet(String streetName) {
        street.setText(streetName);
    }

    @Override
    public void setPostalCode(String pCode) {
        postalCode.setText(pCode);
    }

    @Override
    public void setCoords(String coords) {
        coordenates.setText(coords);
    }

    @Override
    public void setDistance(String dist) {
        distance.setText(dist);
    }

}
