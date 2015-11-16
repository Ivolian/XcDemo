package com.unicorn.csp.xcdemo.adaper.recycleview.chief;

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
import com.unicorn.csp.xcdemo.activity.chief.AssignActivity;
import com.unicorn.csp.xcdemo.activity.shared.SuspendActivity;
import com.unicorn.csp.xcdemo.activity.technician.WorkOrderDetailActivity;
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


public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {


    // ================================== data  ==================================

    private List<WorkOrderProcessInfo> workOrderProcessInfoList = new ArrayList<>();

    public List<WorkOrderProcessInfo> getWorkOrderProcessInfoList() {
        return workOrderProcessInfoList;
    }

    public void setWorkOrderProcessInfoList(List<WorkOrderProcessInfo> workOrderProcessInfoList) {
        this.workOrderProcessInfoList = workOrderProcessInfoList;
    }

    // ================================== viewHolder ==================================

    public  class ViewHolder extends RecyclerView.ViewHolder {

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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cardview)
        public void startDetailActivity(CardView cardView) {
            Context context = cardView.getContext();
            Intent intent = new Intent(context, WorkOrderDetailActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @OnClick(R.id.btn_suspend)
        public void startSuspendActivity(PaperButton paperButton) {
            Context context = paperButton.getContext();
            WorkOrderProcessInfo workOrderProcessInfo = workOrderProcessInfoList.get(getAdapterPosition());
            Intent intent = new Intent(context, SuspendActivity.class);
            intent.putExtra("workOrderProcessInfo",workOrderProcessInfo);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @OnClick(R.id.btn_assign)
        public void startAssignActivity(PaperButton paperButton) {
            Context context = paperButton.getContext();
            WorkOrderProcessInfo workOrderProcessInfo = workOrderProcessInfoList.get(getAdapterPosition());
            Intent intent = new Intent(context, AssignActivity.class);
            intent.putExtra("workOrderProcessInfo",workOrderProcessInfo);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }


    // ================================== item layout ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_todo, viewGroup, false));
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
//        if (position % 2 != 0) {
//            viewHolder.labelView.setBackgroundResource(R.color.blue);
//            viewHolder.labelView.setText("新");
//        } else {
//            viewHolder.labelView.setBackgroundResource(R.color.orange);
//            viewHolder.labelView.setText("限");
//        }
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {

        return workOrderProcessInfoList.size();
    }

}
