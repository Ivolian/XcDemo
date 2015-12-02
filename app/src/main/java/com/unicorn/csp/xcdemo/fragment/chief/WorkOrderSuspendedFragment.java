package com.unicorn.csp.xcdemo.fragment.chief;

import android.os.Bundle;

import com.unicorn.csp.xcdemo.adaper.recycleview.chief.WorkOrderSuspendedAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.fragment.technician.RefreshFragment;

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
    public int getFragmentIndex() {
        return 2;
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

    @Subscriber(tag = "chief_workOrderSuspendedFragment_refresh")
    private void refresh(Object object) {
        reload();
    }

}
