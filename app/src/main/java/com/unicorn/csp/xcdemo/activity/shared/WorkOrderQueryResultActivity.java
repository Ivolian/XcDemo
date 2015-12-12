package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.f2prateek.dart.InjectExtra;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.fragment.shared.WorkOrderQueryResultFragment;


public class WorkOrderQueryResultActivity extends ToolbarActivity {

    @InjectExtra("queryUrl")
    String queryUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_order_query_result);
        initToolbar("查询结果", true);
        initViews();
        enableSlideFinish();
    }

    private void initViews() {
        WorkOrderQueryResultFragment workOrderQueryResultFragment = new WorkOrderQueryResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString("queryUrl", queryUrl);
        workOrderQueryResultFragment.setArguments(bundle);
        replaceFragment_(workOrderQueryResultFragment);
        workOrderQueryResultFragment.initPrepare();
    }

    private void replaceFragment_(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

}
