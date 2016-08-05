package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;


public class WorkOrderProcessInfo implements Serializable {

    boolean expand = false;

    String objectId;

    WorkOrderInfo workOrderInfo;

    WorkOrderProcess workOrderProcess;

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


    public WorkOrderProcess getWorkOrderProcess() {
        return workOrderProcess;
    }

    public void setWorkOrderProcess(WorkOrderProcess workOrderProcess) {
        this.workOrderProcess = workOrderProcess;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

}
