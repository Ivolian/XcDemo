package com.unicorn.csp.xcdemo.activity.shared.summary.model;

/**
 * Created by Administrator on 2016/3/16.
 */
public class WorkOrderStatus {

    // 下单
    private int Issue;

    // 接单/未到达
    private int Receive;

    // 工作中
    private int Arrive;

    // 需复核
    private int Review;

    // 已挂起
    private int HangUp;

    // 已结单
    private int Complete;

    // 指派了技师未接单
    private int Distribute;

    //

    public int getIssue() {
        return Issue;
    }

    public void setIssue(int issue) {
        Issue = issue;
    }

    public int getReceive() {
        return Receive;
    }

    public void setReceive(int receive) {
        Receive = receive;
    }

    public int getArrive() {
        return Arrive;
    }

    public void setArrive(int arrive) {
        Arrive = arrive;
    }

    public int getReview() {
        return Review;
    }

    public void setReview(int review) {
        Review = review;
    }

    public int getHangUp() {
        return HangUp;
    }

    public void setHangUp(int hangUp) {
        HangUp = hangUp;
    }

    public int getComplete() {
        return Complete;
    }

    public void setComplete(int complete) {
        Complete = complete;
    }

    public int getDistribute() {
        return Distribute;
    }

    public void setDistribute(int distribute) {
        Distribute = distribute;
    }
}
