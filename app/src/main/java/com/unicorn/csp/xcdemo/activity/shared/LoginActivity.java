package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.activity.technician.MainActivity;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.EditTextUtils;
import com.unicorn.csp.xcdemo.utils.SfUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends ToolbarActivity {


    // ================================== views ==================================

    @BindView(R.id.et_account)
    MaterialEditText etAccount;

    @BindView(R.id.et_password)
    MaterialEditText etPassword;

    @BindView(R.id.cb_remember_me)
    CheckBox cbRememberMe;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar("登录", false);
        restoreUserInfo();
    }

    private void restoreUserInfo() {
        TinyDB tinyDB = TinyDB.getInstance();
        boolean rememberMe = tinyDB.getBoolean(SfUtils.SF_REMEMBER_ME);
        if (rememberMe) {
            etAccount.setText(tinyDB.getString(SfUtils.SF_ACCOUNT));
            etPassword.setText(tinyDB.getString(SfUtils.SF_PASSWORD));
            cbRememberMe.setChecked(true);
        }
    }


    // ================================== login ==================================

    @OnClick(R.id.btn_login)
    public void loginOnClick() {
        if (checkInput()) {
            login();
        }
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(etAccount.getText())) {
            ToastUtils.show("账号不能为空");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            ToastUtils.show("密码不能为空");
            return false;
        }
        return true;
    }

    private void login() {
        final MaterialDialog mask = DialogUtils.showMask(this, "登录中", "请稍后");
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ConfigUtils.getBaseUrl() + "/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mask.dismiss();
                        copeLoginResponse(response);
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
                map.put("username", EditTextUtils.getValue(etAccount));
                map.put("password", EditTextUtils.getValue(etPassword));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Login-From", "Android");
                return map;
            }
        };
        SimpleVolley.getRequestQueue().add(stringRequest);
    }

    private void copeLoginResponse(String responseString) {

        try {
            // 处理编码
            String original = new String(responseString.getBytes("ISO-8859-1"), "UTF-8");
            JSONObject response = new JSONObject(original);

            boolean success = response.getBoolean("success");

            if (!success) {
                ToastUtils.show("账号或密码错误!");
                return;
            }

            String jsessionid = response.getString("jsessionid");
            JSONObject currentUser = response.getJSONObject("currentUser");
            String role = currentUser.getString("role");
            TinyDB.getInstance().putString(ConfigUtils.JSESSION_ID, jsessionid);
            saveUserLoginInfo();

            if (role != null) {
                switch (role) {
                    case "Artificer":
                        startActivityAndFinish(MainActivity.class);
                        break;
                    case "Manager":
                        startActivityAndFinish(com.unicorn.csp.xcdemo.activity.chief.MainActivity.class);
                        break;
                    case "Supervisor":
                        startActivityAndFinish(com.unicorn.csp.xcdemo.activity.supervisor.MainActivity.class);
                        break;
                    case "SupervisorManager":
                        startActivityAndFinish(com.unicorn.csp.xcdemo.activity.supervisorManager.MainActivity.class);
                        break;
                    default:
                        ToastUtils.show("非法角色!");
                }
            }


        } catch (Exception e) {
            String i = "";
            //
        }
    }

    private void saveUserLoginInfo() {
        TinyDB.getInstance().putString(SfUtils.SF_ACCOUNT, EditTextUtils.getValue(etAccount));
        TinyDB.getInstance().putString(SfUtils.SF_PASSWORD, EditTextUtils.getValue(etPassword));
        TinyDB.getInstance().putBoolean(SfUtils.SF_REMEMBER_ME, cbRememberMe.isChecked());
    }


}
