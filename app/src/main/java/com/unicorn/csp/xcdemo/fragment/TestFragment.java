package com.unicorn.csp.xcdemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;


public class TestFragment extends ButterKnifeFragment {

    @Override
    public int getLayoutResId() {

        return R.layout.fragment_test;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
