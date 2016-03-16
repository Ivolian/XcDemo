package com.unicorn.csp.xcdemo.activity.shared.summary.model;

/**
 * Created by Administrator on 2016/3/16.
 */
public class WorkOrderStatus {

    private int Issue;

    private int Complete;

    private int Distribute;

    //

    public int getIssue() {
        return Issue;
    }

    public void setIssue(int issue) {
        Issue = issue;
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
