package com.app.nytimes.data.remote;


import com.app.nytimes.data.remote.response.NYArticlesResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppServices {

    @GET("all-sections/7.json?")
    Call<ResponseBody> getPopularArticles(
            @Query("api-key") String apiKey
    );

}
