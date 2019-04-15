package com.github.ruiadrmartins.locationsearcher.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.ruiadrmartins.locationsearcher.R;
import com.github.ruiadrmartins.locationsearcher.adapter.LocationAdapter;
import com.github.ruiadrmartins.locationsearcher.data.autocomplete.Suggestion;
import com.github.ruiadrmartins.locationsearcher.util.Preferences;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.vlonjatg.progressactivity.ProgressLinearLayout;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements MainViewInterface, SearchView.OnQueryTextListener {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static final int REQUEST_LOCATION_CODE = 101;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    public static final String LOCATION_LIST_KEY = "locationList";
    public static final String LOCATION_LATITUDE_KEY = "locationLatitude";
    public static final String LOCATION_LONGITUDE_KEY = "locationLongitude";

    private SearchView searchView;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;

    private MainPresenter presenter;

    private ArrayList<Suggestion> locationList;

    private double longitude = 0;
    private double latitude = 0;

    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    private ProgressLinearLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new MainPresenter(this, getApplication());

        searchView = findViewById(R.id.search_view);
        searchView.setQueryHint(getString(R.string.search_hint)); // Por alguma razao, a versao support nao permite query hint no xml
        searchView.setOnQueryTextListener(this);
        searchView.setIconifiedByDefault(false);
        //searchView.requestFocus();
        searchView.setOnCloseListener(() -> true);

        searchView.requestFocus();

        recyclerView = findViewById(R.id.recycler_view);

        progressLayout = findViewById(R.id.progress_layout);

        if(savedInstanceState == null) {
            updateData(new ArrayList<>());
        } else {
            updateData(savedInstanceState.getParcelableArrayList(LOCATION_LIST_KEY));
            latitude = savedInstanceState.getDouble(LOCATION_LATITUDE_KEY);
            longitude = savedInstanceState.getDouble(LOCATION_LONGITUDE_KEY);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_CODE);
            }
        }
    }

    @Override
    public void showEmpty() {
        progressLayout.showEmpty(
                R.drawable.ic_view_list_black_24dp,
                getString(R.string.empty_list_title), getString(R.string.empty_list_description)
        );
    }

    @Override
    public void showError(String error) {
        progressLayout.showError(
                R.drawable.ic_error_black_24dp,
                getString(R.string.error_list_title), error,
                getString(R.string.error_list_button),
                view -> Toast.makeText(this, "Lol.", Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void updateData(ArrayList<Suggestion> list){
        locationList = list;
        Collections.sort(locationList);

        adapter = new LocationAdapter(this, locationList);
        recyclerView.setAdapter(adapter);
        if(list.size() == 0) {
            showEmpty();
        } else {
            progressLayout.showContent();
        }
    }

    // Search Query specific methods
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        //progressLayout.showLoading();

        if(s.isEmpty()) {
            updateData(new ArrayList<>());
        } else {
            presenter.getLocations(s, longitude, latitude);
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_distance) {
            Preferences.setSortPreference(getBaseContext(), Preferences.SORT_BY_DISTANCE);
            item.setChecked(true);
            return true;
        }
        if (id == R.id.action_sort_name) {
            Preferences.setSortPreference(getBaseContext(), Preferences.SORT_BY_NAME);
            item.setChecked(true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(LOCATION_LIST_KEY, locationList);
        outState.putDouble(LOCATION_LATITUDE_KEY, latitude);
        outState.putDouble(LOCATION_LONGITUDE_KEY, longitude);
        super.onSaveInstanceState(outState);
    }

    // Location stuff
    // Inspired from https://github.com/googlesamples/android-play-location/tree/master/LocationUpdates
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                updateLocation();
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        updateLocation();
                        break;
                }
                break;
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, Looper.myLooper());
                    updateLocation();
                })
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    if(statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        try {
                            ResolvableApiException rae = (ResolvableApiException) e;
                            rae.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sie) {
                            sie.printStackTrace();
                        }
                    }
                    updateLocation();
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
        }
    }

    private void updateLocation() {
        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
        startLocationUpdates();
    }
}
