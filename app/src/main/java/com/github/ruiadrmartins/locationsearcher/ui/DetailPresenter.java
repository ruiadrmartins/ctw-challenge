package com.github.ruiadrmartins.locationsearcher.ui;

import android.app.Application;
import android.util.Log;

import com.github.ruiadrmartins.locationsearcher.LocationSearcherApplication;
import com.github.ruiadrmartins.locationsearcher.network.NetworkInterface;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;



public class DetailPresenter implements DetailPresenterInterface {

    private Application application;
    private DetailViewInterface dvi;

    @Inject
    NetworkInterface network;

    public DetailPresenter(DetailViewInterface dvi, Application application) {
        this.application = application;
        this.dvi = dvi;
    }

    @Override
    public void getLocationDetails(String locationId) {
        ((LocationSearcherApplication) application).getAppComponent().inject(this);

        Disposable observable = Observable.fromCallable(() -> network.startGetLocationDetails(application.getBaseContext(), locationId))
                .retry(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            String latitude = String.valueOf(result.getResponse().getView().get(0).getResult().get(0).getLocation().getDisplayPosition().getLatitude());
                            String longitude = String.valueOf(result.getResponse().getView().get(0).getResult().get(0).getLocation().getDisplayPosition().getLongitude());
                            String streetName = result.getResponse().getView().get(0).getResult().get(0).getLocation().getAddress().getLabel();
                            String coords = latitude + "," + longitude;
                            dvi.updateMap(Double.valueOf(latitude), Double.valueOf(longitude));
                            dvi.setCoords(coords);
                            dvi.setStreet(streetName);
                        },
                        error -> {
                            Log.v("HMM",error.getMessage());
                        });
    }
}
