package com.unicorn.csp.xcdemo.utils;

import android.content.Context;

import com.unicorn.csp.xcdemo.SimpleApplication;


public class AppUtils {

    public static String getVersionName() {
        Context context = SimpleApplication.getInstance();
        String packageName = context.getPackageName();
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (Exception e) {
            //
        }
        return versionName;
    }

}
