package com.github.ruiadrmartins.locationsearcher.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.github.ruiadrmartins.locationsearcher.R;
import com.github.ruiadrmartins.locationsearcher.adapter.LocationAdapter;
import com.github.ruiadrmartins.locationsearcher.data.Suggestion;
import com.github.ruiadrmartins.locationsearcher.network.NetworkController;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainViewInterface, SearchView.OnQueryTextListener {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        presenter = new MainPresenter(this, getApplication());

        searchView = findViewById(R.id.search_view);
        searchView.setQueryHint(getString(R.string.search_hint)); // Por alguma razao, a versao support nao permite queries no xml
        searchView.setOnQueryTextListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        updateData(new ArrayList<>());
    }

    @Override
    public void updateData(List<String> list){
        adapter = new LocationAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }


    // Search Query specific methods
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        presenter.getLocations(s);

        /*
        NetworkController network = new NetworkController();

        Disposable observable = Observable.fromCallable(() -> network.start(s))
                .retry(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            List<String> list = new ArrayList<>();
                            for(Suggestion suggestion : result.getSuggestions()) {
                                list.add(suggestion.getLabel());
                            }
                            updateData(list);
                        },
                        error -> {});*/
        //observable.dispose();

        //adapter.getFilter().filter(s);
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
}
