package com.unicorn.csp.xcdemo.fragment.technician;

import android.os.Bundle;

import com.unicorn.csp.xcdemo.adaper.recycleview.technician.RefreshAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.WorkOrderSuspendedAdapter;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;


public class WorkOrderSuspendedFragment extends RefreshFragment {

    @Override
    public RefreshAdapter getAdapter() {
        return new WorkOrderSuspendedAdapter();
    }

    @Override
    public String getLatterPartUrl() {
        return "/api/v1/hems/workOrder/hangUp?";
    }


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


    @Subscriber(tag = "suspendRefresh")
    private void suspendRefresh(String text) {
        reload();
    }

}
