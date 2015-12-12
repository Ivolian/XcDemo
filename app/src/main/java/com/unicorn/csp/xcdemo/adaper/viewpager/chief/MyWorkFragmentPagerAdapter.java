package com.unicorn.csp.xcdemo.adaper.viewpager.chief;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.xcdemo.fragment.chief.WorkOrderAssignedFragment;
import com.unicorn.csp.xcdemo.fragment.chief.WorkOrderSuspendedFragment;
import com.unicorn.csp.xcdemo.fragment.chief.WorkOrderToReviewFragment;


public class MyWorkFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public static String[] titles = {
            "待指派", "待复核", "已挂起",
    };

    public MyWorkFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new WorkOrderAssignedFragment();
            case 1:
                return new WorkOrderToReviewFragment();
            case 2:
                return new WorkOrderSuspendedFragment();
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
