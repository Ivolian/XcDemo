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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WorkOrderFrameLayout extends FrameLayout {

    @BindView(R.id.labelview)
    public LabelView label;

    @BindView(R.id.tv_order_code)
    TextView tvOrderCode;

    @BindView(R.id.tv_request_user)
    TextView tvRequestUser;

    @BindView(R.id.tv_request_time)
    TextView tvRequestTime;

    @BindView(R.id.tv_building_and_address)
    TextView tvBuildingAndAddress;

    @BindView(R.id.tv_type)
    TextView tvType;

    @BindView(R.id.tv_equipment_and_fault_type)
    TextView tvEquipmentAndFaultType;

    @BindView(R.id.tv_processing_time_limit)
    TextView tvProcessingTimeLimit;

    @BindView(R.id.tv_issuer)
    TextView tvIssuer;

    @BindView(R.id.tv_issue_time)
    TextView tvIssueTime;

    @BindView(R.id.tv_distributor)
    TextView tvDistributor;

    @BindView(R.id.tv_distribute_time)
    TextView tvDistributeTime;

    @BindView(R.id.tv_receiver)
    TextView tvReceiver;

    @BindView(R.id.tv_receive_time)
    TextView tvReceiverTime;

    @BindView(R.id.tv_arrive_time)
    TextView tvArriveTime;

    @BindView(R.id.tv_hang_up_time)
    TextView tvHangUpTime;

    @BindView(R.id.tv_complete_time)
    TextView tvCompleteTime;

    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    @BindView(R.id.tv_confirm_time)
    TextView tvConfirmTime;

    @BindView(R.id.tv_pack)
    TextView tvPack;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    @BindView(R.id.tv_fault_description)
    TextView tvFaultDescription;

    @BindView(R.id.tv_hangup_description)
    TextView tvHangupDescription;


    @BindView(R.id.expandableLayout)
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

    private String hangupDescription;

    public void setHangupDescription(String hangupDescription) {
        this.hangupDescription = hangupDescription;
    }

    public void setWorkOrderInfo(final WorkOrderInfo workOrderInfo) {
        label.setText(workOrderInfo.getLabelText());

        // top part
        String orderCode = "订单编号: " + workOrderInfo.getWorkOrderCode();
        tvOrderCode.setText(orderCode);

        String requestUserText = "报修人员: " + workOrderInfo.getRequestUser();
        tvRequestUser.setText(requestUserText);
        addDialLink(requestUserText, workOrderInfo.getCallNumber(), tvRequestUser);

        String requestTimeText = "报修时间: " + getDateString(workOrderInfo.getRequestTime());
        tvRequestTime.setText(requestTimeText);

        String buildingAndAddressText = "报修地点: " + workOrderInfo.getBuilding().split("/")[0] + "/" + workOrderInfo.getRequestDepartment() + "/" + workOrderInfo.getAddress();
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


        String description = "备注说明: " + workOrderInfo.getDescription();
        tvDescription.setText(description);
        tvDescription.setVisibility(workOrderInfo.getDescription() == null ? GONE : VISIBLE);

        String faultDescription = "故障原因: " + workOrderInfo.getFaultDescription();
        tvFaultDescription.setText(faultDescription);
        tvFaultDescription.setVisibility(workOrderInfo.getFaultDescription() == null ? GONE : VISIBLE);


        String hangupDescriptionText = "挂单说明: " + hangupDescription;
        tvHangupDescription.setText(hangupDescriptionText);
        tvHangupDescription.setVisibility( hangupDescription== null ? GONE : VISIBLE);

        //
        List<WorkOrderSupplyInfo> workOrderSupplyInfoList = workOrderInfo.getSupplyList();
        if (workOrderSupplyInfoList.size() == 0) {
            tvPack.setVisibility(View.GONE);
        } else {
            String pack = "领料情况: ";
            Map<String, Integer> map = new LinkedHashMap<>();
            for (WorkOrderSupplyInfo workOrderSupplyInfo : workOrderSupplyInfoList) {
                String materialName = workOrderSupplyInfo.getMaterialName();
                if (materialName != null) {
                    if (!map.containsKey(materialName)) {
                        map.put(materialName, workOrderSupplyInfo.getAmount());
                    } else {
                        int amount = map.get(materialName) + workOrderSupplyInfo.getAmount();
                        map.put(materialName, amount);
                    }
                }
            }
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                pack += (entry.getKey() + "(" + entry.getValue() + ")  ");
            }

            //
            for (WorkOrderSupplyInfo workOrderSupplyInfo : workOrderSupplyInfoList) {
                if (workOrderSupplyInfo.getMaterialName() == null) {
                    pack += (workOrderSupplyInfo.getMaterialInfo() + " ");
                }
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
