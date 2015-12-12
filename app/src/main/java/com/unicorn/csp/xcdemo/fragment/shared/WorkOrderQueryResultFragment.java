package com.unicorn.csp.xcdemo.fragment.shared;

import com.unicorn.csp.xcdemo.adaper.recycleview.shared.QueryResultAdapter;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.utils.GsonUtils;


public class WorkOrderQueryResultFragment extends RefreshFragment {

    @Override
    public RefreshAdapter getAdapter() {
        return new QueryResultAdapter();
    }

    @Override
    public String getLatterPartUrl() {
        return null;
    }

    @Override
    public int getFragmentIndex() {
        return -1;
    }

    @Override
    public Object parseDataList(String jsonArrayString) {
        return GsonUtils.parseWorkOrderInfoList(jsonArrayString);
    }

    @Override
    public String getCompleteUrl(Integer pageNo) {
        String queryUrl = getArguments().getString("queryUrl");
        queryUrl += ("&pageNo" + "=" + pageNo.toString());
        queryUrl += ("&pageSize" + "=" + PAGE_SIZE.toString());
        return queryUrl;
    }

}
