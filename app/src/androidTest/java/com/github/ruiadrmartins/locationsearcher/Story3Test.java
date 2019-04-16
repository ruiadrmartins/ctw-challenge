package com.github.ruiadrmartins.locationsearcher;

import android.Manifest;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.ruiadrmartins.locationsearcher.network.AppComponent;
import com.github.ruiadrmartins.locationsearcher.network.mock.DaggerAppComponentMock;
import com.github.ruiadrmartins.locationsearcher.network.mock.NetworkModuleMock;
import com.github.ruiadrmartins.locationsearcher.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.ruiadrmartins.locationsearcher.TestUtilities.isSortedByDistance;
import static com.github.ruiadrmartins.locationsearcher.TestUtilities.isSortedByName;
import static com.github.ruiadrmartins.locationsearcher.TestUtilities.waitId;

@RunWith(AndroidJUnit4.class)
public class Story3Test {


    @Rule public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

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

    // 1. Users should open an overflow menu from the top bar
    // 2. "Sort by name", "Sort by distance" should be the options in the menu
    @Test
    public void openOverflowMenu_showOptionsTest() {
        openActionBarOverflowOrOptionsMenu(mainActivityActivityTestRule.getActivity());
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_name))).check(matches(isDisplayed()));
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).check(matches(isDisplayed()));
    }

    // 3. When tapping on any of the options, the list should be updated accordingly
    @Test
    public void tappingOptionsUpdatesListAccordinglyTest() {
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(replaceText("A"));
        closeSoftKeyboard();

        onView(isRoot()).perform(waitId(R.id.recycler_view, 500));

        // Check sorted by distance
        openActionBarOverflowOrOptionsMenu(mainActivityActivityTestRule.getActivity());
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).check(matches(isDisplayed()));
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).perform(click());

        onView(isRoot()).perform(waitId(R.id.recycler_view, 500));

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(isSortedByDistance()));

        // Check sorted by name
        openActionBarOverflowOrOptionsMenu(mainActivityActivityTestRule.getActivity());
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_name))).check(matches(isDisplayed()));
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_name))).perform(click());

        onView(isRoot()).perform(waitId(R.id.recycler_view, 500));

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(isSortedByName()));
    }
}
