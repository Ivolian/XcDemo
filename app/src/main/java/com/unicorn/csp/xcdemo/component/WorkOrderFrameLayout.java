package com.unicorn.csp.xcdemo.component;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderSupplyInfo;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorkOrderFrameLayout extends FrameLayout {

    @Bind(R.id.labelview)
    public LabelView label;

    @Bind(R.id.tv_request_user)
    TextView tvRequestUser;

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

    public void setWorkOrderInfo(final WorkOrderInfo workOrderInfo) {
        label.setText(workOrderInfo.getLabelText());

        // top part
        String requestUserText = "报修人员: " + workOrderInfo.getRequestUser();
        tvRequestUser.setText(requestUserText);
        addDialLink(requestUserText, workOrderInfo.getCallNumber(), tvRequestUser);

        String requestTimeText = "报修时间: " + getDateString(workOrderInfo.getRequestTime());
        tvRequestTime.setText(requestTimeText);
        String buildingAndAddressText = "报修地点: " + workOrderInfo.getBuilding() + "(" + workOrderInfo.getAddress() + ")";
        tvBuildingAndAddress.setText(buildingAndAddressText);
        String typeText = "维修类型: " + workOrderInfo.getType();
        tvType.setText(typeText);
        String equipmentAndFaultTypeText = "维修内容: " + workOrderInfo.getEquipment() + "(" + workOrderInfo.getFaultType() + ")";
        tvEquipmentAndFaultType.setText(equipmentAndFaultTypeText);
        String processingTimeLimitText = "是否时限: " + workOrderInfo.getProcessingTimeLimit();
        tvProcessingTimeLimit.setText(processingTimeLimitText);
        String issuerText = "受理人员: " + workOrderInfo.getIssuer();
        tvIssuer.setText(issuerText);
        String issuerTimeText = "受理时间: " + getDateString(workOrderInfo.getIssueTime());
        tvIssueTime.setText(issuerTimeText);

        //
        String distributorText = "派单人员: " + workOrderInfo.getDistributor();
        tvDistributor.setText(distributorText);
        String distributeTimeText = "派单时间: " + getDateString(workOrderInfo.getDistributeTime());
        tvDistributeTime.setText(distributeTimeText);
        tvDistributor.setVisibility(workOrderInfo.getDistributor() == null ? GONE : VISIBLE);
        tvDistributeTime.setVisibility(workOrderInfo.getDistributor() == null ? GONE : VISIBLE);

        //
        String receiverText = "接单人员: " + workOrderInfo.getReceiver();
        tvReceiver.setText(receiverText);
        addDialLink(receiverText, workOrderInfo.getReceiverPhone(), tvReceiver);
        String receiverTimeText = "接单时间: " + getDateString(workOrderInfo.getReceiveTime());
        tvReceiverTime.setText(receiverTimeText);
        tvReceiver.setVisibility(workOrderInfo.getReceiver() == null ? GONE : VISIBLE);
        tvReceiverTime.setVisibility(workOrderInfo.getReceiver() == null ? GONE : VISIBLE);

        String arriveTime = "到达时间: " + new DateTime(workOrderInfo.getArriveTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvArriveTime.setText(arriveTime);
        tvArriveTime.setVisibility(workOrderInfo.getArriveTime() == 0 ? GONE : VISIBLE);

        String hangUpTime = "挂单时间: " + new DateTime(workOrderInfo.getHangUpTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvHangUpTime.setText(hangUpTime);
        tvHangUpTime.setVisibility(workOrderInfo.getHangUpTime() == 0 ? GONE : VISIBLE);

        String completeTime = "结单时间: " + new DateTime(workOrderInfo.getCompleteTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvCompleteTime.setText(completeTime);
        tvCompleteTime.setVisibility(workOrderInfo.getCompleteTime() == 0 ? GONE : VISIBLE);


        String confirmText = "复核人员: " + workOrderInfo.getConfirm();
        tvConfirm.setText(confirmText);
        String confirmTimeText = "复核时间: " + getDateString(workOrderInfo.getConfirmTime());
        tvConfirmTime.setText(confirmTimeText);
        tvConfirm.setVisibility(workOrderInfo.getConfirm() == null ? GONE : VISIBLE);
        tvConfirmTime.setVisibility(workOrderInfo.getConfirm() == null ? GONE : VISIBLE);

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


    private void addDialLink(String linkText, final String telephone, TextView tvTarget) {
        Link link = new Link(linkText)
                .setTextColor(getResources().getColor(R.color.primary))
                .setOnClickListener(new Link.OnClickListener() {
                    @Override
                    public void onClick(String clickedText) {
                        Uri uri = Uri.parse("tel:" + telephone);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(uri);
                        getContext().startActivity(intent);
                    }
                });
        LinkBuilder.on(tvTarget)
                .addLink(link)
                .build();
    }

    private String getDateString(long time) {
        return new DateTime(time).toString("yyyy-MM-dd HH:mm:ss");
    }

}
