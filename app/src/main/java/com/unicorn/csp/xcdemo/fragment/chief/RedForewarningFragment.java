package com.unicorn.csp.xcdemo.fragment.chief;

import com.unicorn.csp.xcdemo.adaper.recycleview.chief.RedForewarningAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.fragment.shared.RefreshFragment;
import com.unicorn.csp.xcdemo.utils.GsonUtils;


public class RedForewarningFragment extends RefreshFragment {

    @Override
    public RefreshAdapter getAdapter() {
        return new RedForewarningAdapter();
    }

    @Override
    public String getLatterPartUrl() {
        return "/api/v1/hems/workOrderWarn?degree=1";
    }

    @Override
    public int getFragmentIndex() {
        return 0;
    }

    @Override
    public Object parseDataList(String jsonArrayString) {
        return GsonUtils.parseWorkOrderWarnList(jsonArrayString);
    }

    @Override
    public String getRefreshEventTag() {
        return "workOrderForewarningFragment_onRefreshFinish";
    }

}
