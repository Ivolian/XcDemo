package com.unicorn.csp.xcdemo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.adaper.pager.ViewPagerAdapter;

import butterknife.Bind;
import io.karim.MaterialTabs;


public class MainActivity extends ToolbarActivity {

    @Bind(R.id.tabs)
    MaterialTabs tabs;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar("工作清单", false);
        initViews();
    }

    public void initViews() {

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
    }

}
