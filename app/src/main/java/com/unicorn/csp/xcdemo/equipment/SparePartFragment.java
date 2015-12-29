package com.unicorn.csp.xcdemo.equipment;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.SparePartAdapter;
import com.unicorn.csp.xcdemo.fragment.shared.base.LazyLoadFragment;
import com.unicorn.csp.xcdemo.utils.RecycleViewUtils;

import java.util.Arrays;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;

public class SparePartFragment extends LazyLoadFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_refresh;
    }

    @Bind(R.id.recyclerView)
    ExRecyclerView recyclerView;

    @Override
    public void initViews() {

        SparePartAdapter sparePartAdapter = new SparePartAdapter();
        sparePartAdapter.setDataList(Arrays.asList("","","","","","",""));

        recyclerView.setLayoutManager(RecycleViewUtils.getLinearLayoutManager(getActivity()));

        recyclerView.setAdapter(sparePartAdapter);

    }


}
