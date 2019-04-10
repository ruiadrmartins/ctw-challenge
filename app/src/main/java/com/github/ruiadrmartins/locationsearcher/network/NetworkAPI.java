package com.github.ruiadrmartins.locationsearcher.network;

import com.github.ruiadrmartins.locationsearcher.data.LocationResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkAPI {

    String BASE_URL = "http://autocomplete.geocoder.api.here.com/6.2/";

    @GET("suggest.json")
    Call<LocationResult> getLocations(
            @Query("app_id") String appId,
            @Query("app_code") String appCode,
            @Query("query") String query,
            @Query("prox") String prox,
            @Query("beginHighlight") String beginHighlight,
            @Query("endHighlight") String endHighlight,
            @Query("maxresults") String maxresults);

}
