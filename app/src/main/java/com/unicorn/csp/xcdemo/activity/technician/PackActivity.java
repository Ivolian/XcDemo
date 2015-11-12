package com.unicorn.csp.xcdemo.activity.technician;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
        for (String text : new String[]{"风扇", "铜管", "风管", "压缩机"}) {
            flMaterialGroup1.addView(getSuspendOptionButton(text), layoutParams);
        }
        for (String text : new String[]{"螺丝", "钉子", "双绞线"}) {
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
                if (btnSuspendOption.isShowOutline()) {
                    showNumberDialog(btnSuspendOption);
                } else {
                    String btnText = btnSuspendOption.getText().toString();
                    int index = btnText.indexOf("(");
                    btnSuspendOption.setText(btnText.substring(0, index));
                    btnSuspendOption.setShowOutline(true);
                }
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

    private void showNumberDialog(final BootstrapButton bootstrapButton) {

        final String btnText = bootstrapButton.getText().toString();
        String title = "输入" + btnText + "数量";
        new MaterialDialog.Builder(this)
                .title(title)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .alwaysCallInputCallback()
                .input("确认", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        boolean isNumeric = org.apache.commons.lang3.StringUtils.isNumeric(input);
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(isNumeric);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        if (materialDialog.getInputEditText() != null) {
                            String number = materialDialog.getInputEditText().getText().toString();
                            bootstrapButton.setText(btnText + "(" + number + ")");
                            bootstrapButton.setShowOutline(false);
                        }
                    }
                })
                .show();
    }


}
