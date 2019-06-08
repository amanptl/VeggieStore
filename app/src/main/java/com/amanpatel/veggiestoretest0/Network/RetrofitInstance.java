package com.amanpatel.veggiestoretest0.Network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    //private static final String BASE_URL = "https://battuta.medunes.net/api/country/all/?key=6a5dcc6d7dac0e06a48f7b4cb30e4274";
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";       //Put API URL

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

