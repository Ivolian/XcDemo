package com.unicorn.csp.xcdemo.adaper.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.xcdemo.fragment.TestFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {
            "待接单", "已接单", "已挂起", "已结单",
    };

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return new TestFragment();
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
