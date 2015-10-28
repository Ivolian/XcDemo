package com.unicorn.csp.xcdemo;

import android.app.Application;

import com.unicorn.csp.xcdemo.fragment.YGQFragment;
import com.unicorn.csp.xcdemo.fragment.YJDDFragment;
import com.unicorn.csp.xcdemo.fragment.YJDFragment;


public class MyApplication extends Application {

    private static MyApplication instance;

    public static MyApplication getInstance() {

        return instance;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        instance = this;
    }

    public static YJDFragment yjdFragment ;
    public static YGQFragment ygqFragment;
    public static YJDDFragment yjddFragment;

}
