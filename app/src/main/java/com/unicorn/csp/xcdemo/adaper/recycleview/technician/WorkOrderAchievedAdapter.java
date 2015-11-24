package com.unicorn.csp.xcdemo.adaper.recycleview.technician;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


//@P
public class WorkOrderAchievedAdapter extends RecyclerView.Adapter<WorkOrderAchievedAdapter.ViewHolder> implements RefreshAdapter {


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

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cardview)
        public void startWorkOrderDetailActivity(CardView cardView) {

            int position = getAdapterPosition();
            WorkOrderProcessInfo workOrderProcessInfo = workOrderProcessInfoList.get(position);
            tvProcessingTimeLimit.setVisibility(workOrderProcessInfo.isExpand() ? View.GONE : View.VISIBLE);
            workOrderProcessInfo.setExpand(!workOrderProcessInfo.isExpand());


//            Context context = cardView.getContext();
//            Intent intent = new Intent(context, WorkOrderDetailActivity.class);
//            intent.putExtra("workOrderProcessInfo", workOrderProcessInfoList.get(getAdapterPosition()));
//            context.startActivity(intent);
//            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_order_achieved, viewGroup, false));
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
        viewHolder.labelView.setText("结");

        WorkOrderProcessInfo workOrderProcessInfo = workOrderProcessInfoList.get(position);
        viewHolder.tvProcessingTimeLimit.setVisibility(workOrderProcessInfo.isExpand() ? View.VISIBLE : View.GONE);

    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderProcessInfoList.size();
    }

}
