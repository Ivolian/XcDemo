package com.unicorn.csp.xcdemo.activity;

import android.os.Bundle;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;


public class GetActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        initToolbar("领料",true);
    }

}
