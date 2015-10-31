package com.unicorn.csp.xcdemo.activity;

import android.os.Bundle;
import android.os.Handler;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.utils.DialogUtils;

import butterknife.Bind;
import butterknife.OnClick;


public class LoginActivity extends ToolbarActivity {

    @Bind(R.id.et_account)
    MaterialEditText etAccount;

    @Bind(R.id.et_password)
    MaterialEditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar("登录", false);
    }

    @OnClick(R.id.btn_login)
    public void login() {

        copeLogin(showMask());
    }

    private MaterialDialog showMask() {

        return DialogUtils.showIndeterminateDialog(this, "登录中", "请稍后...");
    }

    private void copeLogin(final MaterialDialog mask) {

        int delay = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mask.dismiss();
                startActivityAndFinish(MainActivity.class);
            }
        }, delay);
    }

}
