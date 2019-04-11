package com.github.ruiadrmartins.locationsearcher.network;

import android.util.Log;

import com.github.ruiadrmartins.locationsearcher.BuildConfig;
import com.github.ruiadrmartins.locationsearcher.data.autocomplete.LocationResult;
import com.github.ruiadrmartins.locationsearcher.data.geocode.LocationDetailsResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkController implements NetworkInterface {

    public NetworkAPI initNetworkController(String url) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(NetworkAPI.class);
    }

    @Override
    public LocationResult startGetLocationsAutocomplete(String query, double longitude, double latitude) {
        try {
            NetworkAPI api = initNetworkController(NetworkAPI.BASE_URL_AUTOCOMPLETE);

            Call<LocationResult> callLocation = api.getLocations(
                    BuildConfig.HERE_APP_ID,
                    BuildConfig.HERE_APP_CODE,
                    query,
                    getCoords(longitude, latitude),
                    "<b>",
                    "</b>",
                    "20");

            Response<LocationResult> responseLocation = callLocation.execute();

            if(responseLocation.isSuccessful()) {
                return responseLocation.body();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCoords(double longitude, double latitude) {
        return String.valueOf(latitude) + "," + String.valueOf(longitude);
    }

    @Override
    public LocationDetailsResult startGetLocationDetails(String locationId) {
        try {
            NetworkAPI api = initNetworkController(NetworkAPI.BASE_URL_GEOCODE);

            Call<LocationDetailsResult> callLocation = api.getLocationDetails(
                    BuildConfig.HERE_APP_ID,
                    BuildConfig.HERE_APP_CODE,
                    locationId);

            Response<LocationDetailsResult> responseLocationDetails = callLocation.execute();

            if(responseLocationDetails.isSuccessful()) {
                Log.v("HMM","a");
                return responseLocationDetails.body();
            } else {
                Log.v("HMM",responseLocationDetails.errorBody().string());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
