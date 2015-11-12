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
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AlreadyAchieveAdapter extends RecyclerView.Adapter<AlreadyAchieveAdapter.ViewHolder> {


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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Bind(R.id.tv_request_user_and_call_number)
        TextView tvRequestUserAndCallNumber;

        @Bind(R.id.tv_request_time)
        TextView tvRequestTime;

        @OnClick(R.id.cardview)
        public void startDetailActivity(CardView cardView) {
            Context context = cardView.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }


    // ================================== item layout ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_already_achieve, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        WorkOrderProcessInfo workOrderProcessInfo = getWorkOrderProcessInfoList().get(position);
        WorkOrderInfo workOrderInfo = workOrderProcessInfo.getWorkOrderInfo();
        viewHolder.tvRequestUserAndCallNumber.setText("报修电话: " + workOrderInfo.getCallNumber() + " " + workOrderInfo.getRequestUser());
        viewHolder.tvRequestTime.setText("报修时间: " + new DateTime(workOrderInfo.getRequestTime()).toString("yyyy-MM-dd HH:mm:ss"));
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {

        return workOrderProcessInfoList.size();
    }

}
