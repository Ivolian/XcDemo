package com.unicorn.csp.xcdemo.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderWarn;

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


    public static List<WorkOrderWarn> parseWorkOrderWarnList(String workOrderWarnListString) {
        Gson gson = new Gson();
        List<WorkOrderWarn> workOrderWarnList = gson.
                fromJson(workOrderWarnListString, new TypeToken<List<WorkOrderWarn>>() {
                }.getType());
        return workOrderWarnList;
    }

}
