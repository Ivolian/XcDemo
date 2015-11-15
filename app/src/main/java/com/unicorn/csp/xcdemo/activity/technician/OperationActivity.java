package com.unicorn.csp.xcdemo.activity.technician;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.activity.shared.SuspendActivity;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ImageUtils;

import butterknife.OnClick;


// @P
public class OperationActivity extends ToolbarActivity {


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
        enableSlideFinish();
    }


    // ================================== onClick ==================================

    @OnClick(R.id.btn_pack)
    public void startPackActivity() {
        Intent intent = new Intent(this, PackActivity.class);
        intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.btn_suspend)
    public void startSuspendActivity() {
        Intent intent = new Intent(this, SuspendActivity.class);
        intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @OnClick(R.id.btn_achieve)
    public void startAchieveActivity() {
        Intent intent = new Intent(this, AchieveActivity.class);
        intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
        startActivity(intent);
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
                intent.putExtra("photoPath", currentPhotoPath);
                startActivity(intent);
            }
        }
    }


    // ================================== finish ==================================

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
