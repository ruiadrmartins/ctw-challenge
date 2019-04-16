package com.github.ruiadrmartins.locationsearcher.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ruiadrmartins.locationsearcher.R;
import com.github.ruiadrmartins.locationsearcher.data.autocomplete.Suggestion;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements DetailViewInterface {

    public static final String LOCATION_DETAILS_KEY = "locationDetails";
    public static final String LOCATION_DETAILS_LATITUDE_KEY = "locationLatitude";
    public static final String LOCATION_DETAILS_LONGITUDE_KEY = "locationLongitude";
    public static final String LOCATION_DETAILS_STREET_KEY = "streetName";

    @BindView(R.id.detail_linear_Layout) LinearLayout linearLayout;
    @BindView(R.id.progress_linear_layout) ProgressLinearLayout progressLinearLayout;
    @BindView(R.id.street_text) TextView street;
    @BindView(R.id.postal_code_text) TextView postalCode;
    @BindView(R.id.coordinates_text) TextView coordinates;
    @BindView(R.id.distance_text) TextView distance;
    @BindView(R.id.toolbar) Toolbar toolbar;

    private Map map = null;
    private MapFragment mapFragment = null;

    private Suggestion suggestion;
    private double latitude;
    private double longitude;
    private String streetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        linearLayout = findViewById(R.id.detail_linear_Layout);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)  {
            linearLayout.setOrientation(LinearLayout.VERTICAL);
        } else {
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        }

        DetailPresenter presenter = new DetailPresenter(this, getApplication());

        if(savedInstanceState != null) {
            suggestion = savedInstanceState.getParcelable(LOCATION_DETAILS_KEY);
            latitude = savedInstanceState.getDouble(LOCATION_DETAILS_LATITUDE_KEY);
            longitude = savedInstanceState.getDouble(LOCATION_DETAILS_LONGITUDE_KEY);
            streetName = savedInstanceState.getString(LOCATION_DETAILS_STREET_KEY);
            setStreet(streetName);
            setCoords(latitude + "," + longitude);
            updateMap(latitude, longitude);
        } else if(getIntent() != null){
            suggestion = getIntent().getParcelableExtra(LOCATION_DETAILS_KEY);
            if(suggestion != null) {
                presenter.getLocationDetails(suggestion.getLocationId());
            } else {
                showError(getString(R.string.generic_error_message));
            }
        }

        if(suggestion != null) {
            setStreet(suggestion.getAddress().getStreet());
            setPostalCode(suggestion.getAddress().getPostalCode());
            setDistance(suggestion.getDistance());
        } else {
            showError(getString(R.string.generic_error_message));
        }
    }

    @Override
    public void showError(String error) {
        progressLinearLayout.showError(
                R.drawable.ic_error_black_24dp,
                getString(R.string.error_list_title), error,
                getString(R.string.generic_error_button_message),
                view -> {
                    // TODO: THIS
                    Toast.makeText(this, "Lol.", Toast.LENGTH_SHORT).show();
                }
        );
    }

    @Override
    public void updateMap(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
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
        this.streetName = streetName;
        street.setText(streetName);
    }

    @Override
    public void setPostalCode(String pCode) {
        postalCode.setText(pCode);
    }

    @Override
    public void setCoords(String coords) {
        coordinates.setText(coords);
    }

    @Override
    public void setDistance(String dist) {
        Double distanceDouble = Double.valueOf(dist);
        if(distanceDouble > 1000) {
            distance.setText(String.format(getString(R.string.distance_unit_km), distanceDouble/1000));
        } else {
            distance.setText(String.format(getString(R.string.distance_unit_m), distanceDouble.intValue()));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(LOCATION_DETAILS_KEY, suggestion);
        outState.putDouble(LOCATION_DETAILS_LATITUDE_KEY, latitude);
        outState.putDouble(LOCATION_DETAILS_LONGITUDE_KEY, longitude);
        outState.putString(LOCATION_DETAILS_STREET_KEY, streetName);
        super.onSaveInstanceState(outState);
    }
}
