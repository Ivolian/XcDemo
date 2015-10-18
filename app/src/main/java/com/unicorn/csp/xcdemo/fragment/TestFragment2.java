package com.unicorn.csp.xcdemo.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.adaper.recycleview.CustomAdapter2;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;
import com.unicorn.csp.xcdemo.model.Model;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;
import kale.recycler.OnRecyclerViewScrollListener;


public class TestFragment2 extends ButterKnifeFragment {

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    ExRecyclerView recyclerView;

    CustomAdapter2 customAdapter2;


    @Override
    public int getLayoutResId() {

        return R.layout.fragment_test;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initSwipeRefreshLayout();
        initRecyclerView();
        return rootView;
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

    private void initRecyclerView() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        recyclerView.setLayoutManager(getLinearLayoutManager());
        customAdapter2 = new CustomAdapter2(getActivity(), getData());
        recyclerView.setAdapter(customAdapter2);
        recyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {

                customAdapter2.addModelList(getData());
                customAdapter2.notifyDataSetChanged();
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
            modelList.add(new Model(i + ""));
        }
        return modelList;
    }

}
