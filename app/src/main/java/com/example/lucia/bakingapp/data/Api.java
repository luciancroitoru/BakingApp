package com.example.lucia.bakingapp.data;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {

    String BASE_URL = "http://go.udacity.com/";


        @GET("android-baking-app-json")
        Call<ArrayList<Recipe>> getRecipes();
}
