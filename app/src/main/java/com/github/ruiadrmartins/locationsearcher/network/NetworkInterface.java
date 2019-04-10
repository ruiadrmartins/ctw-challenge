package com.github.ruiadrmartins.locationsearcher.network;

import com.github.ruiadrmartins.locationsearcher.data.LocationResult;

public interface NetworkInterface {
    LocationResult start(String query, double longitude, double latitude);
}
