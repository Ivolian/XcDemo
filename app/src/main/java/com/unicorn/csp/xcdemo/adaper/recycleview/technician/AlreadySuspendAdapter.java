package com.unicorn.csp.xcdemo.adaper.recycleview.technician;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.technician.DetailActivity;
import com.unicorn.csp.xcdemo.activity.technician.OperationActivity;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AlreadySuspendAdapter extends RecyclerView.Adapter<AlreadySuspendAdapter.ViewHolder> {


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

        @Bind(R.id.btn_operation)
        PaperButton btnOperation;

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

        @OnClick(R.id.btn_operation)
        public void startOperationActivity(PaperButton paperButton) {

            Context context = paperButton.getContext();
            Intent intent = new Intent(context, OperationActivity.class);
            intent.putExtra("workOrderProcessInfo", workOrderProcessInfoList.get(getAdapterPosition()));
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }


    // ================================== item layout ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_already_suspend, viewGroup, false));
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
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {

        return workOrderProcessInfoList.size();
    }

}
