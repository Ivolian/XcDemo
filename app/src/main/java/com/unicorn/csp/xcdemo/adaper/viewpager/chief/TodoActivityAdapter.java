package com.unicorn.csp.xcdemo.adaper.viewpager.chief;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.xcdemo.fragment.chief.TodoFragment;


// @P
public class TodoActivityAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {
            "全部", "指定", "复核", "挂单",
    };

    public TodoActivityAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return  new TodoFragment();
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
