package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.f2prateek.dart.InjectExtra;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.WorkOrderCardActivity;
import com.unicorn.csp.xcdemo.component.OptionButton;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JSONArrayRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


// @P
public class SuspendActivity extends WorkOrderCardActivity {


    // ================================== extra ==================================

    @InjectExtra("workOrderInfo")
    WorkOrderInfo workOrderInfo;

    @InjectExtra("refreshEventTag")
    String refreshEventTag;


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
        initDescription();
    }


    // ========================== options ==========================

    @Bind(R.id.fl_options)
    FlowLayout flOptions;

    private void fetchOptions() {
        JsonArrayRequest jsonArrayRequest = new JSONArrayRequestWithSessionCheck(
                Request.Method.GET,
                ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/hangUp/options",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i != response.length(); i++) {
                            JSONObject jsonObject = JSONUtils.getJSONObject(response, i);
                            String name = JSONUtils.getString(jsonObject, "name", "");
                            String objectId = JSONUtils.getString(jsonObject, "objectId", "");
                            flOptions.addView(getOptionButton(name, objectId), getLayoutParams());
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }

    private OptionButton getOptionButton(String name, String objectId) {
        final OptionButton optionButton = new OptionButton(this);
        optionButton.name = name;
        optionButton.objectId = objectId;
        optionButton.setText(name);
        optionButton.setPadding(4, 4, 4, 4);
        optionButton.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        optionButton.setBootstrapSize(DefaultBootstrapSize.MD);
        optionButton.setRounded(true);
        optionButton.setShowOutline(true);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionButton.isShowOutline()) {
                    for (int i = 0; i != flOptions.getChildCount(); i++) {
                        View child = flOptions.getChildAt(i);
                        if (child != v) {
                            ((OptionButton) child).setShowOutline(true);
                        }
                    }
                }
                optionButton.setShowOutline(!optionButton.isShowOutline());
            }
        });
        return optionButton;
    }

    private ViewGroup.LayoutParams getLayoutParams() {
        // TODO: Is this a correct use of layoutParams ?
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    // ========================== options ==========================

    @Bind(R.id.et_description)
    BootstrapEditText etDescription;

    private void initDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


    // ========================== confrim ==========================

    @OnClick(R.id.btn_suspend)
    public void suspendConfirm() {
        DialogUtils.showConfirm(this, "确认挂单？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                suspend();
            }
        });
    }

    public void suspend() {
        final OptionButton optionSelected = getOptionSelected();
        if (optionSelected == null) {
            ToastUtils.show("请至少选择一个挂单选项");
            return;
        }
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderInfo.getWorkOrderId() + "/hangUp";
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show("挂单成功!");
                        EventBus.getDefault().post(new Object(), refreshEventTag);
                        SuspendActivity.this.finish();
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject code = new JSONObject();
                    code.put("name", optionSelected.name);
                    code.put("objectId", optionSelected.objectId);

                    JSONObject result = new JSONObject();
                    result.put("option", code);
                    result.put("remark", etDescription.getText().toString().trim());
                    String jsonString = result.toString();
                    return jsonString.getBytes("UTF-8");
                } catch (Exception e) {
                    //
                }
                return null;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Cookie", "JSESSIONID=" + ConfigUtils.getJsessionId());
                // 不加这个会出现 415 错误
                map.put("Content-Type", "application/json");
                return map;
            }
        };
        SimpleVolley.addRequest(stringRequest);
    }

    private OptionButton getOptionSelected() {
        OptionButton optionSelected = null;
        for (int i = 0; i != flOptions.getChildCount(); i++) {
            OptionButton optionButton = (OptionButton) flOptions.getChildAt(i);
            if (!optionButton.isShowOutline()) {
                optionSelected = optionButton;
            }
        }
        return optionSelected;
    }

}
