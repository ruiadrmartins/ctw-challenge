package com.github.ruiadrmartins.locationsearcher;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.ruiadrmartins.locationsearcher.adapter.LocationAdapter;
import com.github.ruiadrmartins.locationsearcher.data.autocomplete.Suggestion;
import com.google.common.collect.Ordering;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;

import static com.github.ruiadrmartins.locationsearcher.util.Utilities.cleanupBreaks;

public class TestUtilities {

    /**
     * Adapted from https://blog.egorand.me/testing-a-sorted-list-with-espresso/
     */
    static Matcher<View> isSortedByDistance() {
        return new TypeSafeMatcher<View>() {

            private final List<Integer> distances = new ArrayList<>();

            @Override
            protected boolean matchesSafely(View item) {
                RecyclerView recyclerView = (RecyclerView) item;
                LocationAdapter adapter = (LocationAdapter) recyclerView.getAdapter();
                distances.clear();
                distances.addAll(extractDistances(adapter.getLocationList()));

                // Returns true if ordered from smaller to bigger integer
                return Ordering.natural().isOrdered(distances);
            }

            private List<Integer> extractDistances(List<Suggestion> locationList) {
                List<Integer> distances = new ArrayList<>();
                for(Suggestion location: locationList) {
                    distances.add(Integer.valueOf(location.getDistance()));
                }
                return distances;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has items sorted by distance: " + distances);
            }
        };
    }

    static Matcher<View> isSortedByName() {
        return new TypeSafeMatcher<View>() {

            private final List<String> names = new ArrayList<>();

            @Override
            protected boolean matchesSafely(View item) {
                RecyclerView recyclerView = (RecyclerView) item;
                LocationAdapter adapter = (LocationAdapter) recyclerView.getAdapter();
                names.clear();
                names.addAll(extractNames(adapter.getLocationList()));

                // Returns true if ordered from smaller to bigger integer
                return Ordering.natural().isOrdered(names);
            }

            private List<String> extractNames(List<Suggestion> locationList) {
                List<String> names = new ArrayList<>();
                for(Suggestion location: locationList) {
                    names.add(cleanupBreaks(location.getLabel()));
                }
                return names;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has items sorted by name: " + names);
            }
        };
    }
}
