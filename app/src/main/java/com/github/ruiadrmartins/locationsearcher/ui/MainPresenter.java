package com.github.ruiadrmartins.locationsearcher.ui;

import android.app.Application;

import com.github.ruiadrmartins.locationsearcher.LocationSearcherApplication;
import com.github.ruiadrmartins.locationsearcher.network.NetworkInterface;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainPresenterInterface {

    private MainViewInterface mvi;
    private Application application;

    @Inject
    NetworkInterface network;

    MainPresenter(MainViewInterface mvi, Application application) {
        this.mvi = mvi;
        this.application = application;
    }

    @Override
    public void getLocations(String text, double longitude, double latitude) {

        ((LocationSearcherApplication) application).getAppComponent().inject(this);

        Disposable observable = Observable.fromCallable(() -> network.start(text, longitude, latitude))
                .retry(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> mvi.updateData(new ArrayList<>(result.getSuggestions())),
                        error -> {});
    }
}
