package com.example.android.api;

import com.example.android.domain.Director;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DirectorApiInterface {
    @GET("directors")
    Call<List<Director>> getAllDirectors();
}
