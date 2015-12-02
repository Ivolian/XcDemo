package com.unicorn.csp.xcdemo.fragment.technician;

import android.os.Bundle;

import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.WorkOrderToReceiveAdapter;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;


public class WorkOrderToReceiveFragment extends RefreshFragment {

    @Override
    public RefreshAdapter getAdapter() {
        return new WorkOrderToReceiveAdapter();
    }

    @Override
    public String getLatterPartUrl() {
        return "/api/v1/hems/workOrder/issue?";
    }

    @Override
    public int getFragmentIndex() {
        return 0;
    }

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

    @Subscriber(tag = "workOrderToReceiveFragment_refresh")
    private void refresh(Object object) {
        reload();
    }

}
