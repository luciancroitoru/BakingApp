package com.example.lucia.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.lucia.bakingapp.UI.StepDetailFragment;
import com.example.lucia.bakingapp.data.Recipe;
import com.example.lucia.bakingapp.utils.Constants;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class StepDetailActivity extends AppCompatActivity {

    private Recipe recipe;
    private int stepId;
    private int stepCount;
    private Bundle stepBundle;
    private ArrayList<Recipe> selectedRecipe;
    private FragmentManager fragmentManager;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            extras = getIntent().getExtras();
            stepId = extras.getInt(Constants.SELECTED_STEP);
            stepCount = extras.getInt(Constants.STEP_COUNT);
        }

        recipe = DetailActivity.recipe.get(0);
        setTitle(recipe.getName());
        fragmentManager = getSupportFragmentManager();

        // Create fragment instance only once
        if (savedInstanceState == null){
            displayStepFragment();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.STATE_SELECTED_STEP, stepId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            stepId = savedInstanceState.getInt(Constants.STATE_SELECTED_STEP);
        } else {
            displayStepFragment();
        }
    }

    /**
     * OnClick handlers for step navigation buttons
     */
    @OnClick({R.id.button_previous, R.id.button_next})
    public void setViewOnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.button_previous:
                submitPrevious();
                break;

            case R.id.button_next:
                submitNext();
                break;
        }
    }

    /**
     * Method to navigate to previous step of selected recipe
     */
    public void submitPrevious() {
        if (stepId > 0) {
            stepId--;
            displayStepFragment();
        }
    }

    /**
     * Method to navigate to next step of selected recipe
     */
    public void submitNext() {
        if (stepId < (stepCount - 1)) {
            stepId++;
            displayStepFragment();
        }
    }


    public void displayStepFragment(){
        stepBundle = new Bundle();
        selectedRecipe = new ArrayList<>();
        selectedRecipe.add(recipe);
        stepBundle.putParcelableArrayList(Constants.SELECTED_RECIPE, selectedRecipe);
        stepBundle.putInt(Constants.SELECTED_STEP, stepId);
        stepBundle.putInt(Constants.STEP_COUNT, stepCount);

        StepDetailFragment stepDetailFragment = new StepDetailFragment();

        stepDetailFragment.setArguments(stepBundle);
        fragmentManager
                .beginTransaction()
                .replace(R.id.detail_step_fragment_container, stepDetailFragment)
                .commit();
    }

}

