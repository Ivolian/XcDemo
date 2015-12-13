package com.unicorn.csp.xcdemo.fragment.chief;

import com.unicorn.csp.xcdemo.adaper.recycleview.chief.YellowForewarningAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.fragment.shared.RefreshFragment;
import com.unicorn.csp.xcdemo.utils.GsonUtils;


public class YellowForewarningFragment extends RefreshFragment {

    @Override
    public RefreshAdapter getAdapter() {
        return new YellowForewarningAdapter();
    }

    @Override
    public String getLatterPartUrl() {
        return "/api/v1/hems/workOrderWarn?degree=2";
    }

    @Override
    public int getFragmentIndex() {
        return 1;
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
