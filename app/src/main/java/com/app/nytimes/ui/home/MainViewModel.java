package com.app.nytimes.ui.home;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.nytimes.R;
import com.app.nytimes.data.local.AppDataManager;
import com.app.nytimes.data.remote.response.NYArticlesResponse;
import com.app.nytimes.helper.AppResponse;
import com.app.nytimes.helper.BaseViewModel;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends BaseViewModel {

    private final WeakReference<Context> mContext;
    AppDataManager appDataManager;

    MutableLiveData<NYArticlesResponse> articlesResponseMutableLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mContext = new WeakReference<>(application.getApplicationContext());
        appDataManager = AppDataManager.getInstance(mContext.get());

        articlesResponseMutableLiveData = new MutableLiveData<>();
    }


    public void getNYPopularArticles() {
        final TypeToken<NYArticlesResponse> responseType = new TypeToken<NYArticlesResponse>() {
        };
        loadingRequest.setValue(true);
        appDataManager.getAppServices().getPopularArticles(mContext.get().getString(R.string.api_key))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        MainViewModel.this.onResponse(response, responseType, null);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                        MainViewModel.this.onResponse(null, responseType, null);
                    }
                });

    }

    @Override
    protected void onSuccessResponse(AppResponse appResponse) {
        loadingRequest.setValue(false);
        if (appResponse instanceof NYArticlesResponse) {
            articlesResponseMutableLiveData.setValue((NYArticlesResponse) appResponse);
        }
    }

    @Override
    protected void onErrorResponse(String message) {
        loadingRequest.setValue(false);
        errorMessage.setValue(message);
    }

    public MutableLiveData<NYArticlesResponse> getArticlesResponseMutableLiveData() {
        return articlesResponseMutableLiveData;
    }
}
