package com.unicorn.csp.xcdemo.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.AutoLabelUISettings;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.Utils;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.utils.EditTextUtils;

import butterknife.Bind;
import butterknife.OnClick;


public class FinishActivity extends ToolbarActivity {

    @Bind(R.id.expandableLayout)
    ExpandableLayout expandableLayout;

    @Bind(R.id.expand_container)
    FrameLayout expandContainer;

    @Bind(R.id.auto_label)
    AutoLabelUI autoLabel;

    @Bind(R.id.et_reason)
    MaterialEditText etReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        initToolbar("挂单", true);
        initViews();
        initAutoLabel();
    }

    private void initAutoLabel() {

        AutoLabelUISettings autoLabelUISettings =
                new AutoLabelUISettings.Builder()
                        .withBackgroundColor(R.color.colorAccent)
                        .withIconCross(R.drawable.ic_highlight_off_white_48dp)
                        .withMaxLabels(6)
                        .withShowCross(true)
                        .withTextColor(android.R.color.white)
                        .withTextSize(R.dimen.label_title_size)
                        .build();

        autoLabel.setSettings(autoLabelUISettings);
        autoLabel.addLabel("无材料");
        autoLabel.addLabel("预约");
    }

    @OnClick(R.id.btn_add)
    public void add(){

        autoLabel.addLabel(EditTextUtils.getValue(etReason));
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

    @OnClick(R.id.expand_container)
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
