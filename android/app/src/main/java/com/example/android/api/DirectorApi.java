package com.example.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectorApi {
    public static DirectorApiInterface buildInstance() {
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> {
                return new JsonPrimitive(src.toString());
            })
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) -> {
                return LocalDate.parse(json.getAsString());
            })
            .setDateFormat("yyyy-MM-dd")
            .create();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-44-218-249-120.compute-1.amazonaws.com:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

        return retrofit.create(DirectorApiInterface.class);
    }
}
