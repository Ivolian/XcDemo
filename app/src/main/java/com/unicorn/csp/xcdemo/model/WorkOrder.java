package com.unicorn.csp.xcdemo.model;

import java.util.Date;


public class WorkOrder {

    private String objectId;

    private Date requestTime;       // 报修时间

    private String requestUser;     // 报修人

    private String callNumber;      // 报修人电话

    //

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

}
