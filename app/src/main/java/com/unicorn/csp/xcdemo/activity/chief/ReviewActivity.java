package com.unicorn.csp.xcdemo.activity.chief;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.f2prateek.dart.InjectExtra;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.MyButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcess;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderSupplyInfo;
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


// @P
public class ReviewActivity extends ToolbarActivity {


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

    //

    //

    @Bind(R.id.tv_issuer)
    TextView tvIssuer;

    @Bind(R.id.tv_issue_time)
    TextView tvIssueTime;

    @Bind(R.id.tv_distributor)
    TextView tvDistributor;

    @Bind(R.id.tv_distribute_time)
    TextView tvDistributeTime;

    @Bind(R.id.tv_receiver)
    TextView tvReceiver;

    @Bind(R.id.tv_receive_time)
    TextView tvReceiverTime;

    @Bind(R.id.tv_arrive_time)
    TextView tvArriveTime;

    @Bind(R.id.tv_hang_up_time)
    TextView tvHangUpTime;

    @Bind(R.id.tv_complete_time)
    TextView tvCompleteTime;

    @Bind(R.id.tv_confirm)
    TextView tvConfirm;

    @Bind(R.id.tv_confirm_time)
    TextView tvConfirmTime;

    @Bind(R.id.tv_pack)
    TextView tvPack;


    //

    @InjectExtra("workOrderProcessInfo")
    WorkOrderProcessInfo workOrderProcessInfo;

    @Bind(R.id.tv_technician)
    TextView tvTechcian;

    @Bind(R.id.tv_review_description)
    TextView tvReviewDescription;

    // ================================== views ==================================

    @Bind(R.id.et_description)
    BootstrapEditText etDescription;

    @Bind(R.id.fl_options)
    FlowLayout flOptions;

    List<MyButton> buttonList = new ArrayList<>();

    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        initToolbar("复核", true);
        initViews();
        enableSlideFinish();
    }

    private void initViews() {

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

        //

        String issuer = "受理人员: " + workOrderInfo.getIssuer();
        tvIssuer.setText(issuer);
        String issuerTime = "受理时间: " + new DateTime(workOrderInfo.getIssueTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvIssueTime.setText(issuerTime);
        if (workOrderInfo.getIssuer() == null) {
            tvIssuer.setVisibility(View.GONE);
            tvIssueTime.setVisibility(View.GONE);
        }
        String distributor = "派单人员: " + workOrderInfo.getDistributor();
        tvDistributor.setText(distributor);
        String distributeTime = "派单时间: " + new DateTime(workOrderInfo.getDistributeTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvDistributeTime.setText(distributeTime);
        if (workOrderInfo.getDistributor() == null) {
            tvDistributor.setVisibility(View.GONE);
            tvDistributeTime.setVisibility(View.GONE);
        }
        String receiver = "接单人员: " + workOrderInfo.getReceiver();
        tvReceiver.setText(receiver);
        String receiverTime = "接单时间: " + new DateTime(workOrderInfo.getReceiveTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvReceiverTime.setText(receiverTime);
        if (workOrderInfo.getReceiver() == null) {
            tvReceiver.setVisibility(View.GONE);
            tvReceiverTime.setVisibility(View.GONE);
        }
        String arriveTime = "到达时间: " + new DateTime(workOrderInfo.getArriveTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvArriveTime.setText(arriveTime);
        if (workOrderInfo.getArriveTime() == 0) {
            tvArriveTime.setVisibility(View.GONE);
        }
        String hangUpTime = "挂单时间: " + new DateTime(workOrderInfo.getHangUpTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvHangUpTime.setText(hangUpTime);
        if (workOrderInfo.getHangUpTime() == 0) {
            tvHangUpTime.setVisibility(View.GONE);
        }
        String completeTime = "结单时间: " + new DateTime(workOrderInfo.getCompleteTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvCompleteTime.setText(completeTime);
        if (workOrderInfo.getCompleteTime() == 0) {
            tvCompleteTime.setVisibility(View.GONE);
        }
        String confirm = "复核人员: " + workOrderInfo.getConfirm();
        tvConfirm.setText(confirm);
        String confirmTime = "复核时间: " + new DateTime(workOrderInfo.getConfirmTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvConfirmTime.setText(confirmTime);
        if (workOrderInfo.getConfirm() == null) {
            tvConfirm.setVisibility(View.GONE);
            tvConfirmTime.setVisibility(View.GONE);
        }

        //
        List<WorkOrderSupplyInfo> workOrderSupplyInfoList = workOrderInfo.getSupplyList();
        if (workOrderSupplyInfoList.size() == 0) {
            tvPack.setVisibility(View.GONE);
        } else {
            String pack = "领料情况: ";
            for (WorkOrderSupplyInfo workOrderSupplyInfo : workOrderSupplyInfoList) {
                pack += (workOrderSupplyInfo.getMaterial() + "(" + workOrderSupplyInfo.getAmount() + ") ");
            }
            tvPack.setText(pack);
        }

        //


        fetchOptions();
        initSuspendDescription();

        WorkOrderProcess workOrderProcess = workOrderProcessInfo.getWorkOrderProcess();
        tvTechcian.setText("技师: " + workOrderProcess.getOperator().getName());
        tvReviewDescription.setText("复核说明: " + workOrderProcess.getWorkOrderReview().getRemark());
    }

    private void fetchOptions() {

        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/confirm/options";
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
                                    flOptions.addView(myButton, layoutParams);
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

        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
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




    @OnClick(R.id.btn_review)
    public void suspendConfirm() {
        new MaterialDialog.Builder(this)
                .content("确认复核？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        review();
                    }
                })
                .show();
    }

    public void review() {

        MyButton btnSelected = null;
        for (MyButton myButton : buttonList) {
            if (!myButton.isShowOutline()) {
                btnSelected = myButton;
            }
        }
        if (btnSelected==null){
            ToastUtils.show("请至少选择一个复核选项");
            return;
        }

        WorkOrderProcessInfo workOrderProcessInfo = (WorkOrderProcessInfo) getIntent().getSerializableExtra("workOrderProcessInfo");
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId() + "/confirm";
        SimpleVolley.getRequestQueue().add(
                new StringRequest(
                        Request.Method.PUT,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ToastUtils.show("复核成功!");
                                setResult(333);
                                ReviewActivity.this.finish();
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
                            result.put("remark", etDescription.getText().toString().trim());
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
