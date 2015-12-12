package com.unicorn.csp.xcdemo.component;

import android.support.design.widget.TabLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import su.levenetc.android.badgeview.BadgeView;


public class TabHelper {

    public static TextView getTitleView(TabLayout.Tab tab){
        LinearLayout linearLayout =  (LinearLayout) tab.getCustomView();
        return (TextView)linearLayout.getChildAt(0);
    }

    public static BadgeView getBadgeView(TabLayout.Tab tab){
        LinearLayout linearLayout =  (LinearLayout) tab.getCustomView();
        return (BadgeView)linearLayout.getChildAt(1);
    }

}
