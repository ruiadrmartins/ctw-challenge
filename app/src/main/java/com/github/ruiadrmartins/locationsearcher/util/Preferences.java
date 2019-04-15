package com.github.ruiadrmartins.locationsearcher.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    public static final int SORT_BY_DISTANCE = 0;
    public static final int SORT_BY_NAME = 1;

    public static int getSortPreference(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt("sort", SORT_BY_DISTANCE);
    }

    public static void setSortPreference(Context context, int sort) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("sort", sort);
        editor.apply();
    }
}
