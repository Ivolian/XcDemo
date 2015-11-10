package com.unicorn.csp.xcdemo.fragment.chief;

import com.unicorn.csp.xcdemo.adaper.recycleview.chief.TodoAdapter;
import com.unicorn.csp.xcdemo.fragment.base.BasicRefreshRecycleFragment;
import com.unicorn.csp.xcdemo.model.Model;
import com.unicorn.csp.xcdemo.utils.RecycleViewUtils;

import java.util.ArrayList;
import java.util.List;


public class TodoFragment extends BasicRefreshRecycleFragment {

    TodoAdapter adapter;

    public void initRecyclerView() {
        recyclerView.setLayoutManager(RecycleViewUtils.getLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new TodoAdapter());
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
