package com.unicorn.csp.xcdemo.adaper.viewpager.technician;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.unicorn.csp.xcdemo.fragment.technician.AlreadyAchieveFragment;
import com.unicorn.csp.xcdemo.fragment.technician.AlreadyReceiveFragment;
import com.unicorn.csp.xcdemo.fragment.technician.AlreadySuspendFragment;
import com.unicorn.csp.xcdemo.fragment.technician.WaitReceiveFragment;


// @P
public class WorkOrderActivityAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {
            "待接单", "已接单", "已挂起", "已结单",
    };

    public WorkOrderActivityAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new WaitReceiveFragment();
            case 1:
                return new AlreadyReceiveFragment();
            case 2:
                return new AlreadySuspendFragment();
            case 3:
                return new AlreadyAchieveFragment();
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
