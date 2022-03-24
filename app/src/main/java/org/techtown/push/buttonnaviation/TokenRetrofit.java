package org.techtown.push.buttonnaviation;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class TokenRetrofit {
    private String BASE_URL =
    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    InsertAPI insert = retrofitClient.create(InsertAPI.class);
}

interface InsertAPI{
    @POST("post")
    Call<Registered> insertOne(@Body Map<String, String> map);
}
