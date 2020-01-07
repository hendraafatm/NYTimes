package com.app.nytimes.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class CommonUtils {
    private static final CommonUtils ourInstance = new CommonUtils();

    public static CommonUtils getInstance() {
        return ourInstance;
    }

    private CommonUtils() {
    }

    public static void showSnackBar(View view, String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }
}
