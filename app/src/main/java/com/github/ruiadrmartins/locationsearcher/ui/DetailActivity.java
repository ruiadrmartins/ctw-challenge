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

public class DetailActivity extends AppCompatActivity implements DetailViewInterface {

    public static final String LOCATION_DETAILS_KEY = "locationDetails";
    public static final String LOCATION_DETAILS_LATITUDE_KEY = "locationLatitude";
    public static final String LOCATION_DETAILS_LONGITUDE_KEY = "locationLongitude";
    public static final String LOCATION_DETAILS_STREET_KEY = "streetName";

    private LinearLayout linearLayout;
    private ProgressLinearLayout progressLinearLayout;

    private DetailPresenter presenter;

    private Map map = null;
    private MapFragment mapFragment = null;

    private Suggestion suggestion;

    private TextView street;
    private TextView postalCode;
    private TextView coordenates;
    private TextView distance;

    private double latitude;
    private double longitude;
    private String streetName;

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

        progressLinearLayout = findViewById(R.id.progress_linear_layout);

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
                showError(getString(R.string.error_list_description));
            }
        }

        if(suggestion != null) {
            setPostalCode(suggestion.getAddress().getPostalCode());
            setDistance(suggestion.getDistance());
        } else {
            showError(getString(R.string.error_list_description));
        }
    }

    @Override
    public void showError(String error) {
        progressLinearLayout.showError(
                R.drawable.ic_error_black_24dp,
                getString(R.string.error_list_title), getString(R.string.error_list_description),
                error,
                view -> Toast.makeText(this, "Lol.", Toast.LENGTH_SHORT).show()
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
        coordenates.setText(coords);
    }

    @Override
    public void setDistance(String dist) {
        distance.setText(String.format(getString(R.string.distance_unit), dist));
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
