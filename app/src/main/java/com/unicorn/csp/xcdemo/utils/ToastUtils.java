package com.unicorn.csp.xcdemo.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.unicorn.csp.xcdemo.SimpleApplication;


public class ToastUtils {

    private static Toast mToast = null;

    public static void show(String text) {

        if (mToast == null) {
            mToast = Toast.makeText(SimpleApplication.getInstance(), text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }
        else {
            mToast.setText(text);
        }
        mToast.show();
    }

}
