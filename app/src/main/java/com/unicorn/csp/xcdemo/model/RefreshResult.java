package com.unicorn.csp.xcdemo.model;

import java.io.Serializable;


public class RefreshResult implements Serializable {

    private int tabIndex;

    private int total;

    //

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
