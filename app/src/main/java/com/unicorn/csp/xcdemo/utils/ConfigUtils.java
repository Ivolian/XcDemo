package com.unicorn.csp.xcdemo.utils;

import android.os.Environment;

import com.unicorn.csp.xcdemo.component.TinyDB;

import java.io.File;


public class ConfigUtils {

//    final static private String IP = "192.168.7.68";
    final static private String IP = "101.200.178.52";
//    final static private String IP = "withub.net.cn";
//    final static private String IP = "192.168.7.100";


//    final static private String IP = "192.168.23.1";


    final static private String PORT = "80";

    public final static String JSESSION_ID = "jsessionid";

    public static String getJsessionId() {
        return TinyDB.getInstance().getString(ConfigUtils.JSESSION_ID);
    }

    public static String getBaseUrl() {
        return "http://" + IP + ":" + PORT + "/hems";
    }

    public static String getBaseDirPath() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "hems");
        if (!dir.exists()) {
            boolean result = dir.mkdir();
            if (!result) {
                ToastUtils.show("创建基础目录失败!");
            }
        }
        return dir.getAbsolutePath();
    }

}
