package com.unicorn.csp.xcdemo.fragment.chief;

import com.unicorn.csp.xcdemo.adaper.recycleview.chief.WorkOrderToReviewAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.RefreshAdapter;
import com.unicorn.csp.xcdemo.fragment.technician.RefreshFragment;


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
        return 0;
    }

}
