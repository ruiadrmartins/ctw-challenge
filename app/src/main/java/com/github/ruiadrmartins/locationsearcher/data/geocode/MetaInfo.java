package com.github.ruiadrmartins.locationsearcher.data.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaInfo {

    @SerializedName("Timestamp")
    @Expose
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}