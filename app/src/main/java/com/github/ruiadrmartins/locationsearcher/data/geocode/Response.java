package com.github.ruiadrmartins.locationsearcher.data.geocode;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("MetaInfo")
    @Expose
    private MetaInfo metaInfo;
    @SerializedName("View")
    @Expose
    private List<View> view = null;

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public void setMetaInfo(MetaInfo metaInfo) {
        this.metaInfo = metaInfo;
    }

    public List<View> getView() {
        return view;
    }

    public void setView(List<View> view) {
        this.view = view;
    }

}

