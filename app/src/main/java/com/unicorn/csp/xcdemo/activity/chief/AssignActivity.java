package com.unicorn.csp.xcdemo.activity.chief;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.f2prateek.dart.InjectExtra;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.WorkOrderCardActivity;
import com.unicorn.csp.xcdemo.component.OptionButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
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
public class AssignActivity extends WorkOrderCardActivity {


    // ================================== extra ==================================

    @InjectExtra("workOrderInfo")
    WorkOrderInfo workOrderInfo;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);
        initToolbar("派单", true);
        initViews();
        enableSlideFinish();
    }

    private void initViews() {
        fetchTechnicians();
    }


    // ================================== fetch technicians ==================================

    @Bind(R.id.fl_technician_group)
    FlowLayout flTechnicianGroup;

    private void fetchTechnicians() {
        JsonArrayRequest jsonArrayRequest = new JSONArrayRequestWithSessionCheck(
                Request.Method.GET,
                ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/artificer",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        for (int i = 0; i != response.length(); i++) {
                            JSONObject jsonObject = JSONUtils.getJSONObject(response, i);
                            String name = JSONUtils.getString(jsonObject, "name", "");
                            String objectId = JSONUtils.getString(jsonObject, "objectId", "");
                            flTechnicianGroup.addView(getOptionButton(name, objectId), getOptionButtonLayoutParams());
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
                    for (int i = 0; i != flTechnicianGroup.getChildCount(); i++) {
                        View child = flTechnicianGroup.getChildAt(i);
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

    private ViewGroup.LayoutParams getOptionButtonLayoutParams() {
        // TODO: Is this a correct use of layoutParams ?
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    // ================================== confirm ==================================

    @OnClick(R.id.btn_assign)
    public void assignConfirm() {
        DialogUtils.showConfirm(this, "确认派单？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                assign();
            }
        });
    }

    private void assign() {
        final OptionButton optionSelected = getOptionSelected();
        if (optionSelected == null) {
            ToastUtils.show("请至少选择一个技师");
            return;
        }
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderInfo.getWorkOrderId() + "/distribute";
        url += ("?userId=" + optionSelected.objectId);
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show("派单成功!");
                        EventBus.getDefault().post(new Object(), "workOrderAssignedFragment_refresh");
                        AssignActivity.this.finish();
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                String jsessionid = TinyDB.getInstance().getString(ConfigUtils.JSESSION_ID);
                map.put("Cookie", "JSESSIONID=" + jsessionid);
                // 不加这个会出现 415 错误
                map.put("Content-Type", "application/json");
                return map;
            }
        };
        SimpleVolley.addRequest(stringRequest);
    }

    private OptionButton getOptionSelected() {
        OptionButton optionSelected = null;
        for (int i = 0; i != flTechnicianGroup.getChildCount(); i++) {
            OptionButton optionButton = (OptionButton) flTechnicianGroup.getChildAt(i);
            if (!optionButton.isShowOutline()) {
                optionSelected = optionButton;
            }
        }
        return optionSelected;
    }

}
