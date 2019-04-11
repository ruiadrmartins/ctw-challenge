package com.github.ruiadrmartins.locationsearcher.data.geocode;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address {

    @SerializedName("Label")
    @Expose
    private String label;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("AdditionalData")
    @Expose
    private List<AdditionalDatum> additionalData = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<AdditionalDatum> getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(List<AdditionalDatum> additionalData) {
        this.additionalData = additionalData;
    }

}