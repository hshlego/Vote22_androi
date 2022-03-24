package org.techtown.push.buttonnaviation.ui.home;

import java.util.List;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class SurveyResultRetrofit {
    private String BASE_URL =
    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    SelectAPI select = retrofitClient.create(SelectAPI.class);
}

interface SelectAPI{
    @GET("all")
    Call<List<SurveyResult>> getAllSurveyResult();
}