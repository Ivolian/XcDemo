package com.unicorn.csp.xcdemo.adaper.recycleview.technician;

import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;

import java.util.List;


public interface RefreshAdapter {

    public void reload(List<WorkOrderProcessInfo> workOrderProcessInfoList);

    public void loadMore(List<WorkOrderProcessInfo> workOrderProcessInfoList);

}
