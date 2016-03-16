package com.unicorn.csp.xcdemo.activity.shared.summary.model;

import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class DayData {

    private Series series;

    private List<String> categories;

    //

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
