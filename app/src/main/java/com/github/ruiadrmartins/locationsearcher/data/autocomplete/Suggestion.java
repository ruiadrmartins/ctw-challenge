package com.github.ruiadrmartins.locationsearcher.data.autocomplete;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Generated by http://www.jsonschema2pojo.org/ and http://www.parcelabler.com/
 */
public class Suggestion implements Comparable<Suggestion>, Parcelable {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("locationId")
    @Expose
    private String locationId;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("matchLevel")
    @Expose
    private String matchLevel;
    @SerializedName("distance")
    @Expose
    private String distance;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getMatchLevel() {
        return matchLevel;
    }

    public void setMatchLevel(String matchLevel) {
        this.matchLevel = matchLevel;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    // To sort results
    @Override
    public int compareTo(@NonNull Suggestion suggestion) {
        if(getDistance() == null || suggestion.getDistance() == null) {
            return 0;
        } else {
            return Integer.valueOf(getDistance()).compareTo(Integer.valueOf(suggestion.getDistance()));
        }
    }

    // Parcelable stuff
    protected Suggestion(Parcel in) {
        label = in.readString();
        language = in.readString();
        countryCode = in.readString();
        locationId = in.readString();
        address = (Address) in.readValue(Address.class.getClassLoader());
        matchLevel = in.readString();
        distance = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(label);
        parcel.writeString(language);
        parcel.writeString(countryCode);
        parcel.writeString(locationId);
        parcel.writeValue(address);
        parcel.writeString(matchLevel);
        parcel.writeString(distance);
    }

    public static final Parcelable.Creator<Suggestion> CREATOR = new Parcelable.Creator<Suggestion>() {
        @Override
        public Suggestion createFromParcel(Parcel in) {
            return new Suggestion(in);
        }

        @Override
        public Suggestion[] newArray(int size) {
            return new Suggestion[size];
        }
    };
}