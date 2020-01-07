package com.app.nytimes.data.local;

import android.content.Context;

import com.app.nytimes.data.remote.AppServices;


public class AppDataManager {

    private final AppServices appServices;
    private volatile static AppDataManager INSTANCE = null;

    private AppDataManager(Context context) {
        this.appServices = APIService.getInstance(context).getAppServices();
    }

    public static AppDataManager getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDataManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppDataManager(context);
                }
            }
        }
        return INSTANCE;
    }

    public AppServices getAppServices() {
        return appServices;
    }
}
