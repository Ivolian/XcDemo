package com.unicorn.csp.xcdemo.model;


import java.io.Serializable;

public class WorkOrderProcess implements Serializable {

    private User operator;

    private Code operation;

    private long eventTime;

    private WorkOrderReview workOrderReview;

    //


    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
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
}
