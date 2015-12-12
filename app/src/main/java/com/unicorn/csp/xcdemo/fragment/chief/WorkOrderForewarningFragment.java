package com.unicorn.csp.xcdemo.fragment.chief;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.adaper.viewpager.chief.MyWorkFragmentPagerAdapter;
import com.unicorn.csp.xcdemo.component.TabHelper;
import com.unicorn.csp.xcdemo.fragment.shared.base.ButterKnifeFragment;
import com.unicorn.csp.xcdemo.model.RefreshResult;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import su.levenetc.android.badgeview.BadgeView;

public class WorkOrderForewarningFragment extends ButterKnifeFragment {


    // ================================== getLayoutResId ==================================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_forewarning;
    }


    // ================================== views ==================================

    @Bind(R.id.tabs_layout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // ================================== onCreate & onDestroy ==================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    // ================================== initViews ==================================

    @Override
    public void initViews() {
        initViewPager();
        initTabLayout();
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(MyWorkFragmentPagerAdapter.titles.length);
        viewPager.setAdapter(new MyWorkFragmentPagerAdapter(getActivity().getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i != MyWorkFragmentPagerAdapter.titles.length; i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    int textColor = getResources().getColor(position == i ? R.color.tab_selected_color : R.color.tab_color);
                    TabHelper.getTitleView(tab).setTextColor(textColor);
                    TabHelper.getBadgeView(tab).setTextColor(textColor);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i != MyWorkFragmentPagerAdapter.titles.length; i++) {
            String title = MyWorkFragmentPagerAdapter.titles[i];
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab);
            }
            TabHelper.getTitleView(tab).setText(title);
        }
    }

    // ================================== onRefreshFinish ==================================

    @Bind(R.id.fl_snack_bar_container)
    FrameLayout flSnackBarContainer;

    // todo
    @Subscriber(tag = "workOrderForewarningFragment_onRefreshFinish")
    private void onRefreshFinish(RefreshResult refreshResult) {
        BadgeView badgeView = TabHelper.getBadgeView(tabLayout.getTabAt(refreshResult.getTabIndex()));
        badgeView.setValue(refreshResult.getTotal(), true);
        showSnackBar("共 " + refreshResult.getTotal() + " 条记录");
    }

    private void showSnackBar(String text) {
        SnackbarManager.show(Snackbar.with(getActivity())
                .position(Snackbar.SnackbarPosition.TOP)
                .color(getResources().getColor(R.color.snack_bar_color))
                .textColor(getResources().getColor(R.color.white))
                .text(text)
                .duration(800), flSnackBarContainer);
    }

}
