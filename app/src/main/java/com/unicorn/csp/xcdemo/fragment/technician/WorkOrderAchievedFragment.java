package com.unicorn.csp.xcdemo.fragment.technician;

import com.unicorn.csp.xcdemo.adaper.recycleview.technician.RefreshAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.WorkOrderAchievedAdapter;


public class WorkOrderAchievedFragment extends RefreshFragment {

    @Override
    public RefreshAdapter getAdapter() {
        return new WorkOrderAchievedAdapter();
    }

    @Override
    public String getLatterPartUrl() {
        return "/api/v1/hems/workOrder/complete?";
    }

}