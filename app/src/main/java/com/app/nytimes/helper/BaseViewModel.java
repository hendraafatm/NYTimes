package com.app.nytimes.helper;

import android.app.Application;
import android.content.Context;

import com.app.nytimes.R;
import com.app.nytimes.utils.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import okhttp3.ResponseBody;
import retrofit2.Response;

public abstract class BaseViewModel extends AndroidViewModel {

    private static final String TAG = BaseViewModel.class.getName();
    private final WeakReference<Context> mContext;
    protected SingleLiveEvent<Boolean> loadingRequest;
    protected SingleLiveEvent<String> errorMessage;
    protected SingleLiveEvent<Boolean> userExpired;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mContext = new WeakReference<>(application.getApplicationContext());
        loadingRequest = new SingleLiveEvent<>();
        errorMessage = new SingleLiveEvent<>();
        userExpired = new SingleLiveEvent<>();
    }

    protected void onResponse(Response<ResponseBody> responseBody, TypeToken typeToken, Throwable throwable) {
        try {
            Gson gson = new Gson();
            if (responseBody != null && responseBody.body() != null) {
                JSONObject responseJsonObject = new JSONObject(responseBody.body().string());
                if (responseBody.code() == 200) {
                    if (responseJsonObject.has("code") &&
                            responseJsonObject.getInt("code") != 200) {
                        ErrorResponse errorResponse = gson.fromJson(responseJsonObject.toString(), ErrorResponse.class);
                        onErrorResponse(errorResponse.getMessage());
                    } else {
                        AppResponse appResponse = gson.fromJson(responseJsonObject.toString(), typeToken.getType());
                        onSuccessResponse(appResponse);
                    }
                } else {
                    onErrorResponse(mContext.get().getString(R.string.something_went_wrong));
                }
            } else if (throwable != null && throwable.getMessage() != null) {
                onErrorResponse(throwable.getMessage());
            } else {
                if (!NetworkUtils.isNetworkConnected(mContext.get())) {
                    onErrorResponse(mContext.get().getString(R.string.no_internet_connection));
                } else {
                    onErrorResponse(mContext.get().getString(R.string.something_went_wrong));
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            if (!NetworkUtils.isNetworkConnected(mContext.get())) {
                onErrorResponse(mContext.get().getString(R.string.no_internet_connection));
            } else {
                onErrorResponse(mContext.get().getString(R.string.something_went_wrong));
            }
        }
    }


    public LiveData<Boolean> getLoadingRequest() {
        return loadingRequest;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public SingleLiveEvent<Boolean> getUserExpired() {
        return userExpired;
    }

    protected abstract void onSuccessResponse(AppResponse appResponse);

    protected abstract void onErrorResponse(String message);


}
