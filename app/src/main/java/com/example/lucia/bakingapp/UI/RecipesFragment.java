package com.example.lucia.bakingapp.UI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lucia.bakingapp.MainActivity;
import com.example.lucia.bakingapp.R;
import com.example.lucia.bakingapp.adapters.RecipeAdapter;
import com.example.lucia.bakingapp.data.Api;
import com.example.lucia.bakingapp.data.Recipe;
import com.example.lucia.bakingapp.data.RetrofitBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import static com.example.lucia.bakingapp.utils.Constants.ALL_RECIPES;

public class RecipesFragment extends Fragment{

    @BindView(R.id.recycler_view_recipes)
    RecyclerView recyclerView;
    @BindView(R.id.main_screen_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.error_loading_recipes_list)
    TextView errorLoadingRecipesList;

    private Unbinder unbinder;
    RecipeAdapter recipesAdapter;

    public RecipesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main_recipe_fragment, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        recipesAdapter =new RecipeAdapter((MainActivity)getActivity());
        recyclerView.setAdapter(recipesAdapter);

        if (rootView.getTag()!=null && rootView.getTag().equals("phone-land")){
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
            recyclerView.setLayoutManager(mLayoutManager);
        }
        else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }

        loadRecipeData();

        return rootView;
    }

    public void loadRecipeData(){

            progressBar.setVisibility(View.VISIBLE);
            errorLoadingRecipesList.setVisibility(View.GONE);

            Api api = RetrofitBuilder.Retrieve();
            Call<ArrayList<Recipe>> recipe = api.getRecipes();

            recipe.enqueue(new Callback<ArrayList<Recipe>>() {
                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                    if(progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }

                    if (response.isSuccessful()) {

                        ArrayList<Recipe> recipes = response.body();

                        Bundle recipesBundle = new Bundle();
                        recipesBundle.putParcelableArrayList(ALL_RECIPES, recipes);

                        recipesAdapter.setRecipeData(recipes, getContext());

                    } else {
                        errorLoadingRecipesList.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                    errorLoadingRecipesList.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    Log.v("http fail: ", t.getMessage());
                }
            });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
