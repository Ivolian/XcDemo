package com.unicorn.csp.xcdemo.fragment.technician;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.component.WorkOrderFrameLayout;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;

import butterknife.Bind;
import butterknife.OnClick;


public class TestFragment extends ButterKnifeFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test;
    }

    @Override
    public void initViews() {
//        expandableRelativeLayout.setExpanded(false);
    }

    @Bind(R.id.work_order_frame_layout)
    WorkOrderFrameLayout workOrderFrameLayout;

    @OnClick(R.id.work_order_frame_layout)
    public void func(){
        workOrderFrameLayout.expandableLayout.toggle();
    }



}
