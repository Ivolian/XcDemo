package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.f2prateek.dart.InjectExtra;
import com.google.gson.Gson;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.WorkOrderFrameLayout;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.StringRequestWithSessionCheck;

import butterknife.Bind;


public class WorkOrderDetailActivity extends ToolbarActivity {


    // ================================== extra ==================================

    @InjectExtra("workOrderId")
    String workOrderId;


    // ================================== view ==================================

    @Bind(R.id.work_order_card)
    WorkOrderFrameLayout workOrderCard;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_detail);
        initToolbar("工单详情", true);
        workOrderCard.label.setVisibility(View.GONE);
        fetchWorkOrderInfo();
        enableSlideFinish();
    }


    private void fetchWorkOrderInfo() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderId + "/info";
        StringRequest stringRequest = new StringRequestWithSessionCheck(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        WorkOrderInfo workOrderInfo = new Gson().fromJson(response, WorkOrderInfo.class);
                        workOrderCard.setWorkOrderInfo(workOrderInfo);
                        workOrderCard.expandableLayout.expand();
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(stringRequest);
    }

}
