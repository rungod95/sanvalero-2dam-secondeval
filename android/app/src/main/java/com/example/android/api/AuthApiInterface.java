package com.example.android.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthApiInterface {

    @FormUrlEncoded
    @POST("api/auth/login")
    Call<String> login(
        @Field("username") String username,
        @Field("password") String password
    );
}
