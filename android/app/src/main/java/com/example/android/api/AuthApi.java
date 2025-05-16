package com.example.android.api;

import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AuthApi {
    public static AuthApiInterface buildInstance() {
        return new Retrofit.Builder()
            .baseUrl("http://ec2-44-218-249-120.compute-1.amazonaws.com:8080/")
            // primero el converter de Strings
            .addConverterFactory(ScalarsConverterFactory.create())
            // luego el de JSON por si necesitas Gson más tarde
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiInterface.class);
    }
}
