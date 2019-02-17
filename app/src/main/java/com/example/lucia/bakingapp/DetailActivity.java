package com.example.lucia.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.lucia.bakingapp.UI.DetailFragment;
import com.example.lucia.bakingapp.UI.StepDetailFragment;
import com.example.lucia.bakingapp.data.Recipe;
import com.example.lucia.bakingapp.utils.Constants;

import java.util.ArrayList;

import butterknife.BindBool;
import butterknife.ButterKnife;


public class DetailActivity extends AppCompatActivity implements DetailFragment.onListItemClickListener {

    public static ArrayList<Recipe> recipe;
    String recipeName;
    private Bundle recipeBundle;
    private FragmentManager fragmentManager;

    @BindBool(R.bool.two_pane_layout) boolean isTwoPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            recipeBundle = getIntent().getExtras();
            recipe = recipeBundle.getParcelableArrayList(Constants.SELECTED_RECIPE);
        }

        if (recipe == null) return;

        // change action bar title to recipe name
        recipeName = recipe.get(0).getName();
        getSupportActionBar().setTitle(recipeName);

        // Create an instance of FragmentManager
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {

            if (isTwoPaneLayout) {

                // Create fragment instance for ingredients and steps
                DetailFragment recipeDetailFragment = new DetailFragment();
                recipeBundle.putInt(Constants.SELECTED_STEP, 0);
                recipeDetailFragment.setArguments(recipeBundle);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.detail_fragment_container, recipeDetailFragment)
                        .addToBackStack(Constants.STACK_RECIPE_DETAIL)
                        .commit();

                // Create fragment instance for Step Details
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                recipeBundle.putInt(Constants.SELECTED_STEP, 0);
                stepDetailFragment.setArguments(recipeBundle);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.detail_step_fragment_container, stepDetailFragment)
                        .addToBackStack(Constants.STACK_RECIPE_STEP_DETAIL)
                        .commit();
            } else {

                // Create fragment instance for ingredients and steps
                DetailFragment recipeDetailFragment = new DetailFragment();
                recipeDetailFragment.setArguments(recipeBundle);
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.detail_fragment_container, recipeDetailFragment)
                        .commit();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        BakingWidgetService.startActionUpdateWidget(this);
    }


    public void onClickIngredients(View view) {
        BakingWidgetService.startActionUpdateWidget(this);
    }


    @Override
    public void onStepSelected(int index) {
        int stepsCount = recipe.get(0).getSteps().size();

        if (isTwoPaneLayout) {
            Bundle stepBundle = new Bundle();
            stepBundle.putParcelableArrayList(Constants.SELECTED_RECIPE, recipe);
            stepBundle.putInt(Constants.SELECTED_STEP, index);
            stepBundle.putInt(Constants.STEP_COUNT, stepsCount);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(stepBundle);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, detailFragment)
                    .commit();

            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(stepBundle);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.detail_step_fragment_container, stepDetailFragment)
                    .commit();

        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(Constants.SELECTED_STEP, index);
            intent.putExtra(Constants.STEP_COUNT, stepsCount);
            startActivity(intent);
        }
    }
}
