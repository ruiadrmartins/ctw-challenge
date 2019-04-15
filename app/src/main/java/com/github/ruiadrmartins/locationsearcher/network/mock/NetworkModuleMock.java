package com.github.ruiadrmartins.locationsearcher.network.mock;

import com.github.ruiadrmartins.locationsearcher.network.NetworkInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModuleMock {

    @Provides
    @Singleton
    static NetworkInterface provideNetworkController() {
        return new NetworkControllerMock();
    }
}
