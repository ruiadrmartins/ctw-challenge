package com.github.ruiadrmartins.locationsearcher.ui;

public interface DetailViewInterface {
    void updateMap(double latitude, double longitude);
    void setStreet(String streetName);
    void setPostalCode(String pCode);
    void setCoords(String coords);
    void setDistance(String dist);
}
