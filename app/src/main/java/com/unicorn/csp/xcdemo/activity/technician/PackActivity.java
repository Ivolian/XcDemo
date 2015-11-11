package com.unicorn.csp.xcdemo.activity.technician;

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
public class PackActivity extends ToolbarActivity {


    // ================================== views ==================================

    @Bind(R.id.fl_material_group1)
    FlowLayout flMaterialGroup1;

    @Bind(R.id.fl_material_group2)
    FlowLayout flMaterialGroup2;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        initToolbar("领料", true);
        initMaterialGroup();
        enableSlideFinish();
    }

    private void initMaterialGroup() {

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (String text : new String[]{"风扇(7)", "铜管(0)", "风管(3)", "压缩机(2)", "风扇(7)", "铜管(0)", "风管(3)", "压缩机(2)"}) {
            flMaterialGroup1.addView(getSuspendOptionButton(text), layoutParams);
        }
        for (String text : new String[]{"螺丝(50)", "钉子(30)", "双绞线(10)"}) {
            flMaterialGroup2.addView(getSuspendOptionButton(text), layoutParams);
        }
    }

    private BootstrapButton getSuspendOptionButton(String text) {

        final BootstrapButton btnSuspendOption = new BootstrapButton(this);
        btnSuspendOption.setText(text);
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
