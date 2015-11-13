package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;

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
import com.unicorn.csp.xcdemo.activity.technician.WorkOrderActivity;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


// @P
public class LoginActivity extends ToolbarActivity {


    public final static String JSESSION_ID = "jsessionid";


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

        etAccount.setText("js1");
        etPassword.setText("123456");
    }

    String role = "";

    String shiroLoginFailure = null;

    // ================================== OnClick ==================================

    @OnClick(R.id.btn_login)
    public void onLoginBtnClick() {
        if (isUserInputValid()) {
            login(showMask());
        }
    }

    private void login(final MaterialDialog mask) {
        SimpleVolley.getRequestQueue().add(
                new StringRequest(
                        Request.Method.POST,
                        ConfigUtils.getBaseUrl() + "/login",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                mask.dismiss();
                                if (shiroLoginFailure!=null){
                                    ToastUtils.show("登录失败！");
                                    return;
                                }
                                startActivityAndFinish(role.equals("Artificer") ? WorkOrderActivity.class : TodoActivity.class);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                mask.dismiss();
//                                startActivityAndFinish(WorkOrderActivity.class);
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

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        shiroLoginFailure = response.headers.get("shiroLoginFailure");
                        if (shiroLoginFailure != null){
                            return super.parseNetworkResponse(response);
                        }

                        TinyDB.getInstance().putString(JSESSION_ID, response.headers.get(JSESSION_ID));
                        role = response.headers.get("role");

                        return super.parseNetworkResponse(response);
                    }
                }
        );


    }

    private boolean isUserInputValid() {
        if (etAccount.getText().toString().equals("")) {
            ToastUtils.show("账号不能为空");
            return false;
        }
        if (etAccount.getText().toString().equals("")) {
            ToastUtils.show("密码不能为空");
            return false;
        }
        return true;
    }

    private MaterialDialog showMask() {
        return new MaterialDialog.Builder(this)
                .title("登录中")
                .content("请稍后...")
                .progress(true, 0)
                .cancelable(false)
                .show();
    }


}
