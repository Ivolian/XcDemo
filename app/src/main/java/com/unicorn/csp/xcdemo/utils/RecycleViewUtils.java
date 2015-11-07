package com.unicorn.csp.xcdemo.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class RecycleViewUtils {

    public static LinearLayoutManager getLinearLayoutManager(Context context) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

}
