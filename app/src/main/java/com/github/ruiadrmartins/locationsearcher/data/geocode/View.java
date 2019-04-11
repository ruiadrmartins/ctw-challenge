package com.github.ruiadrmartins.locationsearcher.data.geocode;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class View {

    @SerializedName("_type")
    @Expose
    private String type;
    @SerializedName("ViewId")
    @Expose
    private Integer viewId;
    @SerializedName("Result")
    @Expose
    private List<Result> result = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getViewId() {
        return viewId;
    }

    public void setViewId(Integer viewId) {
        this.viewId = viewId;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

}
