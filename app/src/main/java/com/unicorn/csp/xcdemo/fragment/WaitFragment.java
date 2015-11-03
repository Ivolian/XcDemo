package com.unicorn.csp.xcdemo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.adaper.recycleview.WaitAdapter;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;
import com.unicorn.csp.xcdemo.model.Model;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;
import kale.recycler.OnRecyclerViewScrollListener;


public class WaitFragment extends ButterKnifeFragment {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    ExRecyclerView recyclerView;

    WaitAdapter adapter;

    @Override
    public int getLayoutResId() {

        return R.layout.fragment_basic_refresh_recycle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initSwipeRefreshLayout();
        initRecyclerView();
        return rootView;
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

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
    }


    @Subscriber(tag = "search")
    private void search(String ss) {
        ToastUtils.show("接收到来自主界面的消息");
    }

    private void initRecyclerView() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        recyclerView.setLayoutManager(getLinearLayoutManager());
        adapter = new WaitAdapter(getActivity(), getData());
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

                adapter.addModelList(getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onMoved(int i, int i1) {

            }
        });
    }

    public LinearLayoutManager getLinearLayoutManager() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        return linearLayoutManager;
    }

    private List<Model> getData() {

        List<Model> modelList = new ArrayList<>();
        for (int i = 0; i != 10; i++) {
            modelList.add(new Model());
        }
        return modelList;
    }

}
