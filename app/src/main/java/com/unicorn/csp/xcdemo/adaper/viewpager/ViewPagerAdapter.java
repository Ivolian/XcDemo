package com.unicorn.csp.xcdemo.adaper.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.xcdemo.MyApplication;
import com.unicorn.csp.xcdemo.fragment.DJDFragment;
import com.unicorn.csp.xcdemo.fragment.YGQFragment;
import com.unicorn.csp.xcdemo.fragment.YJDDFragment;
import com.unicorn.csp.xcdemo.fragment.YJDFragment;


public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {
            "待接单", "已接单", "已挂起", "已结单",
    };

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new DJDFragment();
            case 1:
                MyApplication.yjdFragment = new YJDFragment();
                return MyApplication.yjdFragment;
            case 2:
                MyApplication.ygqFragment = new YGQFragment();
                return MyApplication.ygqFragment;
            case 3:
                MyApplication.yjddFragment = new YJDDFragment();
                return MyApplication.yjddFragment;

            default:
                return null;
        }
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
