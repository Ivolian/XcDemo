package com.unicorn.csp.xcdemo.fragment.technician;

import android.os.Bundle;

import com.unicorn.csp.xcdemo.adaper.recycleview.technician.WaitReceiveAdapter;
import com.unicorn.csp.xcdemo.fragment.base.BasicRefreshRecycleFragment;
import com.unicorn.csp.xcdemo.model.Model;
import com.unicorn.csp.xcdemo.utils.RecycleViewUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import kale.recycler.OnRecyclerViewScrollListener;


public class WaitReceiveFragment extends BasicRefreshRecycleFragment {


    WaitReceiveAdapter adapter;

    public void initRecyclerView() {
        recyclerView.setLayoutManager(RecycleViewUtils.getLinearLayoutManager(getActivity()));
        adapter = new WaitReceiveAdapter( getData());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {

//                adapter.addModelList(getData());
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onMoved(int i, int i1) {

            }
        });
    }


    private List<Model> getData() {

        List<Model> modelList = new ArrayList<>();
        for (int i = 0; i != 20; i++) {
            modelList.add(new Model());
        }
        return modelList;
    }



    // ================================== EventBus ==================================

    @Subscriber(tag = "search")
    private void search(String ss) {
        ToastUtils.show("接收到来自主界面的消息");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


}
