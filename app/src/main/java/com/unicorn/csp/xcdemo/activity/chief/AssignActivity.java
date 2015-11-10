package com.unicorn.csp.xcdemo.activity.chief;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import butterknife.Bind;


// @P
public class AssignActivity extends ToolbarActivity {


    // ================================== views ==================================

    @Bind(R.id.fl_technician_group)
    FlowLayout flTechnicianGroup;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign);
        initToolbar("派单", true);
        initViews();
        slidr();
    }

    private void initViews() {

        initSuspendOptions();
    }

    private void initSuspendOptions() {

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 1; i <= 12; i++) {
            flTechnicianGroup.addView(getSuspendOptionButton(i), layoutParams);
        }
    }


    private BootstrapButton getSuspendOptionButton(int i) {

        final BootstrapButton btnSuspendOption = new BootstrapButton(this);
        btnSuspendOption.setText("技师" + i);
        btnSuspendOption.setPadding(4, 4, 4, 4);
        btnSuspendOption.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        if (i==5 || i==6){
            btnSuspendOption.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
            btnSuspendOption.setEnabled(false);
        }
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
