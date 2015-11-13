package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;


public class WorkOrderInfo implements Serializable {

    private String workOrderId;

    private String type;

    private String requestDepartment;

    private long requestTime;

    private String requestUser;

    private String requestUserNo;

    private String callNumber;

    private String building; //(a)

    private String equipment;//(f)

    private String faultType;

    private String processingMode;

    private String processingTimeLimit;

    private String emergencyDegree;

    private String emergencyDegreeTag;

    private String address;

    private String description;

    private String status;

    private String statusTag;

    private String issuer;

    private long issueTime;

    private String distributor;

    private long distributeTime;

    //

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequestDepartment() {
        return requestDepartment;
    }

    public void setRequestDepartment(String requestDepartment) {
        this.requestDepartment = requestDepartment;
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

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getFaultType() {
        return faultType;
    }

    public void setFaultType(String faultType) {
        this.faultType = faultType;
    }

    public String getProcessingMode() {
        return processingMode;
    }

    public void setProcessingMode(String processingMode) {
        this.processingMode = processingMode;
    }

    public String getProcessingTimeLimit() {
        return processingTimeLimit;
    }

    public void setProcessingTimeLimit(String processingTimeLimit) {
        this.processingTimeLimit = processingTimeLimit;
    }

    public String getEmergencyDegree() {
        return emergencyDegree;
    }

    public void setEmergencyDegree(String emergencyDegree) {
        this.emergencyDegree = emergencyDegree;
    }

    public String getEmergencyDegreeTag() {
        return emergencyDegreeTag;
    }

    public void setEmergencyDegreeTag(String emergencyDegreeTag) {
        this.emergencyDegreeTag = emergencyDegreeTag;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public long getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(long issueTime) {
        this.issueTime = issueTime;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public long getDistributeTime() {
        return distributeTime;
    }

    public void setDistributeTime(long distributeTime) {
        this.distributeTime = distributeTime;
    }
}
