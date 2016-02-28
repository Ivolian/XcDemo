package com.unicorn.csp.xcdemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;


public class SimpleApplication extends Application {

    private static SimpleApplication instance;

    public static SimpleApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
        SimpleVolley.init(instance);
        Fresco.initialize(instance);
    }

}
