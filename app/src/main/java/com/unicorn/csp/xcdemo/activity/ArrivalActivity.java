package com.unicorn.csp.xcdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import butterknife.OnClick;


public class ArrivalActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrival);
        initToolbar("到达", true);
    }

    @OnClick(R.id.btn_take_photo)
    public void takePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 233);
    }

    @OnClick(R.id.btn_get)
    public void startGetActivity(){

        startActivity(GetActivity.class);
    }

}
