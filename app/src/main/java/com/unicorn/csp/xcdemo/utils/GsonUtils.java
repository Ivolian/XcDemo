package com.unicorn.csp.xcdemo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;

import java.util.List;


public class GsonUtils {

    public static List<WorkOrderProcessInfo> parseWorkOrderProcessInfoList(String workOrderProcessInfoListString) {
        Gson gson = new Gson();
        List<WorkOrderProcessInfo> workOrderProcessInfoList = gson.
                fromJson(workOrderProcessInfoListString, new TypeToken<List<WorkOrderProcessInfo>>() {
                }.getType());
        return workOrderProcessInfoList;
    }

    public static List<WorkOrderInfo> parseWorkOrderInfoList(String workOrderInfoListString) {
        Gson gson = new Gson();
        List<WorkOrderInfo> workOrderInfoList = gson.
                fromJson(workOrderInfoListString, new TypeToken<List<WorkOrderInfo>>() {
                }.getType());
        return workOrderInfoList;
    }

}
