package com.unicorn.csp.xcdemo.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;

import java.util.HashMap;
import java.util.Map;


public class StringRequestWithSessionCheck extends StringRequest {

    public StringRequestWithSessionCheck(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public StringRequestWithSessionCheck(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", "JSESSIONID=" + ConfigUtils.getJsessionId());
        return map;
    }

}
