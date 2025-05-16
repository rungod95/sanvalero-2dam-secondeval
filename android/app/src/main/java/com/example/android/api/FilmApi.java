package com.example.android.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilmApi {

    private static final String BASE_URL = "http://ec2-44-218-249-120.compute-1.amazonaws.com:8080/";

    // Para llamadas públicas (si aún las tienes)
    public static FilmApiInterface buildInstance() {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(buildGson()))
            .build()
            .create(FilmApiInterface.class);
    }

    // Para llamadas autenticadas (PUT, DELETE, etc.)
    public static FilmApiInterface buildInstance(String authToken) {
        // Interceptor que añade el header Authorization
        Interceptor authInterceptor = chain -> {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                .header("Authorization", "Bearer " + authToken);
            return chain.proceed(builder.build());
        };

        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build();

        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(buildGson()))
            .build()
            .create(FilmApiInterface.class);
    }

    // Gson con soporte de LocalDate
    private static Gson buildGson() {
        return new GsonBuilder()
            .registerTypeAdapter(LocalDate.class,
                (com.google.gson.JsonSerializer<LocalDate>)
                    (src, typeOfSrc, context) -> new com.google.gson.JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalDate.class,
                (com.google.gson.JsonDeserializer<LocalDate>)
                    (json, typeOfT, context) -> LocalDate.parse(json.getAsString()))
            .setDateFormat("yyyy-MM-dd")
            .create();
    }
}
