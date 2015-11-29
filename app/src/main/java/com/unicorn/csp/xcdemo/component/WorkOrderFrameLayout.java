package com.unicorn.csp.xcdemo.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderSupplyInfo;
import com.unicorn.csp.xcdemo.utils.PrettyTimeUtils;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorkOrderFrameLayout extends FrameLayout {

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

    @Bind(R.id.expandableLayout)
    public ExpandableRelativeLayout expandableLayout;

    //

    public WorkOrderFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    //


    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.work_order_frame_layout, this, true);
        ButterKnife.bind(this);
    }

    public void setWorkOrderInfo(WorkOrderInfo workOrderInfo) {

        String requestUserAndCallNumber = "报修电话: " + workOrderInfo.getCallNumber() + " " + workOrderInfo.getRequestUser();
        tvRequestUserAndCallNumber.setText(requestUserAndCallNumber);
        String requestTime = "报修时间: " + PrettyTimeUtils.pretty(workOrderInfo.getRequestTime());
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
        String issuerTime = "受理时间: " + PrettyTimeUtils.pretty(workOrderInfo.getIssueTime());
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
        String receiverTime = "接单时间: " +PrettyTimeUtils.pretty(workOrderInfo.getReceiveTime());
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
        }else{
            tvConfirm.setVisibility(View.VISIBLE);
            tvConfirmTime.setVisibility(View.VISIBLE);
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
            tvPack.setVisibility(View.VISIBLE);
        }
    }

}
