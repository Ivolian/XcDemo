package com.unicorn.csp.xcdemo.login;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.unicorn.csp.xcdemo.SimpleApplication;
import com.unicorn.csp.xcdemo.login.model.CurrentUser;
import com.unicorn.csp.xcdemo.login.model.WorkType;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JsonObjectRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class UserInfoModuler {

    public void fetchUserInfo() {
        String url = ConfigUtils.getBaseUrl() + "/hems/getApplicationInfo";
        final JsonObjectRequest request = new JsonObjectRequestWithSessionCheck(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String currentUserString = JSONUtils.getString(response, "currentUser", "");
                        CurrentUser currentUser = new Gson().fromJson(currentUserString, CurrentUser.class);


                        Set<String> tags = new HashSet<>();
                        tags.add(currentUser.getObjectId().replace("-", "_"));

                        if (currentUser.getRole().getTag().equals("Artificer")) {
                            for (WorkType workType : currentUser.getWorkTypeList()) {
                                tags.add(workType.getWorkTypeId().replace("-", "_"));
                            }
                        }

                        JPushInterface.setTags(SimpleApplication.getInstance(), tags, new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
//                                ToastUtils.show("");
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
                    }
                });
        SimpleVolley.addRequest(request);
    }

}
