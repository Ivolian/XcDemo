package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/17.
 */
public class Code implements Serializable {

    private String name;
    private String tag;
    private String description;
    private Integer orderNo;

    //


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}
