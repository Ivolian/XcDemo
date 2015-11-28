package com.unicorn.csp.xcdemo.activity.technician;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.google.gson.Gson;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.WorkOrderCardActivity;
import com.unicorn.csp.xcdemo.component.OptionButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.model.Material;
import com.unicorn.csp.xcdemo.model.WorkOrderSupply;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JSONArrayRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


public class PackActivity extends WorkOrderCardActivity {


    // ================================== views ==================================

    @Bind(R.id.ll_material_group_container)
    LinearLayout llMaterialGroupContainer;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        initToolbar("领料", true);
        initViews();
        enableSlideFinish();
    }

    private void initViews() {
        fetchMaterialGroup();
    }

    private void fetchMaterialGroup() {
        JsonArrayRequest jsonArrayRequest = new JSONArrayRequestWithSessionCheck(
                Request.Method.GET,
                ConfigUtils.getBaseUrl() + "/api/v1/hems/material/group",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        initMaterialGroup(response);
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }

    private void initMaterialGroup(JSONArray response) {
        for (int i = 0; i != response.length(); i++) {
            JSONObject jsonObject = JSONUtils.getJSONObject(response, i);
            String groupName = JSONUtils.getString(jsonObject, "name", "");
            TextView tvGroupName = new TextView(this);
            tvGroupName.setText(groupName);
            tvGroupName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tvGroupName.setTextColor(getResources().getColor(R.color.black));
            llMaterialGroupContainer.addView(tvGroupName, getGroupNameLayoutParams());

            FlowLayout flMaterialGroup = new FlowLayout(this);
            flMaterialGroup.setHorizontalSpacing(20);
            flMaterialGroup.setVerticalSpacing(20);
            JSONArray items = JSONUtils.getJSONArray(jsonObject, "items", null);
            if (items != null) {
                for (int j = 0; j != items.length(); j++) {
                    JSONObject item = JSONUtils.getJSONObject(items, j);
                    String name = JSONUtils.getString(item, "name", "");
                    String objectId = JSONUtils.getString(item, "objectId", "");
                    flMaterialGroup.addView(getOptionButton(name, objectId), getOptionButtonLayoutParams());
                }
            }
            llMaterialGroupContainer.addView(flMaterialGroup, getMaterialGroupLayoutParams());
        }
    }

    private ViewGroup.LayoutParams getOptionButtonLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private ViewGroup.LayoutParams getGroupNameLayoutParams() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 70, 0, 0);
        return layoutParams;
    }

    private ViewGroup.LayoutParams getMaterialGroupLayoutParams() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 50, 0, 0);
        return layoutParams;
    }

    private OptionButton getOptionButton(final String name, final String objectId) {
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
                    showNumberDialog(optionButton);
                } else {
                    optionButton.setText(name);
                    optionButton.setShowOutline(true);
                }
            }
        });
        return optionButton;
    }

    private void showNumberDialog(final OptionButton bootstrapButton) {
        final String btnText = bootstrapButton.getText().toString();
        String title = "输入" + btnText + "数量";
        new MaterialDialog.Builder(this)
                .title(title)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .alwaysCallInputCallback()
                .input("确认", "1", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        boolean isNumeric = org.apache.commons.lang3.StringUtils.isNumeric(input);
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(isNumeric);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        if (materialDialog.getInputEditText() != null) {
                            String number = materialDialog.getInputEditText().getText().toString();
                            bootstrapButton.setText(btnText + "(" + number + ")");
                            bootstrapButton.amount = Integer.parseInt(number);
                            bootstrapButton.setShowOutline(false);
                        }
                    }
                })
                .show();


    }


    // ================================== pack ==================================

    @OnClick(R.id.btn_pack)
    public void packConfirm() {
        DialogUtils.showConfirm(this, "确认领料？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                pack();
            }
        });

    }

    public void pack() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderInfo.getWorkOrderId() + "/supply";
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show("领料成功!");
                        // todo refresh
                        PackActivity.this.finish();
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                List<WorkOrderSupply> workOrderSupplyList = new ArrayList<>();
                for (int i = 0; i != llMaterialGroupContainer.getChildCount(); i++) {
                    View child = llMaterialGroupContainer.getChildAt(i);
                    if (child instanceof FlowLayout) {
                        FlowLayout flMaterialGroup = (FlowLayout) child;
                        for (int j = 0; j != flMaterialGroup.getChildCount(); j++) {
                            OptionButton optionButton = (OptionButton) flMaterialGroup.getChildAt(j);
                            if (!optionButton.isShowOutline()) {
                                WorkOrderSupply workOrderSupply = new WorkOrderSupply();
                                workOrderSupply.setAmount(optionButton.amount);
                                Material material = new Material();
                                material.setObjectId(optionButton.objectId);
                                material.setName(optionButton.name);
                                workOrderSupply.setMaterial(material);
                                workOrderSupplyList.add(workOrderSupply);
                            }
                        }
                    }
                }
                try {
                    String jsonString = new Gson().toJson(workOrderSupplyList);
                    return jsonString.getBytes("UTF-8");
                } catch (Exception e) {
                    //
                }
                return null;
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
        };
        SimpleVolley.addRequest(stringRequest);
    }

}
