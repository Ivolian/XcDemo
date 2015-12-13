package com.unicorn.csp.xcdemo.adaper.viewpager.chief;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.xcdemo.fragment.chief.RedForewarningFragment;
import com.unicorn.csp.xcdemo.fragment.chief.YellowForewarningFragment;


public class WorkOrderForewarningFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public static String[] titles = {"红色预警", "黄色预警"};

    public WorkOrderForewarningFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RedForewarningFragment();
            case 1:
                return new YellowForewarningFragment();
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
