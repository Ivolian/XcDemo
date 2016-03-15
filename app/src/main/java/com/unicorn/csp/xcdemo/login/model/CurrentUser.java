package com.unicorn.csp.xcdemo.login.model;


import java.util.List;

public class CurrentUser {

    private String objectId;

    private List<WorkType> workTypeList;

    private Role role;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public List<WorkType> getWorkTypeList() {
        return workTypeList;
    }

    public void setWorkTypeList(List<WorkType> workTypeList) {
        this.workTypeList = workTypeList;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

