package com.unicorn.csp.xcdemo.adaper.viewpager.shared;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.xcdemo.equipment.CostFragment;
import com.unicorn.csp.xcdemo.equipment.PrincipalFragment;
import com.unicorn.csp.xcdemo.equipment.SummaryFragment;
import com.unicorn.csp.xcdemo.fragment.technician.WorkOrderSuspendedFragment;


// @PP
public class EquipmentInfoPagerAdapter extends FragmentStatePagerAdapter {

    public static String[] titles = {
            "概要", "型号规格", "备件", "维修记录",
            "保养记录", "成本", "负责人"
    };

    public EquipmentInfoPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new SummaryFragment();
            case 5:
                return new CostFragment();
            case 6:
                return new PrincipalFragment();
        }

        return new WorkOrderSuspendedFragment();
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}
