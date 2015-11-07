package com.unicorn.csp.xcdemo;

import android.app.Application;

import com.unicorn.csp.xcdemo.fragment.technician.AlreadyReceiveFragment;
import com.unicorn.csp.xcdemo.fragment.YJDDFragment;
import com.unicorn.csp.xcdemo.fragment.YJDFragment;


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

    public static YJDFragment yjdFragment ;
    public static AlreadyReceiveFragment alreadyReceiveFragment;
    public static YJDDFragment yjddFragment;

}
