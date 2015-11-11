package com.unicorn.csp.xcdemo.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.unicorn.csp.xcdemo.utils.ToastUtils;


public class SimpleVolley {

    private static RequestQueue mRequestQueue;

    private SimpleVolley() {
        // no instances
    }

    static public void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static void addRequest(Request request){

        getRequestQueue().add(request);
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static Response.ErrorListener getDefaultErrorListener() {

        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
            }
        };
    }

}