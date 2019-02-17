package com.example.lucia.bakingapp.UI;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lucia.bakingapp.R;
import com.example.lucia.bakingapp.adapters.StepsAdapter;
import com.example.lucia.bakingapp.data.Ingredient;
import com.example.lucia.bakingapp.data.Recipe;
import com.example.lucia.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.lucia.bakingapp.utils.Constants.SELECTED_RECIPE;

public class DetailFragment extends Fragment implements StepsAdapter.StepsOnClickListener {

    public String widgetRecipeText = "";
    onListItemClickListener callback;
    ArrayList<Recipe> recipe;
    @BindString(R.string.display_ingredient)
    String mDisplayIngredient;
    @BindView(R.id.text_view_ingredients)
    TextView ingredientTextView;
    private Unbinder unbinder;

    public DetailFragment() {
    }

    @Override
    public void onItemClick(int index) {
        callback.onStepSelected(index);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (onListItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onRecipeClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_detail_recipe_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (getArguments() != null) {

            recipe = getArguments().getParcelableArrayList(SELECTED_RECIPE);

            displayIngredients();

            SharedPreferences preferences = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("preference_ingredients", widgetRecipeText);
            editor.apply();

            RecyclerView stepsRecyclerView = rootView.findViewById(R.id.recycler_view_steps);

            List<Step> steps = recipe.get(0).getSteps();

            RecyclerView.LayoutManager stepsLayoutManager = new LinearLayoutManager(getActivity());
            StepsAdapter stepsAdapter = new StepsAdapter(getActivity(), steps, this);

            stepsRecyclerView.setAdapter(stepsAdapter);
            stepsRecyclerView.setLayoutManager(stepsLayoutManager);
            stepsRecyclerView.setNestedScrollingEnabled(false);

        }
        return rootView;


    }


    @TargetApi(Build.VERSION_CODES.N)
    public void displayIngredients() {
        List<Ingredient> ingredients = recipe.get(0).getIngredients();

        ingredients.forEach((ingredient) ->
        {
            ingredientTextView.append("\u2022 " + ingredient.getIngredient());
            ingredientTextView.append("\t\t\t" + ingredient.getIngredientQuantity().toString());
            ingredientTextView.append("\t\t\t" + ingredient.getIngredientMeasure() + "\n\n");

            widgetRecipeText = widgetRecipeText + "\u2022 " + ingredient.getIngredient();
            widgetRecipeText = widgetRecipeText + "\t\t\t" + ingredient.getIngredientQuantity().toString();
            widgetRecipeText = widgetRecipeText + "\t\t\t" + ingredient.getIngredientMeasure() + "\n\n";
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface onListItemClickListener {
        void onStepSelected(int index);
    }
}

