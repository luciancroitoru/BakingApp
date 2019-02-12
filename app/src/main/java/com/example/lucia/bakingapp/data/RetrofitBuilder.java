package com.example.lucia.bakingapp.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    static Api api;

    public static Api Retrieve(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //using the GsonConverterFactory to directly convert json data to object
                .build();

        api = retrofit.create(Api.class);

        return api;
    }
}
