package com.unicorn.csp.xcdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import butterknife.OnClick;


public class OperationActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        initToolbar("操作", true);
    }

    @OnClick(R.id.btn_take_photo)
    public void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 233);
    }

    @OnClick(R.id.btn_ll)
    public void startGetActivity() {

        startActivity(LLActivity.class);
    }

    @OnClick(R.id.btn_over)
    public void startOverActivity() {

        startActivity(OverActivity.class);
    }

    @OnClick(R.id.btn_gd)
    public void onBtnGdClick(){

//        startActivity(SuspendActivity.class);
    }

}
