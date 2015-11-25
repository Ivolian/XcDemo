package com.unicorn.csp.xcdemo.fragment.technician;

import com.unicorn.csp.xcdemo.adaper.recycleview.technician.RefreshAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.WorkOrderReceivedAdapter;


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

}
