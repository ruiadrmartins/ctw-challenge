package com.github.ruiadrmartins.locationsearcher.data.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MapView {

    @SerializedName("TopLeft")
    @Expose
    private TopLeft topLeft;
    @SerializedName("BottomRight")
    @Expose
    private BottomRight bottomRight;

    public TopLeft getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(TopLeft topLeft) {
        this.topLeft = topLeft;
    }

    public BottomRight getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(BottomRight bottomRight) {
        this.bottomRight = bottomRight;
    }

}