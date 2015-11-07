package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.os.Handler;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.activity.technician.MainActivity;

import butterknife.Bind;
import butterknife.OnClick;


// @P
public class LoginActivity extends ToolbarActivity {


    // ================================== views ==================================

    @Bind(R.id.et_account)
    MaterialEditText etAccount;

    @Bind(R.id.et_password)
    MaterialEditText etPassword;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar("登录", false);
    }


    // ================================== OnClick ==================================

    @OnClick(R.id.btn_login)
    public void login() {

        copeLogin(showMask());
    }

    private MaterialDialog showMask() {

        return new MaterialDialog.Builder(this)
                .title("登录中")
                .content("请稍后...")
                .progress(true, 0)
                .cancelable(false)
                .show();
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
