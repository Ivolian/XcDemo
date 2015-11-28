package com.unicorn.csp.xcdemo.activity.technician;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.f2prateek.dart.InjectExtra;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.OptionButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.component.WorkOrderFrameLayout;
import com.unicorn.csp.xcdemo.model.Material;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderSupply;
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


public class PackActivity extends ToolbarActivity {


    // ================================== extra ==================================

    @InjectExtra("workOrderProcessInfo")
    WorkOrderProcessInfo workOrderProcessInfo;


    public static final String SHARED_VIEW = "PackActivity:WordOrderCard";
    // ================================== views ==================================

    @Bind(R.id.container)
    LinearLayout container;

    @Bind(R.id.work_order_card)
    WorkOrderFrameLayout workOrderCard;

    @OnClick(R.id.work_order_card)
    public void toggle(){
        workOrderCard.expandableLayout.toggle();
    }


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        initToolbar("领料", true);
        enableSlideFinish();
        fetchMaterialGroup();
        initViews();

        ViewCompat.setTransitionName(workOrderCard, SHARED_VIEW);

    }

    private void initViews() {
        WorkOrderInfo workOrderInfo = workOrderProcessInfo.getWorkOrderInfo();
        workOrderCard.setWorkOrderInfo(workOrderInfo);
    }

    private void fetchMaterialGroup() {

        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/material/group";
        SimpleVolley.getRequestQueue().add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                initMaterialGroup(response);
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

    private void initMaterialGroup(JSONArray response) {

        ViewGroup.LayoutParams layoutParamsForBtn = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams layoutParamsFlow = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParamsFlow.setMargins(0, 50, 0, 0);


        for (int i = 0; i != response.length(); i++) {
            JSONObject jsonObject = JSONUtils.getJSONObject(response, i);
            String groupName = JSONUtils.getString(jsonObject, "name", "");
            TextView tvGroupTag = new TextView(this);
            tvGroupTag.setText(groupName);
            tvGroupTag.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            tvGroupTag.setTextColor(getResources().getColor(R.color.black));
            container.addView(tvGroupTag, layoutParamsFlow);

            FlowLayout flowLayout = new FlowLayout(this);
            flowLayout.setHorizontalSpacing(20);
            flowLayout.setVerticalSpacing(20);

            JSONArray items = JSONUtils.getJSONArray(jsonObject, "items", null);
            if (items != null) {
                for (int j = 0; j != items.length(); j++) {
                    JSONObject item = JSONUtils.getJSONObject(items, j);
                    String text = JSONUtils.getString(item, "name", "");
                    OptionButton btn = getSuspendOptionButton(text);
                    btn.name = text;
                    String objectId = JSONUtils.getString(item, "objectId", "");
                    btn.objectId = objectId;
                    flowLayout.addView(btn, layoutParamsForBtn);
                }
            }
            container.addView(flowLayout, layoutParamsFlow);
        }


    }

    private OptionButton getSuspendOptionButton(String text) {

        final OptionButton btnSuspendOption = new OptionButton(this);
        btnSuspendOption.setText(text);
        btnSuspendOption.setPadding(4, 4, 4, 4);
        btnSuspendOption.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        btnSuspendOption.setBootstrapSize(DefaultBootstrapSize.MD);
        btnSuspendOption.setRounded(true);
        btnSuspendOption.setShowOutline(true);
        btnSuspendOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSuspendOption.isShowOutline()) {
                    showNumberDialog(btnSuspendOption);
                } else {
                    String btnText = btnSuspendOption.getText().toString();
                    int index = btnText.indexOf("(");
                    btnSuspendOption.setText(btnText.substring(0, index));
                    btnSuspendOption.setShowOutline(true);
                }
            }
        });
        return btnSuspendOption;
    }


    // ================================== finish ==================================

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


    @OnClick(R.id.btn_pack)
    public void packConfirm() {
        new MaterialDialog.Builder(this)
                .content("确认领料？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        pack();
                    }
                })
                .show();
    }


    public void pack() {


        WorkOrderProcessInfo workOrderProcessInfo2 = (WorkOrderProcessInfo) getIntent().getSerializableExtra("workOrderProcessInfo");
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfo2.getWorkOrderInfo().getWorkOrderId() + "/supply";
        SimpleVolley.getRequestQueue().add(
                new StringRequest(
                        Request.Method.PUT,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ToastUtils.show("领料成功!");
                                PackActivity.this.finish();
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

                        List<WorkOrderSupply> workOrderSupplyList = new ArrayList<>();

                        for (int i = 0; i != container.getChildCount(); i++) {
                            View child = container.getChildAt(i);
                            if (child instanceof FlowLayout) {
                                FlowLayout flowLayout = (FlowLayout) child;
                                for (int j = 0; j != flowLayout.getChildCount(); j++) {
                                    OptionButton optionButton = (OptionButton) flowLayout.getChildAt(j);
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


                        Gson gson = new Gson();
                        String json = gson.toJson(workOrderSupplyList, new TypeToken<List<WorkOrderProcessInfo>>() {
                        }.getType());
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
