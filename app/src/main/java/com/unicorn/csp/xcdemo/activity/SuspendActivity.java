package com.unicorn.csp.xcdemo.activity;

import android.os.Bundle;

import com.unicorn.csp.xcdemo.MyApplication;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import butterknife.Bind;
import butterknife.OnClick;


public class SuspendActivity extends ToolbarActivity {


@Bind(R.id.multi)
public MultiStateToggleButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspend);
        initToolbar("挂单", true);
        slidr();
        CharSequence[] texts = new CharSequence[]{"无材料", "预约","其他"};
        button.setElements(texts);
        button.setValue(0);
    }

    @OnClick(R.id.btn_finish)
    public void btnFinishOnClick(){

        MyApplication.ygqFragment.ygqAdapter.addModel();
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

    }
}
