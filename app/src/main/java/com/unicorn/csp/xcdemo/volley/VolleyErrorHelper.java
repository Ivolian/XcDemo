package com.unicorn.csp.xcdemo.volley;

import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;


public class VolleyErrorHelper {

    public static String getErrorMessage(VolleyError volleyError) {

        if (volleyError instanceof NoConnectionError) {
            return "手机未连接到网络";
        } else if (volleyError instanceof ServerError) {
            return "服务器内部错误，错误码:" + volleyError.networkResponse.statusCode;
        } else if (volleyError instanceof ParseError) {
            return "解析错误";
        } else if (volleyError instanceof TimeoutError) {
            return "连接超时，请稍后再试";
        } else {
            return "未知错误";
        }
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