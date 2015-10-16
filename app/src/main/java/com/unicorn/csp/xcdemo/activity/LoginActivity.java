package com.unicorn.csp.xcdemo.activity;

import android.os.Bundle;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import butterknife.OnClick;


public class LoginActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar("登录",false);
    }

    @OnClick(R.id.btn_login)
    public void login(){


    }

}
