package com.unicorn.csp.xcdemo.model;


import java.io.Serializable;

public class WorkOrderProcess implements Serializable {

    private String operatorName;

    private Code operation;

    private long eventTime;

    private WorkOrderReview workOrderReview;

    private WorkOrderHangUp workOrderHangUp;


    //


    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Code getOperation() {
        return operation;
    }

    public void setOperation(Code operation) {
        this.operation = operation;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public WorkOrderReview getWorkOrderReview() {
        return workOrderReview;
    }

    public void setWorkOrderReview(WorkOrderReview workOrderReview) {
        this.workOrderReview = workOrderReview;
    }

    public WorkOrderHangUp getWorkOrderHangUp() {
        return workOrderHangUp;
    }

    public void setWorkOrderHangUp(WorkOrderHangUp workOrderHangUp) {
        this.workOrderHangUp = workOrderHangUp;
    }


}
