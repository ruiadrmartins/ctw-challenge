package com.github.ruiadrmartins.locationsearcher.data.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("LocationId")
    @Expose
    private String locationId;
    @SerializedName("LocationType")
    @Expose
    private String locationType;
    @SerializedName("DisplayPosition")
    @Expose
    private DisplayPosition displayPosition;
    @SerializedName("MapView")
    @Expose
    private MapView mapView;
    @SerializedName("Address")
    @Expose
    private Address address;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public DisplayPosition getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(DisplayPosition displayPosition) {
        this.displayPosition = displayPosition;
    }

    public MapView getMapView() {
        return mapView;
    }

    public void setMapView(MapView mapView) {
        this.mapView = mapView;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

}