package com.unicorn.csp.xcdemo.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class LLActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);
        initToolbar("领料", true);
    }

    @OnClick(R.id.btn_finish)
    public void onBtnFinishClick() {
        finish();
    }

    @Bind({R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6, R.id.text7, R.id.text8})
    public List<TextView> textList;

    @OnClick({R.id.text1, R.id.text2, R.id.text3, R.id.text4, R.id.text5, R.id.text6, R.id.text7, R.id.text8})
    public void onTextClick(TextView clicked) {
        for (TextView text : textList) {
            text.setTextColor(getResources().getColor(android.R.color.black));
            text.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
        clicked.setTextColor(getResources().getColor(android.R.color.white));
        clicked.setBackgroundResource(R.color.blue);
    }


    @Bind({R.id.text9, R.id.text10, R.id.text11})
    public List<TextView> textList2;

    @OnClick({R.id.text9, R.id.text10, R.id.text11})
    public void onText2Click(TextView clicked) {
        for (TextView text : textList2) {
            text.setTextColor(getResources().getColor(android.R.color.black));
            text.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
        clicked.setTextColor(getResources().getColor(android.R.color.white));
        clicked.setBackgroundResource(R.color.blue);
    }

}
