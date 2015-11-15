package com.unicorn.csp.xcdemo.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class JSONObjectRequestWithSessionCheck extends JsonObjectRequest {

    public JSONObjectRequestWithSessionCheck(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public JSONObjectRequestWithSessionCheck(int method, String url, String requestBody, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    public JSONObjectRequestWithSessionCheck(String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    public JSONObjectRequestWithSessionCheck(int method, String url, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    public JSONObjectRequestWithSessionCheck(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> map = new HashMap<>();
        map.put("Cookie", "JSESSIONID=" + ConfigUtils.getJsessionId());
        return map;
    }

}
