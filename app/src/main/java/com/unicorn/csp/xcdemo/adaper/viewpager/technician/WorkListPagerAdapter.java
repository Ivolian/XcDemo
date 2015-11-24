package com.unicorn.csp.xcdemo.adaper.viewpager.technician;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.xcdemo.fragment.technician.WorkOrderAchievedFragment;
import com.unicorn.csp.xcdemo.fragment.technician.WorkOrderReceivedFragment;
import com.unicorn.csp.xcdemo.fragment.technician.WorkOrderSuspendedFragment;
import com.unicorn.csp.xcdemo.fragment.technician.WorkOrderToReceiveFragment;


// @PP
public class WorkListPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {
            "待接单", "已接单", "已挂起", "已结单",
    };

    public WorkListPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return  new WorkOrderToReceiveFragment();
            case 1:
                return new WorkOrderReceivedFragment();
            case 2:
                return new WorkOrderSuspendedFragment();
            case 3:
                return new WorkOrderAchievedFragment();
        }
        return null;
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
