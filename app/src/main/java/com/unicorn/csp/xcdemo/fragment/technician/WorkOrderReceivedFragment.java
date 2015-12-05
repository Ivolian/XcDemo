package com.unicorn.csp.xcdemo.fragment.technician;

import android.os.Bundle;

import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.WorkOrderReceivedAdapter;
import com.unicorn.csp.xcdemo.fragment.shared.RefreshFragment;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;


public class WorkOrderReceivedFragment extends RefreshFragment {

    @Override
    public RefreshAdapter getAdapter() {
        return new WorkOrderReceivedAdapter();
    }

    @Override
    public String getLatterPartUrl() {
        return "/api/v1/hems/workOrder/receive?";
    }

    @Override
    public int getFragmentIndex() {
        return 1;
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

    @Subscriber(tag = "workOrderReceivedFragment_refresh")
    private void refresh(Object object) {
        reload();
    }

}
