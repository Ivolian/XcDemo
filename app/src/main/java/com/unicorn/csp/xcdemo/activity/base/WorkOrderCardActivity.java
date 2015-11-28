package com.unicorn.csp.xcdemo.activity.base;


import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.component.WorkOrderFrameLayout;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;

import butterknife.Bind;
import butterknife.OnClick;

abstract public class WorkOrderCardActivity extends ToolbarActivity{

    public static final String SHARED_VIEW = "WordOrderCard";

    @InjectExtra("workOrderProcessInfo")
    public WorkOrderProcessInfo workOrderProcessInfo;

    @Bind(R.id.work_order_card)
    WorkOrderFrameLayout workOrderCard;

    @OnClick(R.id.work_order_card)
    public void toggle(){
        workOrderCard.expandableLayout.toggle();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResId) {
        super.setContentView(layoutResId);
        workOrderCard.setWorkOrderInfo(workOrderProcessInfo.getWorkOrderInfo());
        ViewCompat.setTransitionName(workOrderCard, SHARED_VIEW);
    }

}