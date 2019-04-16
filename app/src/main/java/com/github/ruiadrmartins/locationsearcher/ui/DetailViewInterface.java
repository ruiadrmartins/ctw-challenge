package com.github.ruiadrmartins.locationsearcher.ui;

public interface DetailViewInterface {
    void updateMap(double latitude, double longitude);
    void setStreet(String streetName);
    void setPostalCode(String pCode);
    void setCoords(double latitude, double longitude);
    void setDistance(String dist);

    void showGetDetailsError(String error);
    void showError(String error);
}
