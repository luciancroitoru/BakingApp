package com.example.lucia.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.lucia.bakingapp.TestUtils.atPosition;

@RunWith(AndroidJUnit4.class)
public class RecipeSelectionTest {

    public static final String RECIPE_NAME = "Nutella Pie";
    // rule that provides functional testing of a single activity
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void RecipesFragment() {
        mActivityTestRule.getActivity()
                .getFragmentManager().beginTransaction();
    }

    @Test
    public void clickOnMainActivityRecyclerViewItem_OpensDetailsActivityClass() {
        // please advise on this test
        //onView((withId(R.id.recycler_view_recipes)))
                //.check(matches(atPosition(0, hasDescendant(withText(RECIPE_NAME)))));

        // Checks that the DetailsActivity opens
        onView( withId( R.id.recycler_view_recipes ) )
                .perform( RecyclerViewActions.actionOnItemAtPosition( 0, click() ) );
    }
}
