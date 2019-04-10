package com.github.ruiadrmartins.locationsearcher.network;

import com.github.ruiadrmartins.locationsearcher.ui.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules= NetworkModule.class)
public interface AppComponent {
    void inject(MainPresenter presenter);
}
