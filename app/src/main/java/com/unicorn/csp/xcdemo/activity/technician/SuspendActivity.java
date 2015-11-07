package com.unicorn.csp.xcdemo.activity.technician;

import android.os.Bundle;
import android.view.Gravity;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


//@P
public class SuspendActivity extends ToolbarActivity {


    // ================================== views ==================================

    @Bind({R.id.btn_suspend_option1, R.id.btn_suspend_option2, R.id.btn_suspend_option3})
    List<BootstrapButton> btnSuspendOptionList;

    @Bind(R.id.et_suspend_description)
    BootstrapEditText etSuspendDescription;


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

        etSuspendDescription.setGravity(Gravity.TOP);
    }


    // ================================== onClick ==================================

    @OnClick({R.id.btn_suspend_option1, R.id.btn_suspend_option2, R.id.btn_suspend_option3})
    void onSuspendOptionClick(BootstrapButton btnClicked) {
        btnClicked.setShowOutline(!btnClicked.isShowOutline());
    }


    // ================================== finish ==================================

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
