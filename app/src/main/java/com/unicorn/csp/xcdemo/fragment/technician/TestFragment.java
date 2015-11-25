package com.unicorn.csp.xcdemo.fragment.technician;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.component.WorkOrderCard;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;

import butterknife.Bind;
import butterknife.OnClick;


public class TestFragment extends ButterKnifeFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test;
    }

    @Bind(R.id.word_order_card)
    WorkOrderCard workOrderCard;

    boolean expand = false;
    @OnClick(R.id.word_order_card)
    public void func(){
        if (expand) {
            workOrderCard.collapse();
        expand =false;
        }else {
            workOrderCard.expand();
            expand =true;
        }
        }


}
