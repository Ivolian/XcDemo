package com.unicorn.csp.xcdemo.adaper.recycleview.technician;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.technician.DetailActivity;
import com.unicorn.csp.xcdemo.activity.technician.OperationActivity;
import com.unicorn.csp.xcdemo.activity.technician.PackActivity;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AlreadyReceiveAdapter extends RecyclerView.Adapter<AlreadyReceiveAdapter.ViewHolder> {


    // ================================== data  ==================================

    private List<WorkOrderProcessInfo> workOrderProcessInfoList = new ArrayList<>();

    public List<WorkOrderProcessInfo> getWorkOrderProcessInfoList() {
        return workOrderProcessInfoList;
    }

    public void setWorkOrderProcessInfoList(List<WorkOrderProcessInfo> workOrderProcessInfoList) {
        this.workOrderProcessInfoList = workOrderProcessInfoList;
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

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

        @Bind(R.id.btn_arrival_or_operation)
        PaperButton btnArrivalOrOperation;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        @OnClick(R.id.cardview)
        public void startDetailActivity(CardView cardView) {
            Context context = cardView.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @OnClick(R.id.btn_pack)
        public void startPackActivity(PaperButton paperButton) {
            Context context = paperButton.getContext();
            Intent intent = new Intent(context, PackActivity.class);
            WorkOrderProcessInfo workOrderProcessInfo = workOrderProcessInfoList.get(getAdapterPosition());
            intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @OnClick(R.id.btn_arrival_or_operation)
        public void arrivalOrOperate(PaperButton btnArrivalOrOperation) {

            switch (btnArrivalOrOperation.getText()) {
                case "到达":
                    showConfirmArrivalDialog(btnArrivalOrOperation);
                    break;
                case "操作":
                    startOperationActivity(btnArrivalOrOperation);
                    break;
            }
        }

        private void showConfirmArrivalDialog(final PaperButton btnArrivalOrOperate) {

            new MaterialDialog.Builder(btnArrivalOrOperate.getContext())
                    .content("确认到达？")
                    .positiveText("确认")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            arrive(getAdapterPosition(),btnArrivalOrOperate);
                        }
                    })
                    .show();
        }

        private void startOperationActivity(final PaperButton btnArrivalOrOperation) {

            Context context = btnArrivalOrOperation.getContext();
            Intent intent = new Intent(context, OperationActivity.class);
            intent.putExtra("workOrderProcessInfo",workOrderProcessInfoList.get(getAdapterPosition()));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }


    private void arrive(final int position, final PaperButton paperButton) {
        WorkOrderProcessInfo workOrderProcessInfo = workOrderProcessInfoList.get(position);
        WorkOrderInfo workOrderInfo = workOrderProcessInfo.getWorkOrderInfo();
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderInfo.getWorkOrderId() + "/arrive";
        SimpleVolley.addRequest(
                new StringRequest(
                        Request.Method.PUT,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // TODO 改状态，改按钮文字
                                paperButton.setText("操作");

                                ToastUtils.show("到达！");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
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

    // ================================== item layout ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_already_receive, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        WorkOrderProcessInfo workOrderProcessInfo = getWorkOrderProcessInfoList().get(position);
        WorkOrderInfo workOrderInfo = workOrderProcessInfo.getWorkOrderInfo();
        viewHolder.tvRequestUserAndCallNumber.setText("报修电话: " + workOrderInfo.getCallNumber() + " " + workOrderInfo.getRequestUser());
        viewHolder.tvRequestTime.setText("报修时间: " + new DateTime(workOrderInfo.getRequestTime()).toString("yyyy-MM-dd HH:mm:ss"));
        viewHolder.tvBuildingAndAddress.setText("保修地点: " + workOrderInfo.getBuilding() + "(" + workOrderInfo.getAddress() + ")");
        viewHolder.tvType.setText("维修类型: " + workOrderInfo.getType());
        viewHolder.tvEquipmentAndFaultType.setText("维修内容: " + workOrderInfo.getEquipment() + "(" + workOrderInfo.getFaultType() + ")");
        viewHolder.tvProcessingTimeLimit.setText("是否时限: " + workOrderInfo.getProcessingTimeLimit());
        viewHolder.btnArrivalOrOperation.setText(workOrderInfo.getStatus().equals("receive") ? "到达" : "操作");
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {

        return workOrderProcessInfoList.size();
    }

}
