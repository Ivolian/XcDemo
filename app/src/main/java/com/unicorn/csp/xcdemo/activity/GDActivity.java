package com.unicorn.csp.xcdemo.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.Utils;
import com.unicorn.csp.xcdemo.MyApplication;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class GDActivity extends ToolbarActivity {

    @Bind(R.id.expandableLayout)
    ExpandableLayout expandableLayout;

    @Bind(R.id.expand_container)
    FrameLayout expandContainer;

    @Bind({R.id.text1, R.id.text2})
    public List<TextView> textList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gd);
        initToolbar("挂单", true);
        initViews();
        slidr();
    }

    @OnClick(R.id.btn_finish)
    public void btnFinishOnClick(){

        MyApplication.ygqFragment.ygqAdapter.addModel();
        finish();
    }

    @OnClick({R.id.text1, R.id.text2})
    public void onTextClick(TextView clicked) {
        for (TextView text : textList) {
            text.setTextColor(getResources().getColor(android.R.color.black));
            text.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
        clicked.setTextColor(getResources().getColor(android.R.color.white));
        clicked.setBackgroundResource(R.color.blue);
    }


    private void initViews() {

        expandableLayout.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                createRotateAnimator(expandContainer, 0f, 180f).start();
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(expandContainer, 180f, 0f).start();
            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
            }
        });
    }

    @OnClick(R.id.cardview)
    public void toggle() {
        expandableLayout.toggle();
    }


    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

}
