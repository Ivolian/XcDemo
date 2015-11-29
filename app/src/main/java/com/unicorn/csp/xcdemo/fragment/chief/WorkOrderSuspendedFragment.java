package com.unicorn.csp.xcdemo.fragment.chief;

import com.unicorn.csp.xcdemo.adaper.recycleview.chief.WorkOrderSuspendedAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.RefreshAdapter;
import com.unicorn.csp.xcdemo.fragment.technician.RefreshFragment;


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
}
