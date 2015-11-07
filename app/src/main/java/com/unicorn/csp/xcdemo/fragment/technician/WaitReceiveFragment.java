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


//@P
public class WaitReceiveFragment extends BasicRefreshRecycleFragment {


    // ================================== adapter ==================================

    WaitReceiveAdapter adapter;


    // ================================== override methods ==================================

    @Override
    public void initRecyclerView() {
        recyclerView.setLayoutManager(RecycleViewUtils.getLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter = new WaitReceiveAdapter());
        adapter.setModelList(getData());
        adapter.notifyDataSetChanged();
//        recyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
//            @Override
//            public void onScrollUp() {
//
//            }
//
//            @Override
//            public void onScrollDown() {
//
//            }
//
//            @Override
//            public void onBottom() {
//
////                adapter.addModelList(getData());
////                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onMoved(int i, int i1) {
//
//            }
//        });
    }

    @Override
    public void onRefreshFinish() {
            ToastUtils.show("刷新完毕");
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
    private void search(String str) {
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
