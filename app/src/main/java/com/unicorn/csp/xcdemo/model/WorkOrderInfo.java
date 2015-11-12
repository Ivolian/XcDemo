package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;


public class WorkOrderInfo implements Serializable {

    private String objectId;

    private String workOrderId;

    private long requestTime;

    private String requestUser;

    private String requestUserNo;

    private String callNumber;

    private String status;

    private String statusTag;


    //

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public String getRequestUserNo() {
        return requestUserNo;
    }

    public void setRequestUserNo(String requestUserNo) {
        this.requestUserNo = requestUserNo;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusTag() {
        return statusTag;
    }

    public void setStatusTag(String statusTag) {
        this.statusTag = statusTag;
    }

}
