package com.unicorn.csp.xcdemo.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;


public abstract class ButterKnifeActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResId) {

        super.setContentView(layoutResId);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        Dart.inject(this);
    }


    // ========================== 滑动移除 ==========================

    public void enableSlidr() {

//        SlidrConfig config = new SlidrConfig.Builder().edge(true).build();
//        Slidr.attach(this, config);
    }


    // ========================== 其他方法 ==========================

    public void startActivity(Class activityClass) {

        startActivity(new Intent(this, activityClass));
    }

    public void startActivityAndFinish(Class activityClass) {

        startActivity(activityClass);
        finish();
    }

}
