package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;


public class WorkOrderProcessInfo implements Serializable {

    String objectId;

    WorkOrderInfo workOrderInfo;

    //

    public WorkOrderInfo getWorkOrderInfo() {
        return workOrderInfo;
    }

    public void setWorkOrderInfo(WorkOrderInfo workOrderInfo) {
        this.workOrderInfo = workOrderInfo;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

}
