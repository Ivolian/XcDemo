package com.unicorn.csp.xcdemo.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.adaper.recycleview.CustomAdapter;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;
import com.unicorn.csp.xcdemo.model.Model;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;
import kale.recycler.OnRecyclerViewScrollListener;


public class TestFragment extends ButterKnifeFragment {


    @Bind(R.id.recyclerView)
    ExRecyclerView recyclerView;

    CustomAdapter customAdapter;


    @Override
    public int getLayoutResId() {

        return R.layout.fragment_test;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  super.onCreateView(inflater, container, savedInstanceState);
        initRecyclerView();
        return rootView;
    }

    private void initRecyclerView() {

        recyclerView.setLayoutManager(getLinearLayoutManager());
        customAdapter = new CustomAdapter(getData());
        recyclerView.setAdapter(customAdapter);
        recyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {

                customAdapter.addModelList(getData());
                customAdapter.notifyDataSetChanged();
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
