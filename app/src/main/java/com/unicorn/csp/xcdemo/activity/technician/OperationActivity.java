package com.unicorn.csp.xcdemo.activity.technician;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.activity.shared.SuspendActivity;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ImageUtils;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.OnClick;


// @P
public class OperationActivity extends ToolbarActivity {


    @Bind(R.id.btn_suspend)
    PaperButton btnSuspend;

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

    @Bind(R.id.tv_distribute_time)
    TextView tvDistributeTime;

    @Bind(R.id.tv_distributor)
    TextView tvDistributor;

    // ================================== extra ==================================

    @InjectExtra("workOrderProcessInfo")
    WorkOrderProcessInfo workOrderProcessInfo;


    // ================================== fields ==================================

    final int TAKE_PHOTO_REQUEST_CODE = 2333;

    String currentPhotoPath = "";


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        initToolbar("操作", true);
        initViews();
        enableSlideFinish();
    }

    private void initViews(){
        WorkOrderInfo workOrderInfo = workOrderProcessInfo.getWorkOrderInfo();
        String requestUserAndCallNumber = "报修电话: " + workOrderInfo.getCallNumber() + " " + workOrderInfo.getRequestUser();
        tvRequestUserAndCallNumber.setText(requestUserAndCallNumber);
        String requestTime = "报修时间: " + new DateTime(workOrderInfo.getRequestTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvRequestTime.setText(requestTime);
        String buildingAndAddress = "保修地点: " + workOrderInfo.getBuilding() + "(" + workOrderInfo.getAddress() + ")";
        tvBuildingAndAddress.setText(buildingAndAddress);
        String type = "维修类型: " + workOrderInfo.getType();
        tvType.setText(type);
        String equipmentAndFaultType = "维修内容: " + workOrderInfo.getEquipment() + "(" + workOrderInfo.getFaultType() + ")";
        tvEquipmentAndFaultType.setText(equipmentAndFaultType);
        String processingTimeLimit = "是否时限: " + workOrderInfo.getProcessingTimeLimit();
        tvProcessingTimeLimit.setText(processingTimeLimit);
        String issuer = "受理人员: " + workOrderInfo.getIssuer();
        tvIssuer.setText(issuer);
        String distributeTime = "派单时间: " + new DateTime(workOrderInfo.getDistributeTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvDistributeTime.setText(distributeTime);
        String distributor = "拍单人员: " + workOrderInfo.getDistributor();

        tvDistributor.setText(distributor);

        String statusTag = workOrderInfo.getStatusTag();
        labelView.setText(statusTag.equals("Distribute") ? "派" : "抢");

        if (workOrderInfo.getDistributor() == null) {
            tvDistributeTime.setVisibility(View.GONE);
            tvDistributor.setVisibility(View.GONE);
        }


        if (workOrderInfo.getStatusTag().equals("HangUp") ){
            btnSuspend.setColor(getResources().getColor(R.color.md_blue_grey_500));
        }else {
            btnSuspend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OperationActivity.this, SuspendActivity.class);
                    intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }

    }


    // ================================== onClick ==================================

    @OnClick(R.id.btn_pack)
    public void startPackActivity() {
        Intent intent = new Intent(this, PackActivity.class);
        intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.btn_achieve)
    public void startAchieveActivity() {
        Intent intent = new Intent(this, AchieveActivity.class);
        intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
        startActivityForResult(intent,2333);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.btn_take_photo)
    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri randomUri = ImageUtils.getRandomPhotoUri();
        currentPhotoPath = randomUri.getPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, randomUri);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
    }


    // ================================== onActivityResult ==================================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, PhotoConfirmActivity.class);
                intent.putExtra("workOrderProcessInfo",workOrderProcessInfo);
                intent.putExtra("photoPath", currentPhotoPath);
                startActivity(intent);
            }
        }

        if (resultCode==333){
            finish();
        }
    }


    // ================================== finish ==================================

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
