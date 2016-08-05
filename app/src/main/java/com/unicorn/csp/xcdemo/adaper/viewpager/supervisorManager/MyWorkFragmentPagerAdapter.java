package com.unicorn.csp.xcdemo.adaper.viewpager.supervisorManager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.xcdemo.fragment.supervisorManager.WorkOrderSuspendedFragment;


public class MyWorkFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public static String[] titles = {
            "挂单列表"
    };

    public MyWorkFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
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
