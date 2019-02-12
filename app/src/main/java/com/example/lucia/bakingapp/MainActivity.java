package com.example.lucia.bakingapp;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lucia.bakingapp.UI.RecipesFragment;
import com.example.lucia.bakingapp.adapters.RecipeAdapter;
import com.example.lucia.bakingapp.data.ConnectivityReceiver;
import com.example.lucia.bakingapp.data.Recipe;

import java.util.ArrayList;

import static com.example.lucia.bakingapp.utils.Constants.SELECTED_RECIPE;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener, ConnectivityReceiver.ConnectivityReceiverListener{

    private RecipesFragment recipesFragment;
    ConnectivityReceiver x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recipesFragment = (RecipesFragment) getSupportFragmentManager().findFragmentById(R.id.activity_main_recipe_fragment);

    }

    @Override
    public void onListItemClick(Recipe selectedItemIndex) {

        Bundle bundle = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(selectedItemIndex);
        bundle.putParcelableArrayList(SELECTED_RECIPE,selectedRecipe);

        final Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            recipesFragment.loadRecipeData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        x = new ConnectivityReceiver();
        IntentFilter connFilter = new IntentFilter();
        connFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        connFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        this.registerReceiver(x, connFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(x);
    }
}
