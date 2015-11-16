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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.technician.OperationActivity;
import com.unicorn.csp.xcdemo.activity.technician.PackActivity;
import com.unicorn.csp.xcdemo.activity.technician.WorkOrderDetailActivity;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.StringRequestWithSessionCheck;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


//@P
public class WorkOrderReceivedAdapter extends RecyclerView.Adapter<WorkOrderReceivedAdapter.ViewHolder> implements RefreshAdapter {


    // ================================== data ==================================

    private List<WorkOrderProcessInfo> workOrderProcessInfoList = new ArrayList<>();

    @Override
    public void reload(List<WorkOrderProcessInfo> workOrderProcessInfoList) {
        this.workOrderProcessInfoList = workOrderProcessInfoList;
        notifyDataSetChanged();
    }

    @Override
    public void loadMore(List<WorkOrderProcessInfo> workOrderProcessInfoList) {
        this.workOrderProcessInfoList.addAll(workOrderProcessInfoList);
        notifyDataSetChanged();
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
        public void startWorkOrderDetailActivity(CardView cardView) {
            Context context = cardView.getContext();
            Intent intent = new Intent(context, WorkOrderDetailActivity.class);
            intent.putExtra("workOrderProcessInfo", workOrderProcessInfoList.get(getAdapterPosition()));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        // ================================== 领料 ==================================

        @OnClick(R.id.btn_pack)
        public void startPackActivity(PaperButton paperButton) {
            Context context = paperButton.getContext();
            Intent intent = new Intent(paperButton.getContext(), PackActivity.class);
            intent.putExtra("workOrderProcessInfo", workOrderProcessInfoList.get(getAdapterPosition()));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }


        // ================================== 到达 & 操作 ==================================

        @OnClick(R.id.btn_arrival_or_operation)
        public void arrivalOrOperate(PaperButton btnArrivalOrOperation) {
            switch (btnArrivalOrOperation.getText()) {
                case "操作":
                    startOperationActivity(btnArrivalOrOperation);
                    break;
                case "到达":
                    showConfirmArrivalDialog(btnArrivalOrOperation);
                    break;
            }
        }

        private void startOperationActivity(final PaperButton paperButton) {
            Context context = paperButton.getContext();
            Intent intent = new Intent(context, OperationActivity.class);
            intent.putExtra("workOrderProcessInfo", workOrderProcessInfoList.get(getAdapterPosition()));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        private void showConfirmArrivalDialog(final PaperButton btnArrivalOrOperate) {
            new MaterialDialog.Builder(btnArrivalOrOperate.getContext())
                    .content("确认到达？")
                    .positiveText("确认")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            arrive(btnArrivalOrOperate);
                        }
                    })
                    .show();
        }

        private void arrive(final PaperButton btnArrivalOrOperate) {
            final WorkOrderInfo workOrderInfo = workOrderProcessInfoList.get(getAdapterPosition()).getWorkOrderInfo();
            String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderInfo.getWorkOrderId() + "/arrive";
            StringRequest stringRequest = new StringRequestWithSessionCheck(
                    Request.Method.PUT,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            btnArrivalOrOperate.setText("操作");
                            workOrderInfo.setStatus("到达");
                            workOrderInfo.setStatusTag("Arrive");
                            ToastUtils.show("到达！");
                        }
                    },
                    SimpleVolley.getDefaultErrorListener()
            );
            SimpleVolley.addRequest(stringRequest);
        }
    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_order_received, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        WorkOrderInfo workOrderInfo = workOrderProcessInfoList.get(position).getWorkOrderInfo();
        String requestUserAndCallNumber = "报修电话: " + workOrderInfo.getCallNumber() + " " + workOrderInfo.getRequestUser();
        viewHolder.tvRequestUserAndCallNumber.setText(requestUserAndCallNumber);
        String requestTime = "报修时间: " + new DateTime(workOrderInfo.getRequestTime()).toString("yyyy-MM-dd HH:mm:ss");
        viewHolder.tvRequestTime.setText(requestTime);
        String buildingAndAddress = "保修地点: " + workOrderInfo.getBuilding() + "(" + workOrderInfo.getAddress() + ")";
        viewHolder.tvBuildingAndAddress.setText(buildingAndAddress);
        String type = "维修类型: " + workOrderInfo.getType();
        viewHolder.tvType.setText(type);
        String equipmentAndFaultType = "维修内容: " + workOrderInfo.getEquipment() + "(" + workOrderInfo.getFaultType() + ")";
        viewHolder.tvEquipmentAndFaultType.setText(equipmentAndFaultType);
        String processingTimeLimit = "是否时限: " + workOrderInfo.getProcessingTimeLimit();
        viewHolder.tvProcessingTimeLimit.setText(processingTimeLimit);
        viewHolder.btnArrivalOrOperation.setText(workOrderInfo.getStatusTag().equals("Receive")?"到达":"操作");
        viewHolder.labelView.setText("接");
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderProcessInfoList.size();
    }

}
