package com.unicorn.csp.xcdemo.fragment.technician;

import com.unicorn.csp.xcdemo.adaper.recycleview.technician.AlreadyReceiveAdapter;
import com.unicorn.csp.xcdemo.fragment.base.BasicRefreshRecycleFragment;
import com.unicorn.csp.xcdemo.model.Model;
import com.unicorn.csp.xcdemo.utils.RecycleViewUtils;

import java.util.ArrayList;
import java.util.List;


public class AlreadyReceiveFragment extends BasicRefreshRecycleFragment {

     AlreadyReceiveAdapter adapter;

    public void initRecyclerView() {
        recyclerView.setLayoutManager(RecycleViewUtils.getLinearLayoutManager(getActivity()));
        adapter = new AlreadyReceiveAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setModelList(getData());
        adapter.notifyDataSetChanged();
    }

    private List<Model> getData() {

        List<Model> modelList = new ArrayList<>();
        for (int i = 0; i != 20; i++) {
            modelList.add(new Model());
        }
        return modelList;
    }

}