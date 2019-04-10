package com.github.ruiadrmartins.locationsearcher.network;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    static NetworkInterface provideNetworkController() {
        return new NetworkController();
    }
}
