package com.unicorn.csp.xcdemo.activity.technician;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import butterknife.Bind;


// @P
public class SuspendActivity extends ToolbarActivity {


    // ================================== views ==================================

    @Bind(R.id.et_suspend_description)
    BootstrapEditText etSuspendDescription;

    @Bind(R.id.fl_suspend_options)
    FlowLayout flSuspendOptions;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspend);
        initToolbar("挂单", true);
        initViews();
        slidr();
    }

    private void initViews() {

        initSuspendOptions();
        initSuspendDescription();
    }

    private void initSuspendOptions(){

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (String suspendOptionText : new String[]{"无材料", "没有预约", "其他原因1", "其他原因2"}) {
            flSuspendOptions.addView(getSuspendOptionButton(suspendOptionText), layoutParams);
        }
    }

    private void initSuspendDescription() {

        etSuspendDescription.setGravity(Gravity.TOP);
        etSuspendDescription.setPadding(20, 20, 20, 20);
        etSuspendDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }

    private BootstrapButton getSuspendOptionButton(String suspendOptionText) {

        final BootstrapButton btnSuspendOption = new BootstrapButton(this);
        btnSuspendOption.setText(suspendOptionText);
        btnSuspendOption.setPadding(4, 4, 4, 4);
        btnSuspendOption.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        btnSuspendOption.setBootstrapSize(DefaultBootstrapSize.MD);
        btnSuspendOption.setRounded(true);
        btnSuspendOption.setShowOutline(true);
        btnSuspendOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSuspendOption.setShowOutline(!btnSuspendOption.isShowOutline());
            }
        });
        return btnSuspendOption;
    }


    // ================================== finish ==================================

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
