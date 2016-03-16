package com.unicorn.csp.xcdemo.activity.shared.summary.model;

/**
 * Created by Administrator on 2016/3/16.
 */
public class SummaryData {

    private long AvgArriveTime;

    private WorkOrderStatus workOrderStatus;

    private long AvgCompleteTime;

    //

    public long getAvgArriveTime() {
        return AvgArriveTime;
    }

    public void setAvgArriveTime(long avgArriveTime) {
        AvgArriveTime = avgArriveTime;
    }

    public WorkOrderStatus getWorkOrderStatus() {
        return workOrderStatus;
    }

    public void setWorkOrderStatus(WorkOrderStatus workOrderStatus) {
        this.workOrderStatus = workOrderStatus;
    }

    public long getAvgCompleteTime() {
        return AvgCompleteTime;
    }

    public void setAvgCompleteTime(long avgCompleteTime) {
        AvgCompleteTime = avgCompleteTime;
    }

}
