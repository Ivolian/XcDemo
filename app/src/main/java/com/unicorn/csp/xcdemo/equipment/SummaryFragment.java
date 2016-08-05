package com.unicorn.csp.xcdemo.equipment;

import android.widget.TextView;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.fragment.shared.base.LazyLoadFragment;

import butterknife.BindView;


public class SummaryFragment extends LazyLoadFragment {

    @BindView(R.id.tv_equipment_id)
    TextView tvEquipmentId;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_equipment_summary;
    }

    @Override
    public void initViews() {
        String equipmentId = getActivity().getIntent().getStringExtra("equipmentId");
        tvEquipmentId.setText("序列号: " + equipmentId);
    }

}
