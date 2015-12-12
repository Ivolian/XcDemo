package com.unicorn.csp.xcdemo.fragment.technician;

import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.WorkOrderAchievedAdapter;
import com.unicorn.csp.xcdemo.fragment.shared.RefreshFragment;
import com.unicorn.csp.xcdemo.utils.GsonUtils;


public class WorkOrderAchievedFragment extends RefreshFragment {

    @Override
    public RefreshAdapter getAdapter() {
        return new WorkOrderAchievedAdapter();
    }

    @Override
    public String getLatterPartUrl() {
        return "/api/v1/hems/workOrder/complete?";
    }

    @Override
    public int getFragmentIndex() {
        return 3;
    }

    @Override
    public Object parseDataList(String jsonArrayString) {
        return GsonUtils.parseWorkOrderProcessInfoList(jsonArrayString);
    }

}
