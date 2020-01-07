package com.app.nytimes.data.remote;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.app.nytimes.utils.AppConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppServiceHelper {

    private static OkHttpClient getClient(Context context) {

        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build();

        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        okHttpBuilder.connectionSpecs(Collections.singletonList(spec));
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpBuilder.interceptors().add(interceptor);

        okHttpBuilder.connectTimeout(10, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(10, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(10, TimeUnit.SECONDS);
        okHttpBuilder.retryOnConnectionFailure(true);
//        okHttpBuilder.interceptors().add(chain -> {
//            Request.Builder ongoing = chain.request().newBuilder();
//            //   ongoing.addHeader("Accept", "application/json; charset=utf-8");
//
//            ongoing.addHeader("Authorization", "bearer " + AppDataManager.getInstance(context).getUserAccessToken());
//            return chain.proceed(ongoing.build());
//        });
        okHttpBuilder.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request request = chain.request();
                Response response = doRequest(chain, request);
                int tryCount = 0;
                int RetryCount = 2;
                while (response == null && tryCount <= RetryCount) {
                    String url = request.url().toString();
                    Request newRequest = request.newBuilder().url(url).build();
                    tryCount++;
                    response = doRequest(chain, newRequest);
                }
                if (response == null) {//important ,should throw an exception here
                    throw new IOException();
                }
                return response;
            }

            private Response doRequest(Chain chain, Request request) {
                Response response = null;
                try {
                    response = chain.proceed(request);
                } catch (Exception e) {
                    Log.e("doRequest OkHttp3", Objects.requireNonNull(e.getMessage()));
                }
                return response;
            }
        });
        return okHttpBuilder.build();

    }

    public static AppServices getApiServices(Context context) {
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(AppConstants.API_BASE_URL)
                .client(getClient(context))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(AppServices.class);
    }

}
