package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.widget.CheckBox;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.activity.chief.TodoActivity;
import com.unicorn.csp.xcdemo.activity.technician.TMainActivity;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.SfUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


// @PP
public class LoginActivity extends ToolbarActivity {


    // ================================== 全局变量 ==================================

    String role;

    String shiroLoginFailure;


    // ================================== views ==================================

    @Bind(R.id.et_account)
    MaterialEditText etAccount;

    @Bind(R.id.et_password)
    MaterialEditText etPassword;

    @Bind(R.id.cb_remember_me)
    CheckBox cbRememberMe;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar("登录", false);
        initViews();
    }

    private void initViews() {
        String account = "js1";
        etAccount.setText(account);
        String password = "123456";
        etPassword.setText(password);
    }


    // ================================== OnClick ==================================

    @OnClick(R.id.btn_login)
    public void onLoginButtonClick() {
        if (isUserInputValid()) {
            MaterialDialog mask = DialogUtils.showMask(this, "登录中", "请稍后");
            login(mask);
        }
    }

    private boolean isUserInputValid() {
        if (etAccount.getText().toString().equals("")) {
            ToastUtils.show("账号不能为空");
            return false;
        }
        if (etPassword.getText().toString().equals("")) {
            ToastUtils.show("密码不能为空");
            return false;
        }
        return true;
    }

    private void login(final MaterialDialog mask) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ConfigUtils.getBaseUrl() + "/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mask.dismiss();
                        if (shiroLoginFailure != null) {
                            ToastUtils.show("账号或密码错误!");
                            return;
                        }
                        startActivityAndFinish(role.equals("Artificer") ? TMainActivity.class : TodoActivity.class);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mask.dismiss();
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", etAccount.getText().toString().trim());
                map.put("password", etPassword.getText().toString().trim());
                return map;
            }

            // 从返回的头部中获取一些信息
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                shiroLoginFailure = response.headers.get("shiroLoginFailure");
                // shiroLoginFailure != null 表示登录失败
                if (shiroLoginFailure != null) {
                    return super.parseNetworkResponse(response);
                }
                // 如果登录成功，获取角色，保存 JSessionId
                role = response.headers.get("role");
                ConfigUtils.saveJSessionId(response);
                saveUserInput();
                return super.parseNetworkResponse(response);
            }
        };
        SimpleVolley.getRequestQueue().add(stringRequest);
    }

    private void saveUserInput() {
        TinyDB.getInstance().putString(SfUtils.SF_ACCOUNT, etAccount.getText().toString().trim());
        TinyDB.getInstance().putString(SfUtils.SF_PASSWORD, etPassword.getText().toString().trim());
        TinyDB.getInstance().putBoolean(SfUtils.SF_REMEMBER_ME, cbRememberMe.isChecked());
    }

}
