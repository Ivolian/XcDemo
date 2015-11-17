package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/17.
 */
public class User implements Serializable {

    private String name;

    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
