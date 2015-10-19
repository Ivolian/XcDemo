package com.unicorn.csp.xcdemo.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.Utils;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import butterknife.Bind;
import butterknife.OnClick;


public class FinishActivity extends ToolbarActivity {

    @Bind(R.id.expandableLayout)
    ExpandableLayout expandableLayout;

    @Bind(R.id.expand_container)
    FrameLayout expandContainer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        initToolbar("挂单", true);
        initViews();
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
