package com.github.ruiadrmartins.locationsearcher.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.github.ruiadrmartins.locationsearcher.R;
import com.github.ruiadrmartins.locationsearcher.data.autocomplete.Suggestion;

public class DetailActivity extends AppCompatActivity implements DetailViewInterface {

    public static final String LOCATION_DETAILS_KEY = "locationDetails";

    private DetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new DetailPresenter(this, getApplication());

        Suggestion s = getIntent().getParcelableExtra(LOCATION_DETAILS_KEY);
        presenter.getLocationDetails(s.getLocationId());

    }

}
