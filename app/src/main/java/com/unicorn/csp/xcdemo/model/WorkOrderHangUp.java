package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;

/**
 * Created by ivolianer on 2016/7/22.
 */
public class WorkOrderHangUp implements Serializable {

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
