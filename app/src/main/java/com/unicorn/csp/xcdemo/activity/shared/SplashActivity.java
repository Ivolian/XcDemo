package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ButterKnifeActivity;
import com.unicorn.csp.xcdemo.activity.technician.MainActivity;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.SfUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;


public class SplashActivity extends ButterKnifeActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean rememberMe = TinyDB.getInstance().getBoolean(SfUtils.SF_REMEMBER_ME);
        if (rememberMe) {
            login();
        } else {
            delayToLoginActivity();
        }
    }

    private void login() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                ConfigUtils.getBaseUrl() + "/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        try {
                            // 处理编码
                            String original = new String(responseString.getBytes("ISO-8859-1"), "UTF-8");
                            JSONObject response = new JSONObject(original);
                            String jsessionid = response.getString("jsessionid");
                            TinyDB.getInstance().putString(ConfigUtils.JSESSION_ID, jsessionid);

                            JSONObject currentUser = response.getJSONObject("currentUser");
                            String role = currentUser.getString("role");
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
                            //
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
                        startActivityAndFinish(LoginActivity.class);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", TinyDB.getInstance().getString(SfUtils.SF_ACCOUNT));
                map.put("password", TinyDB.getInstance().getString(SfUtils.SF_PASSWORD));
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

    private void delayToLoginActivity() {
        int delay = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityAndFinish(LoginActivity.class);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, delay);
    }

    //

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();

    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }

}

