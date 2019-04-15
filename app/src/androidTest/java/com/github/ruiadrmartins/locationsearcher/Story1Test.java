package com.github.ruiadrmartins.locationsearcher;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.ruiadrmartins.locationsearcher.adapter.LocationAdapter;
import com.github.ruiadrmartins.locationsearcher.data.autocomplete.Suggestion;
import com.github.ruiadrmartins.locationsearcher.network.AppComponent;
import com.github.ruiadrmartins.locationsearcher.network.mock.DaggerAppComponentMock;
import com.github.ruiadrmartins.locationsearcher.network.mock.NetworkModuleMock;
import com.github.ruiadrmartins.locationsearcher.ui.MainActivity;
import com.google.common.collect.Ordering;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class Story1Test {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class) {

        @Override
        protected void beforeActivityLaunched() {

            LocationSearcherApplication application = (LocationSearcherApplication) InstrumentationRegistry
                    .getInstrumentation()
                    .getTargetContext()
                    .getApplicationContext();
            AppComponent appComponent = DaggerAppComponentMock
                    .builder()
                    .networkModuleMock(new NetworkModuleMock())
                    .build();
            application.setAppComponent(appComponent);
        }
    };

    // 1. The user must be able to insert text in a search box and results should be presented
    // 2. Locations are presented in a list format
    @Test
    public void insertInSearchBoxAndPresentResultTest() {
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("A"));
        onView(withId(android.support.design.R.id.search_src_text)).check(matches(withText("A")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(0, R.id.label_text)).check(matches(withText("Ghana, Greater Accra, Accra")));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(1, R.id.label_text)).check(matches(withText("Ghana, Greater Accra, Accra, Accra")));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(2, R.id.label_text)).check(matches(withText("Ghana, Ashanti, Amansie West")));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(3, R.id.label_text)).check(matches(withText("Ghana, Ashanti")));

    }

    // 3. Locations must be sorted by distance
    @Test
    public void locationIsSortedByDistanceTest() {
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(replaceText("A"));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(isSortedByDistance()));

    }

    // 4. The user must be able to scroll through the list of locations
    @Test
    public void locationListIsScrollableTest() {
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(replaceText("A"));
        onView(withText("Angola, Viana, Auto Estrada Golfe Camama")).check(doesNotExist());
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(19));
        onView(withText("Angola, Viana, Auto Estrada Golfe Camama")).check(matches(isDisplayed()));
    }

    // 5. Bonus: Search box should behave like an autocomplete (update the list while user is typing)
    @Test
    public void autocompleteSearchTest() {
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(replaceText("A"));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Ghana, Greater Accra, Accra"))));
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Ghana, Greater Accra, Accra, Accra"))));
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Ghana, Ashanti, Amansie West"))));
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Ghana, Ashanti"))));
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("B"));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Côte d'Ivoire, Abidjan, Abidjan, Abidjan"))));
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Côte d'Ivoire, Abidjan, Abidjan"))));
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Côte d'Ivoire, Abidjan"))));
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText("Côte d'Ivoire, Abidjan, Abidjan, Abidjan, Abidjan"))));
    }

    /**
     * Adapted from https://blog.egorand.me/testing-a-sorted-list-with-espresso/
     */
    private static Matcher<View> isSortedByDistance() {
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
                description.appendText("has items sorted alphabetically: " + distances);
            }
        };
    }
}
