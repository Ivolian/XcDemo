package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;
import java.util.List;


public class WorkOrderInfo implements Serializable {

    private String workOrderId;

    private String type;

    private String requestDepartment;

    private long requestTime;

    private String requestUser;

    private String requestUserNo;

    private String callNumber;

    private String building;

    private String equipment;

    private String faultType;

    private String processingMode;

    private String processingTimeLimit;

    private String emergencyDegree;

    private String emergencyDegreeTag;

    private String address;

    private String description;

    private String status;

    private String statusTag;

    // 受理人
    private String issuer;

    // 受理时间
    private long issueTime;

    // 派单人
    private String distributor;

    // 派单时间
    private long distributeTime;

    // 接单人
    private String receiver;

    // 接单人电话
    private String receiverPhone;

    // 接单时间
    private long receiveTime;

    // 到达时间
    private long arriveTime;

    // 挂单时间
    private long hangUpTime;

    // 结单时间
    private long completeTime;

    // 复核人
    private String confirm;

    // 复核时间
    private long confirmTime;

    // 领料列表
    private List<WorkOrderSupplyInfo> supplyList;

    private String labelText;

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }

    public long getHangUpTime() {
        return hangUpTime;
    }

    public void setHangUpTime(long hangUpTime) {
        this.hangUpTime = hangUpTime;
    }

    public long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(long completeTime) {
        this.completeTime = completeTime;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public long getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(long confirmTime) {
        this.confirmTime = confirmTime;
    }

    public List<WorkOrderSupplyInfo> getSupplyList() {
        return supplyList;
    }

    public void setSupplyList(List<WorkOrderSupplyInfo> supplyList) {
        this.supplyList = supplyList;
    }

}
