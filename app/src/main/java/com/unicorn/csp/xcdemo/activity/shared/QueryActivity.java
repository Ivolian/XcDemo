package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.fragment.technician.WorkOrderSuspendedFragment;


public class QueryActivity extends ToolbarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        initToolbar("查询2", true);
        WorkOrderSuspendedFragment workOrderSuspendedFragment = new WorkOrderSuspendedFragment();
        replaceFragment_(workOrderSuspendedFragment);
        workOrderSuspendedFragment.initPrepare();
    }

    public void replaceFragment_(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.ll_container, fragment).commit();
    }


}
