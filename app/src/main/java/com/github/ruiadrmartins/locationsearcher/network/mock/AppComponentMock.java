package com.github.ruiadrmartins.locationsearcher.network.mock;

import com.github.ruiadrmartins.locationsearcher.network.AppComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules= NetworkModuleMock.class)
public interface AppComponentMock extends AppComponent {
}
