package org.techtown.push.buttonnaviation.candidate.video;

import android.provider.MediaStore;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class VideoDataRetrofit {
    private String BASE_URL =
    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    SelectAPI select = retrofitClient.create(SelectAPI.class);
}

interface SelectAPI{
    @GET("all/{candidate}")
    Call<List<VideoData>> getAllByCandidate(@Path("candidate") String candidate);
}
