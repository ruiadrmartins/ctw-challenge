package com.github.ruiadrmartins.locationsearcher.ui;

import android.app.Application;

import com.github.ruiadrmartins.locationsearcher.LocationSearcherApplication;
import com.github.ruiadrmartins.locationsearcher.R;
import com.github.ruiadrmartins.locationsearcher.network.NetworkInterface;
import com.github.ruiadrmartins.locationsearcher.util.Utilities;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailPresenter implements DetailPresenterInterface {

    private Application application;
    private DetailViewInterface dvi;
    private Disposable observable;

    @Inject
    NetworkInterface network;

    DetailPresenter(DetailViewInterface dvi, Application application) {
        this.application = application;
        this.dvi = dvi;
    }

    @Override
    public void getLocationDetails(String locationId) {
        if(Utilities.isNetworkConnected(application)) {
            ((LocationSearcherApplication) application).getAppComponent().inject(this);

            observable = Observable.fromCallable(() -> network.startGetLocationDetails(application.getBaseContext(), locationId))
                    .retry(5)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            result -> {
                                if(result!= null) {
                                    String latitude = String.valueOf(result.getResponse().getView().get(0).getResult().get(0).getLocation().getDisplayPosition().getLatitude());
                                    String longitude = String.valueOf(result.getResponse().getView().get(0).getResult().get(0).getLocation().getDisplayPosition().getLongitude());
                                    String streetName = result.getResponse().getView().get(0).getResult().get(0).getLocation().getAddress().getLabel();
                                    dvi.updateMap(Double.valueOf(latitude), Double.valueOf(longitude));
                                    dvi.setCoords(Double.valueOf(latitude), Double.valueOf(longitude));
                                    dvi.setStreet(streetName);
                                } else {
                                    dvi.showGetDetailsError(application.getString(R.string.generic_error_message));
                                }
                            },
                            error -> dvi.showGetDetailsError(error.getMessage()));
        } else {
            dvi.showGetDetailsError(application.getString(R.string.network_not_connected));
        }
    }

    @Override
    public void close() {
        if(observable != null && !observable.isDisposed()) {
            observable.dispose();
        }
    }
}
