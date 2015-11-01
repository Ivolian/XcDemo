package com.unicorn.csp.xcdemo.utils;

import android.app.Activity;
import android.content.Intent;

import com.unicorn.csp.xcdemo.R;


public class ActivityUtil {

    public static void startActivityWithAnim(Activity fromActivity, Class toActivityClass) {

        Intent intent = new Intent(fromActivity, toActivityClass);
        fromActivity.startActivity(intent);
        fromActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void finishActivityWithAnim(Activity finishActivity) {
        finishActivity.finish();
        finishActivity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
