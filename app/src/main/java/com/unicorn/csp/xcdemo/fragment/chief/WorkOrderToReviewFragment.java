package com.unicorn.csp.xcdemo.fragment.chief;

import android.os.Bundle;

import com.unicorn.csp.xcdemo.adaper.recycleview.chief.WorkOrderToReviewAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.fragment.shared.RefreshFragment;
import com.unicorn.csp.xcdemo.utils.GsonUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;


public class WorkOrderToReviewFragment extends RefreshFragment {

    @Override
    public RefreshAdapter getAdapter() {
        return new WorkOrderToReviewAdapter();
    }


    @Override
    public String getLatterPartUrl() {
        return "/api/v1/hems/workOrder/review?";
    }

    @Override
    public int getFragmentIndex() {
        return 1;
    }

    @Override
    public Object parseDataList(String jsonArrayString) {
        return GsonUtils.parseWorkOrderProcessInfoList(jsonArrayString);
    }
    //

    //

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscriber(tag = "workOrderToReviewFragment_refresh")
    private void refresh(Object object) {
        reload();
    }

}
