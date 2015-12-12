package com.unicorn.csp.xcdemo.fragment.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.component.TabHelper;

import org.simple.eventbus.EventBus;

import butterknife.Bind;


public abstract class TabLayoutFragment extends ButterKnifeFragment {


    // ================================== abstract method ==================================

    abstract public FragmentStatePagerAdapter getPagerAdapter();


    // ================================== getLayoutResId ==================================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_layout;
    }


    // ================================== views ==================================

    @Bind(R.id.tab_layout)
    public TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // ================================== onCreate & onDestroy ==================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setHasOptionsMenu(true);
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
        viewPager.setAdapter(getPagerAdapter());
        viewPager.setOffscreenPageLimit(getPagerAdapter().getCount());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 因为使用了 tabLayout 的 customView 所以默认的选中效果没了，得自己写下
                for (int i = 0; i != getPagerAdapter().getCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    int color = ContextCompat.getColor(getActivity(), position == i ? R.color.tab_selected_color : R.color.tab_color);
                    TabHelper.getTitleView(tab).setTextColor(color);
                    TabHelper.getBadgeView(tab).setTextColor(color);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabLayout() {
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i != getPagerAdapter().getCount(); i++) {
            String title = getPagerAdapter().getPageTitle(i).toString();
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab);
            }
            TabHelper.getTitleView(tab).setText(title);
        }
    }


    // ================================== showSnackBar ==================================

    @Bind(R.id.fl_snack_bar_container)
    FrameLayout flSnackBarContainer;

    public void showSnackBar(String text) {
        SnackbarManager.show(Snackbar.with(getActivity())
                .position(Snackbar.SnackbarPosition.TOP)
                .color(ContextCompat.getColor(getActivity(), R.color.snack_bar_color))
                .textColor(ContextCompat.getColor(getActivity(), R.color.white))
                .text(text)
                .duration(800), flSnackBarContainer);
    }

}
