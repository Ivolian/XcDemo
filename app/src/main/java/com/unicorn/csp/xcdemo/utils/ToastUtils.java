package com.unicorn.csp.xcdemo.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.unicorn.csp.xcdemo.MyApplication;

public class ToastUtils {

    private static Toast mToast = null;

    public static void show(String text) {

        if (mToast == null) {
            mToast = Toast.makeText(MyApplication.getInstance(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

}
