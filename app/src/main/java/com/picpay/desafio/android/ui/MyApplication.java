package com.picpay.desafio.android.ui;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication instance;

    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public static Context getContext() {
        return instance;
    }
}
