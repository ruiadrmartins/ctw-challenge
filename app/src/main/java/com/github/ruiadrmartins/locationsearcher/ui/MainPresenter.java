package com.github.ruiadrmartins.locationsearcher.ui;

import android.app.Application;

import com.github.ruiadrmartins.locationsearcher.LocationSearcherApplication;
import com.github.ruiadrmartins.locationsearcher.data.Suggestion;
import com.github.ruiadrmartins.locationsearcher.network.NetworkInterface;

import java.util.ArrayList;
import java.util.List;

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

    public MainPresenter(MainViewInterface mvi, Application application) {
        this.mvi = mvi;
        this.application = application;
    }

    @Override
    public void getLocations(String text) {

        ((LocationSearcherApplication) application).getAppComponent().inject(this);

        Disposable observable = Observable.fromCallable(() -> network.start(text))
                .retry(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            List<String> list = new ArrayList<>();
                            for(Suggestion suggestion : result.getSuggestions()) {
                                list.add(suggestion.getLabel());
                            }
                            mvi.updateData(list);
                        },
                        error -> {});
    }
}
