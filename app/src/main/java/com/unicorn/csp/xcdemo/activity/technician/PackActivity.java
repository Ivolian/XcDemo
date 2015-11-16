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
import com.unicorn.csp.xcdemo.component.MyButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.model.Material;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderSupply;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


//
public class PackActivity extends ToolbarActivity {

    @InjectExtra("workOrderProcessInfo")
    WorkOrderProcessInfo workOrderProcessInfo;


    // ================================== views ==================================

    @Bind(R.id.container)
    LinearLayout container;

    @Bind(R.id.labelview)
    LabelView labelView;

    @Bind(R.id.tv_request_user_and_call_number)
    TextView tvRequestUserAndCallNumber;

    @Bind(R.id.tv_request_time)
    TextView tvRequestTime;

    @Bind(R.id.tv_building_and_address)
    TextView tvBuildingAndAddress;

    @Bind(R.id.tv_type)
    TextView tvType;

    @Bind(R.id.tv_equipment_and_fault_type)
    TextView tvEquipmentAndFaultType;

    @Bind(R.id.tv_processing_time_limit)
    TextView tvProcessingTimeLimit;

    @Bind(R.id.tv_issuer)
    TextView tvIssuer;

    @Bind(R.id.tv_distribute_time)
    TextView tvDistributeTime;

    @Bind(R.id.tv_distributor)
    TextView tvDistributor;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        initToolbar("领料", true);
        enableSlideFinish();
        fetchMaterialGroup();
        initViews();
    }

    private void initViews(){
        WorkOrderInfo workOrderInfo = workOrderProcessInfo.getWorkOrderInfo();
        String requestUserAndCallNumber = "报修电话: " + workOrderInfo.getCallNumber() + " " + workOrderInfo.getRequestUser();
        tvRequestUserAndCallNumber.setText(requestUserAndCallNumber);
        String requestTime = "报修时间: " + new DateTime(workOrderInfo.getRequestTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvRequestTime.setText(requestTime);
        String buildingAndAddress = "保修地点: " + workOrderInfo.getBuilding() + "(" + workOrderInfo.getAddress() + ")";
        tvBuildingAndAddress.setText(buildingAndAddress);
        String type = "维修类型: " + workOrderInfo.getType();
        tvType.setText(type);
        String equipmentAndFaultType = "维修内容: " + workOrderInfo.getEquipment() + "(" + workOrderInfo.getFaultType() + ")";
        tvEquipmentAndFaultType.setText(equipmentAndFaultType);
        String processingTimeLimit = "是否时限: " + workOrderInfo.getProcessingTimeLimit();
        tvProcessingTimeLimit.setText(processingTimeLimit);
        String issuer = "受理人员: " + workOrderInfo.getIssuer();
        tvIssuer.setText(issuer);
        String distributeTime = "派单时间: " + new DateTime(workOrderInfo.getDistributeTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvDistributeTime.setText(distributeTime);
        String distributor = "拍单人员: " + workOrderInfo.getDistributor();
        tvDistributor.setText(distributor);
// todo
//        String statusTag = workOrderInfo.getStatusTag();
//        labelView.setText(statusTag.equals("Distribute") ? "派" : "抢");
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
                    MyButton btn = getSuspendOptionButton(text);
                    btn.name = text;
                    String objectId = JSONUtils.getString(item, "objectId", "");
                    btn.objectId = objectId;
                    flowLayout.addView(btn, layoutParamsForBtn);
                }
            }
            container.addView(flowLayout, layoutParamsFlow);
        }


    }

    private MyButton getSuspendOptionButton(String text) {

        final MyButton btnSuspendOption = new MyButton(this);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void showNumberDialog(final MyButton bootstrapButton) {

        final String btnText = bootstrapButton.getText().toString();
        String title = "输入" + btnText + "数量";
        new MaterialDialog.Builder(this)
                .title(title)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .alwaysCallInputCallback()
                .input("确认", "", new MaterialDialog.InputCallback() {
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
                                    MyButton myButton = (MyButton) flowLayout.getChildAt(j);
                                    if (!myButton.isShowOutline()) {
                                        WorkOrderSupply workOrderSupply = new WorkOrderSupply();
                                        workOrderSupply.setAmount(myButton.amount);
                                        Material material = new Material();
                                        material.setObjectId(myButton.objectId);
                                        material.setName(myButton.name);
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
