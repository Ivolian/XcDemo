package com.unicorn.csp.xcdemo.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.SfUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class VolleyErrorHelper {

    public static String getErrorMessage(VolleyError volleyError) {

        if (volleyError instanceof AuthFailureError) {
            reLogin();
            return "登录超时,自动重新登录中";
        }
        if (volleyError instanceof NoConnectionError) {
            return "手机未连接到网络";
        } else if (volleyError instanceof ServerError) {
            return "服务器内部错误，错误码:" + volleyError.networkResponse.statusCode;
        } else if (volleyError instanceof TimeoutError) {
            return "连接超时，请稍后再试";
        } else {
            return "未知错误";
        }
    }

    //
    public static void reLogin() {
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
                            ToastUtils.show("登录成功");
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        )
        {
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

    // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
    // For AuthFailure, you can re login with user credentials.
    // For ClientError, 400 & 401, Errors happening on client side when sending api request.
    // In this case you can check how client is forming the api and debug accordingly.
    // For ServerError 5xx, you can do retry or handle accordingly.
//    if( error instanceof NetworkError) {
//    } else if( error instanceof ClientError) {
//    } else if( error instanceof ServerError) {
//    } else if( error instanceof AuthFailureError) {
//    } else if( error instanceof ParseError) {
//    } else if( error instanceof NoConnectionError) {
//    } else if( error instanceof TimeoutError) {
//    }

}