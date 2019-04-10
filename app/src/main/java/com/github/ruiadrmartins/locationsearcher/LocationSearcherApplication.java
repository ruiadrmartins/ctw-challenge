package com.github.ruiadrmartins.locationsearcher;

import android.app.Application;

import com.github.ruiadrmartins.locationsearcher.network.AppComponent;
import com.github.ruiadrmartins.locationsearcher.network.DaggerAppComponent;
import com.github.ruiadrmartins.locationsearcher.network.NetworkModule;

public class LocationSearcherApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = generateAppComponent();
    }
    public AppComponent getAppComponent() {
        return appComponent;
    }

    private AppComponent generateAppComponent() {
        return DaggerAppComponent.builder().networkModule(new NetworkModule()).build();
    }
}
