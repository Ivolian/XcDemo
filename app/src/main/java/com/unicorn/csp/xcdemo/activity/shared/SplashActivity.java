package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ButterKnifeActivity;
import com.unicorn.csp.xcdemo.activity.chief.TodoActivity;
import com.unicorn.csp.xcdemo.activity.technician.WorkOrderActivity;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.SfUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import java.util.HashMap;
import java.util.Map;


// @OK
public class SplashActivity extends ButterKnifeActivity {


    // ================================== 全局变量 ==================================

    String role;


    // ================================== onCreate ==================================

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
                    public void onResponse(String response) {
                        startActivityAndFinish(role.equals("Artificer") ? WorkOrderActivity.class : TodoActivity.class);
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", TinyDB.getInstance().getString(SfUtils.SF_ACCOUNT));
                map.put("password",TinyDB.getInstance().getString(SfUtils.SF_PASSWORD));
                return map;
            }
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                role = response.headers.get("role");
                ConfigUtils.saveJSessionId(response);
                return super.parseNetworkResponse(response);
            }
        };
        SimpleVolley.addRequest(stringRequest);
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

}
