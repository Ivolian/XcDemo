package com.unicorn.csp.xcdemo;

import android.app.Application;


public class SimpleApplication extends Application {

    private static SimpleApplication instance;

    public static SimpleApplication getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
    }

}
