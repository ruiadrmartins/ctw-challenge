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
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.github.ruiadrmartins.locationsearcher.TestUtilities.isSortedByDistance;
import static com.github.ruiadrmartins.locationsearcher.TestUtilities.waitId;


@RunWith(AndroidJUnit4.class)
public class Story1Test {

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public GrantPermissionRule permissionRule2 = GrantPermissionRule.grant(Manifest.permission.ACCESS_COARSE_LOCATION);

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
        openActionBarOverflowOrOptionsMenu(mainActivityActivityTestRule.getActivity());
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).check(matches(isDisplayed()));
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).perform(click());
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("A"));
        onView(withId(android.support.design.R.id.search_src_text)).check(matches(withText("A")));
        closeSoftKeyboard();

        onView(isRoot()).perform(waitId(R.id.recycler_view, 500));

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(0));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(0, R.id.label_text)).check(matches(withText("Ghana, Greater Accra, Accra")));
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(1));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(1, R.id.label_text)).check(matches(withText("Ghana, Greater Accra, Accra, Accra")));
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(2));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(2, R.id.label_text)).check(matches(withText("Ghana, Ashanti, Amansie West")));
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(3));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(3, R.id.label_text)).check(matches(withText("Ghana, Ashanti")));
    }

    // 3. Locations must be sorted by distance
    @Test
    public void locationIsSortedByDistanceTest() {
        openActionBarOverflowOrOptionsMenu(mainActivityActivityTestRule.getActivity());
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).check(matches(isDisplayed()));
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).perform(click());
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(replaceText("A"));
        closeSoftKeyboard();

        onView(isRoot()).perform(waitId(R.id.recycler_view, 500));

        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(isSortedByDistance()));
    }

    // 4. The user must be able to scroll through the list of locations
    @Test
    public void locationListIsScrollableTest() {
        openActionBarOverflowOrOptionsMenu(mainActivityActivityTestRule.getActivity());
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).check(matches(isDisplayed()));
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).perform(click());
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(replaceText("A"));
        closeSoftKeyboard();
        onView(withText("Angola, Viana, Auto Estrada Golfe Camama")).check(doesNotExist());
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(19));
        onView(withText("Angola, Viana, Auto Estrada Golfe Camama")).check(matches(isDisplayed()));
    }

    // 5. Bonus: Search box should behave like an autocomplete (update the list while user is typing)
    @Test
    public void autocompleteSearchTest() {
        openActionBarOverflowOrOptionsMenu(mainActivityActivityTestRule.getActivity());
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).check(matches(isDisplayed()));
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_distance))).perform(click());
        onView(withId(R.id.search_view)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(replaceText("A"));
        closeSoftKeyboard();

        onView(isRoot()).perform(waitId(R.id.recycler_view, 500));

        onView(withId(R.id.recycler_view)).perform(scrollToPosition(0));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(0, R.id.label_text)).check(matches(withText("Ghana, Greater Accra, Accra")));
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(1));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(1, R.id.label_text)).check(matches(withText("Ghana, Greater Accra, Accra, Accra")));
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(2));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(2, R.id.label_text)).check(matches(withText("Ghana, Ashanti, Amansie West")));
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(3));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(3, R.id.label_text)).check(matches(withText("Ghana, Ashanti")));
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("B"));
        closeSoftKeyboard();

        onView(isRoot()).perform(waitId(R.id.recycler_view, 500));

        onView(withId(R.id.recycler_view)).perform(scrollToPosition(0));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(0, R.id.label_text)).check(matches(withText("Côte d'Ivoire, Abidjan, Abidjan, Abidjan")));
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(1));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(1, R.id.label_text)).check(matches(withText("Côte d'Ivoire, Abidjan, Abidjan")));
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(2));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(2, R.id.label_text)).check(matches(withText("Côte d'Ivoire, Abidjan")));
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(3));
        onView(new RecyclerViewMatcher(R.id.recycler_view).atPositionOnView(3, R.id.label_text)).check(matches(withText("Côte d'Ivoire, Abidjan, Abidjan, Abidjan, Abidjan")));
    }
}
