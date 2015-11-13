package com.unicorn.csp.xcdemo.model;


import java.io.Serializable;

public class WorkOrderSupply implements Serializable{

    private Material material;

    private Integer amount;

    //


    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

}
