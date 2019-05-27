package com.example.macstudent.parkingticket;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AddTicketTest {

    // 1. Tell Android what screen you want to test
    // In example below, it will start on MainActivity.java
    @Rule
    public ActivityTestRule activityRule =
            new ActivityTestRule<>(HomeActivity.class);

    // TEST CASES
    @Test
    public void testAddParkingTicket() {
        // Espresso code to open a hamburger menu & click on a menu link
        // ------------
        // 1. open hamburger menu
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open()); // Open Drawer

        // 2. chick on a menu link
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_addticket));


        Utils.pauseTestFor(3000);

        // 3. check that next page is properly dispalyed
        onView(withId(R.id.txtTotalCost)).check(matches(isDisplayed()));

        Utils.pauseTestFor(3000);

        //@TODO: Write your test case code here

    }
}
