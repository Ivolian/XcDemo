package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.os.Handler;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ButterKnifeActivity;


// @P
public class SplashActivity extends ButterKnifeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        delayToLogin();
    }

    private void delayToLogin() {

        int delay = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startLoginActivity();
            }
        }, delay);
    }

    private void startLoginActivity() {

        startActivityAndFinish(LoginActivity.class);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
