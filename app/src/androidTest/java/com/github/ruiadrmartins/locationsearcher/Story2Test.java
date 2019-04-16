package com.github.ruiadrmartins.locationsearcher;

import android.content.pm.ActivityInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.ruiadrmartins.locationsearcher.network.AppComponent;
import com.github.ruiadrmartins.locationsearcher.network.mock.DaggerAppComponentMock;
import com.github.ruiadrmartins.locationsearcher.network.mock.NetworkModuleMock;
import com.github.ruiadrmartins.locationsearcher.ui.DetailActivity;
import com.github.ruiadrmartins.locationsearcher.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.PositionAssertions.isBelow;
import static android.support.test.espresso.assertion.PositionAssertions.isRightOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class Story2Test {

    // TODO: Use something else other than sleep, it's not reliable
    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<MainActivity>(MainActivity.class) {

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

    // 1. Location details should be fetched
    // 4. Location street, postal code, coordinates and distance should be displayed below the map
    @Test
    public void locationInformationBelowMapTest() {
        int originalOrientation = intentsTestRule.getActivity().getRequestedOrientation();

        intentsTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        openActionBarOverflowOrOptionsMenu(intentsTestRule.getActivity());
        onView(withText(intentsTestRule.getActivity().getString(R.string.action_sort_distance))).check(matches(isDisplayed()));
        onView(withText(intentsTestRule.getActivity().getString(R.string.action_sort_distance))).perform(click());

        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("A"));
        onView(withId(android.support.design.R.id.search_src_text)).check(matches(withText("A")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.street_text)).check(matches(isDisplayed()));
        onView(withId(R.id.street_text)).check(matches(withText("Accra, Ghana")));
        onView(withId(R.id.street_text)).check(isBelow(withId(R.id.mapfragment)));

        onView(withId(R.id.postal_code_text)).check(matches(isDisplayed()));
        onView(withId(R.id.postal_code_text)).check(matches(withText("")));
        onView(withId(R.id.postal_code_text)).check(isBelow(withId(R.id.mapfragment)));

        onView(withId(R.id.coordinates_text)).check(matches(isDisplayed()));
        onView(withId(R.id.coordinates_text)).check(matches(withText("5.57889,-0.23261")));
        onView(withId(R.id.coordinates_text)).check(isBelow(withId(R.id.mapfragment)));

        onView(withId(R.id.distance_text)).check(matches(isDisplayed()));
        onView(withId(R.id.distance_text)).check(matches(withText("620.88km away")));
        onView(withId(R.id.distance_text)).check(isBelow(withId(R.id.mapfragment)));

        intentsTestRule.getActivity().setRequestedOrientation(originalOrientation);
    }

    // 2. Tapping on a location presents a new screen
    @Test
    public void newScreenTest() {
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("A"));
        onView(withId(android.support.design.R.id.search_src_text)).check(matches(withText("A")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(0, click()));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intended(hasComponent(DetailActivity.class.getName()));
    }

    // 3. The details screen must display a map
    @Test
    public void mapDisplayOnDetailScreenTest() {
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("A"));
        onView(withId(android.support.design.R.id.search_src_text)).check(matches(withText("A")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(0, click()));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.mapfragment)).check(matches(isDisplayed()));
    }


    // TODO: 5. The map should contain a pin in the correct place for the selected location
    /*@Test
    public void checkForPinInMapTest() {

    }*/

    // 6. Bonus: When the orientation changes to landscape the map should be on the left of the other address details
    // TODO: Orientation doesn't go back to original
    @Test
    public void orientationChangeInformationLocationTest() {


        int originalOrientation = intentsTestRule.getActivity().getRequestedOrientation();

        intentsTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        openActionBarOverflowOrOptionsMenu(intentsTestRule.getActivity());
        onView(withText(intentsTestRule.getActivity().getString(R.string.action_sort_distance))).check(matches(isDisplayed()));
        onView(withText(intentsTestRule.getActivity().getString(R.string.action_sort_distance))).perform(click());

        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("A"));
        onView(withId(android.support.design.R.id.search_src_text)).check(matches(withText("A")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(actionOnItemAtPosition(0, click()));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.street_text)).check(matches(isDisplayed()));
        onView(withId(R.id.street_text)).check(matches(withText("Accra, Ghana")));
        onView(withId(R.id.street_text)).check(isRightOf(withId(R.id.mapfragment)));

        onView(withId(R.id.postal_code_text)).check(matches(isDisplayed()));
        onView(withId(R.id.postal_code_text)).check(matches(withText("")));
        onView(withId(R.id.postal_code_text)).check(isRightOf(withId(R.id.mapfragment)));

        onView(withId(R.id.coordinates_text)).check(matches(isDisplayed()));
        onView(withId(R.id.coordinates_text)).check(matches(withText("5.57889,-0.23261")));
        onView(withId(R.id.coordinates_text)).check(isRightOf(withId(R.id.mapfragment)));

        onView(withId(R.id.distance_text)).check(matches(isDisplayed()));
        onView(withId(R.id.distance_text)).check(matches(withText("620.88km away")));
        onView(withId(R.id.distance_text)).check(isRightOf(withId(R.id.mapfragment)));

        intentsTestRule.getActivity().setRequestedOrientation(originalOrientation);
    }

}
