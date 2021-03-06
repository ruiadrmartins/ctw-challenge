package com.github.ruiadrmartins.locationsearcher.ui;

import android.app.Application;

import com.github.ruiadrmartins.locationsearcher.LocationSearcherApplication;
import com.github.ruiadrmartins.locationsearcher.R;
import com.github.ruiadrmartins.locationsearcher.network.NetworkInterface;
import com.github.ruiadrmartins.locationsearcher.util.Utilities;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainPresenterInterface {

    private MainViewInterface mvi;
    private Application application;
    private Disposable observable;

    @Inject
    NetworkInterface network;

    MainPresenter(MainViewInterface mvi, Application application) {
        this.mvi = mvi;
        this.application = application;
    }

    @Override
    public void getLocations(String text, double longitude, double latitude) {
        if(Utilities.isNetworkConnected(application)) {
            ((LocationSearcherApplication) application).getAppComponent().inject(this);

            observable = Observable.fromCallable(() -> network.startGetLocationsAutocomplete(application.getBaseContext(), text, longitude, latitude))
                    .retry(5)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            result -> {
                                if(result != null) {
                                    mvi.updateData(new ArrayList<>(result.getSuggestions()));
                                } else {
                                    mvi.showError(application.getString(R.string.generic_error_message));
                                }
                            },
                            error -> mvi.showError(error.getMessage()));
        } else {
            mvi.showError(application.getString(R.string.network_not_connected));
        }
    }

    @Override
    public void close() {
        if(observable != null && !observable.isDisposed()) {
            observable.dispose();
        }
    }
}
