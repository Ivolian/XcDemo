package com.unicorn.csp.xcdemo.activity.technician;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.activity.shared.SuspendActivity;

import butterknife.OnClick;


// @P
public class OperationActivity extends ToolbarActivity {


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        initToolbar("操作", true);
        slidr();
    }


    // ================================== onClick ==================================

    @OnClick(R.id.btn_pack)
    public void startPackActivity() {

        startActivity(PackActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.btn_take_photo)
    public void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 233);
    }

    @OnClick(R.id.btn_achieve)
    public void startAchieveActivity() {

        startActivity(AchieveActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.btn_suspend)
    public void startSuspendActivity() {

        startActivity(SuspendActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    // ================================== finish ==================================

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
