package com.unicorn.csp.xcdemo.activity.technician;

import android.os.Bundle;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class PackActivity extends ToolbarActivity {

    @Bind({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11})
    List<BootstrapButton> btnList;


    // ================================== onClick ==================================

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
            R.id.btn7, R.id.btn8, R.id.btn9, R.id.btn10, R.id.btn11})
    void onSuspendOptionClick(BootstrapButton btnClicked) {
        btnClicked.setShowOutline(!btnClicked.isShowOutline());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack);
        initToolbar("领料", true);
        slidr();
    }


}
