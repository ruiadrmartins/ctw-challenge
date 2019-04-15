package com.github.ruiadrmartins.locationsearcher.ui;

import com.github.ruiadrmartins.locationsearcher.data.autocomplete.Suggestion;

import java.util.ArrayList;

public interface MainViewInterface {
    void updateData(ArrayList<Suggestion> list);
    void showEmpty();
    void showError(String error);
}
