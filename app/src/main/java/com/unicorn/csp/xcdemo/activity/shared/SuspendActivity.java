package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.MyButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


// @P
public class SuspendActivity extends ToolbarActivity {


    // ================================== views ==================================

    @Bind(R.id.et_suspend_description)
    BootstrapEditText etSuspendDescription;

    @Bind(R.id.fl_suspend_options)
    FlowLayout flSuspendOptions;

    List<MyButton> buttonList = new ArrayList<>();

    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspend);
        initToolbar("挂单", true);
        initViews();
        enableSlideFinish();
    }

    private void initViews() {

        fetchOptions();
//        initSuspendOptions();
        initSuspendDescription();
    }

    private void fetchOptions() {

        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/hangUp/options";
        SimpleVolley.getRequestQueue().add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                for (int i = 0; i != response.length(); i++) {
                                    JSONObject jsonObject = JSONUtils.getJSONObject(response, i);
                                    String objectId = JSONUtils.getString(jsonObject, "objectId", "");
                                    String name = JSONUtils.getString(jsonObject, "name", "");
                                    MyButton myButton = getSuspendOptionButton(name);
                                    myButton.name = name;
                                    myButton.objectId = objectId;
                                    flSuspendOptions.addView(myButton, layoutParams);
                                    buttonList.add(myButton);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        String jsessionid = TinyDB.getInstance().getString(ConfigUtils.JSESSION_ID);
                        map.put("Cookie", "JSESSIONID=" + jsessionid);
                        return map;
                    }
                }
        );

    }


    private void initSuspendDescription() {

        etSuspendDescription.setGravity(Gravity.TOP);
        etSuspendDescription.setPadding(20, 20, 20, 20);
        etSuspendDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }

    private MyButton getSuspendOptionButton(String suspendOptionText) {

        final MyButton btnSuspendOption = new MyButton(this);
        btnSuspendOption.setText(suspendOptionText);
        btnSuspendOption.setPadding(4, 4, 4, 4);
        btnSuspendOption.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        btnSuspendOption.setBootstrapSize(DefaultBootstrapSize.MD);
        btnSuspendOption.setRounded(true);
        btnSuspendOption.setShowOutline(true);
        btnSuspendOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSuspendOption.isShowOutline()) {
                    for (MyButton myButton : buttonList) {
                        if (myButton != v) {
                            myButton.setShowOutline(true);
                        }
                    }
                }

                btnSuspendOption.setShowOutline(!btnSuspendOption.isShowOutline());
            }
        });
        return btnSuspendOption;
    }


    // ================================== finish ==================================

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @OnClick(R.id.btn_suspend)
    public void suspend() {

        MyButton btnSelected = null;
        for (MyButton myButton : buttonList) {
            if (!myButton.isShowOutline()) {
                btnSelected = myButton;
            }
        }
        if (btnSelected==null){
            ToastUtils.show("请至少选择一个挂单选项");
            return;
        }

        WorkOrderProcessInfo workOrderProcessInfo = (WorkOrderProcessInfo) getIntent().getSerializableExtra("workOrderProcessInfo");
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId() + "/hangUp";
        SimpleVolley.getRequestQueue().add(
                new StringRequest(
                        Request.Method.PUT,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ToastUtils.show("挂单成功!");
//                                SuspendActivity.this.finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
                            }
                        }
                ) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {

                        JSONObject result = new JSONObject();
                        MyButton btnSelected = null;
                        for (MyButton myButton : buttonList) {
                            if (!myButton.isShowOutline()) {
                                btnSelected = myButton;
                            }
                        }
                        String codeName = btnSelected.name;
                        String codeId = btnSelected.objectId;
                        JSONObject code = new JSONObject();
                        try {
                            code.put("name", codeName);
                            code.put("objectId", codeId);
                            result.put("option", code);
                            result.put("remark", etSuspendDescription.getText().toString().trim());
                        } catch (Exception e) {
                            //
                        }


                        String json = result.toString();
                        byte[] bytes = null;
                        try {
                            bytes = json.getBytes("UTF-8");
                        } catch (Exception e) {
                            //
                        }
                        return bytes;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        String jsessionid = TinyDB.getInstance().getString(ConfigUtils.JSESSION_ID);
                        map.put("Cookie", "JSESSIONID=" + jsessionid);
                        // 不加这个会出现 415 错误
                        map.put("Content-Type", "application/json");
                        return map;
                    }
                }
        );


    }

}
