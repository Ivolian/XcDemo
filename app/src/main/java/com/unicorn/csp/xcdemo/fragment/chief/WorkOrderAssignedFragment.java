package com.unicorn.csp.xcdemo.fragment.chief;

import com.unicorn.csp.xcdemo.adaper.recycleview.chief.WorkOrderAssignedAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.RefreshAdapter;
import com.unicorn.csp.xcdemo.fragment.technician.RefreshFragment;


public class WorkOrderAssignedFragment extends RefreshFragment {

    @Override
    public RefreshAdapter getAdapter() {
        return new WorkOrderAssignedAdapter();
    }

    @Override
    public String getLatterPartUrl() {
        return "/api/v1/hems/workOrder/issue/timeout?";
    }

}
