package com.github.ruiadrmartins.locationsearcher;

import android.support.test.espresso.PerformException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.util.HumanReadables;
import android.support.test.espresso.util.TreeIterables;
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
import java.util.concurrent.TimeoutException;

import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
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

    /**
     * From https://stackoverflow.com/questions/49796132/android-espresso-wait-for-text-to-appear
     *
     * Perform action of waiting for a specific view id.
     * @param viewId The id of the view to wait for.
     * @param millis The timeout of until when to wait for.
     */
    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }
}
