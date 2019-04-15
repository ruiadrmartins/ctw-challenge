package com.github.ruiadrmartins.locationsearcher;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.github.ruiadrmartins.locationsearcher.network.AppComponent;
import com.github.ruiadrmartins.locationsearcher.network.mock.DaggerAppComponentMock;
import com.github.ruiadrmartins.locationsearcher.network.mock.NetworkModuleMock;
import com.github.ruiadrmartins.locationsearcher.ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class Story3Test {

    // TODO: Use something else other than sleep, it's not reliable
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
    public void Test() {
        openActionBarOverflowOrOptionsMenu(mainActivityActivityTestRule.getActivity());
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_name))).check(matches(isDisplayed()));
        onView(withText(mainActivityActivityTestRule.getActivity().getString(R.string.action_sort_name))).check(matches(isDisplayed()));
    }

    // TODO: 3. When tapping on any of the options, the list should be updated accordingly
    @Test
    public void Test3() {

    }
}
