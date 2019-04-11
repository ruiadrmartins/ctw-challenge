package com.github.ruiadrmartins.locationsearcher.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.ruiadrmartins.locationsearcher.R;
import com.github.ruiadrmartins.locationsearcher.adapter.LocationAdapter;
import com.github.ruiadrmartins.locationsearcher.data.Suggestion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements MainViewInterface, SearchView.OnQueryTextListener {

    public static final int REQUEST_LOCATION_CODE = 101;
    public static final String LOCATION_LIST_KEY = "locationList";

    private SearchView searchView;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;

    private MainPresenter presenter;

    private ArrayList<Suggestion> locationList;

    private double longitude = 0;
    private double latitude = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initLocation();

        presenter = new MainPresenter(this, getApplication());

        searchView = findViewById(R.id.search_view);
        searchView.setQueryHint(getString(R.string.search_hint)); // Por alguma razao, a versao support nao permite query hint no xml
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(() -> true);

        recyclerView = findViewById(R.id.recycler_view);

        if(savedInstanceState == null) {
            updateData(new ArrayList<>());
        } else {
            updateData(savedInstanceState.getParcelableArrayList(LOCATION_LIST_KEY));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void updateData(ArrayList<Suggestion> list){
        locationList = list;
        Collections.sort(locationList);

        adapter = new LocationAdapter(this, locationList);
        recyclerView.setAdapter(adapter);
    }

    private void initLocation() {
        FusedLocationProviderClient fusedLocationClient;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_CODE);
            }
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if(location!=null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }
        });
    }


    // Search Query specific methods
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if(s.isEmpty()) {
            updateData(new ArrayList<>());
        } else {
            presenter.getLocations(s, longitude, latitude);
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(LOCATION_LIST_KEY, locationList);
        super.onSaveInstanceState(outState);
    }
}
