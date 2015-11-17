package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/17.
 */
public class WorkOrderSupplyInfo implements Serializable {

    private String material;

    private Integer amount;

    //

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
