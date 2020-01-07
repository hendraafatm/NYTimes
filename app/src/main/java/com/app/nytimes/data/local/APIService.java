package com.app.nytimes.data.local;

import android.content.Context;

import com.app.nytimes.data.remote.AppServiceHelper;
import com.app.nytimes.data.remote.AppServices;

public class APIService {

    private static volatile APIService INSTANCE;
    private AppServices appServices;

    public static APIService getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (APIService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new APIService(context);
                }
            }
        }
        return INSTANCE;
    }

    private APIService(Context context) {
        appServices = AppServiceHelper.getApiServices(context);
    }

    public AppServices getAppServices() {
        return appServices;
    }
}
