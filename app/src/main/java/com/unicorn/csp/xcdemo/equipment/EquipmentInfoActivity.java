package com.unicorn.csp.xcdemo.equipment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.adaper.viewpager.shared.EquipmentInfoPagerAdapter;

import butterknife.Bind;


public class EquipmentInfoActivity extends ToolbarActivity {




    // ================================== views ==================================

    @Bind(R.id.tab_layout)
    public TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_info);
        initToolbar("设备信息",true);
        initViews();
        enableSlideFinish();
    }


    // ================================== initViews ==================================

    private void initViews() {
        EquipmentInfoPagerAdapter equipmentInfoPagerAdapter = new EquipmentInfoPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(equipmentInfoPagerAdapter);
        viewPager.setOffscreenPageLimit(equipmentInfoPagerAdapter.getCount());
        tabLayout.setupWithViewPager(viewPager);
    }

}
