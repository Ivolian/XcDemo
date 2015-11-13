package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;


public class Material implements Serializable{

    private String objectId;

    private String name;

    //

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
