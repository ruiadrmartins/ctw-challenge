package com.github.ruiadrmartins.locationsearcher.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Generated by http://www.jsonschema2pojo.org/
 */
public class LocationResult {

    @SerializedName("suggestions")
    @Expose
    private List<Suggestion> suggestions = null;

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }

}