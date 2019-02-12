package com.example.lucia.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DetailActivityContainerVisibilityTest {

    @Rule
    public ActivityTestRule<DetailActivity> mActivityTestRule = new ActivityTestRule<>(DetailActivity.class);

    @Test
    public void testDetailActivityFrameContainer_Visibility() {
        onView(withId(R.id.detail_fragment_container))
                .check(matches(isDisplayed()));
    }
}
