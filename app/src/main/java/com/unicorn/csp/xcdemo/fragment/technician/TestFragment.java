package com.unicorn.csp.xcdemo.fragment.technician;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.unicorn.csp.xcdemo.R;
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

    @Bind(R.id.expandableLayout)
    ExpandableRelativeLayout expandableRelativeLayout;

    @OnClick(R.id.tv_request_user_and_call_number)
    public void func(){
        expandableRelativeLayout.toggle();
    }



}
