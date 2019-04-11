package com.github.ruiadrmartins.locationsearcher.network;

import com.github.ruiadrmartins.locationsearcher.data.autocomplete.LocationResult;
import com.github.ruiadrmartins.locationsearcher.data.geocode.LocationDetailsResult;

public interface NetworkInterface {
    LocationResult startGetLocationsAutocomplete(String query, double longitude, double latitude);
    LocationDetailsResult startGetLocationDetails(String locationId);
}
