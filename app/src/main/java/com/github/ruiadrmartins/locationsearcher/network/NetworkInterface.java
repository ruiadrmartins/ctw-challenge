package com.github.ruiadrmartins.locationsearcher.network;

import android.content.Context;

import com.github.ruiadrmartins.locationsearcher.data.autocomplete.LocationResult;
import com.github.ruiadrmartins.locationsearcher.data.geocode.LocationDetailsResult;

public interface NetworkInterface {
    LocationResult startGetLocationsAutocomplete(Context context,  String query, double longitude, double latitude);
    LocationDetailsResult startGetLocationDetails(Context context, String locationId);
}
