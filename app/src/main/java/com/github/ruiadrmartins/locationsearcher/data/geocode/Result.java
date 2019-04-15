package com.github.ruiadrmartins.locationsearcher.data.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("Relevance")
    @Expose
    private Double relevance;
    @SerializedName("MatchLevel")
    @Expose
    private String matchLevel;
    @SerializedName("Location")
    @Expose
    private Location location;

    public Double getRelevance() {
        return relevance;
    }

    public void setRelevance(Double relevance) {
        this.relevance = relevance;
    }

    public String getMatchLevel() {
        return matchLevel;
    }

    public void setMatchLevel(String matchLevel) {
        this.matchLevel = matchLevel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}