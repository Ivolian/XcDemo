package com.unicorn.csp.xcdemo.fragment.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorn.csp.xcdemo.R;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;


//@P
public abstract class BasicRefreshRecycleFragment extends ButterKnifeFragment {


    // ================================== views ==================================

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    public ExRecyclerView recyclerView;


    // ================================== getLayoutResId ==================================

    @Override
    public int getLayoutResId() {

        return R.layout.fragment_basic_refresh_recycle;
    }


    // ================================== onCreateView ==================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initSwipeRefreshLayout();
        initRecyclerView();
        initOtherViews();
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
                        onRefreshFinish();
                    }
                }, 1500);
            }
        });
    }


    // ================================== methods to override ==================================

    public void onRefreshFinish(){

    }

    public void initRecyclerView(){

    }

    public void initOtherViews(){

    }


}
