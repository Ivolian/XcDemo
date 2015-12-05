package com.unicorn.csp.xcdemo.fragment.shared;

import android.content.Intent;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.TreeChooseActivity;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class QueryFragment extends ButterKnifeFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_query;
    }

    @Bind(R.id.et_work_order_type)
    MaterialEditText etWorkOrderType;

    @OnClick(R.id.et_work_order_type)
    public void chooseWorkOrderType() {
        Intent intent = new Intent(getActivity(), TreeChooseActivity.class);
        intent.putExtra("title", "选择工单类型");
        startActivity(intent);
    }

}
