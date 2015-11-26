package com.unicorn.csp.xcdemo.fragment.technician;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.technician.TechnicianMainActivity;
import com.unicorn.csp.xcdemo.adaper.viewpager.technician.WorkListPagerAdapter;
import com.unicorn.csp.xcdemo.component.TabHelper;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;
import com.unicorn.csp.xcdemo.model.RefreshResult;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import su.levenetc.android.badgeview.BadgeView;

public class WorkListFragment extends ButterKnifeFragment {


    // ================================== getLayoutResId ==================================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_work_list;
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
        viewPager.setOffscreenPageLimit(WorkListPagerAdapter.titles.length);
        viewPager.setAdapter(new WorkListPagerAdapter(getActivity().getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i != WorkListPagerAdapter.titles.length; i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    int textColor = getResources().getColor(position == i ? R.color.tab_selected_color : R.color.tab_color);
                    TabHelper.getTextView(tab).setTextColor(textColor);
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
        for (int i = 0; i != WorkListPagerAdapter.titles.length; i++) {
            String title = WorkListPagerAdapter.titles[i];
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab);
            }
            TabHelper.getTextView(tab).setText(title);
        }
    }


    // ================================== search ==================================

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_work_list, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setIcon(getSearchIconDrawable());
        initSearchView(menuItem);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initSearchView(MenuItem menuItem) {
        TechnicianMainActivity technicianMainActivity = (TechnicianMainActivity) getActivity();
        MaterialSearchView searchView = technicianMainActivity.searchView;
        searchView.setMenuItem(menuItem);
        searchView.setHint("请输入查询内容");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private Drawable getSearchIconDrawable() {
        return new IconicsDrawable(getActivity())
                .icon(GoogleMaterial.Icon.gmd_search)
                .color(Color.WHITE)
                .sizeDp(18);
    }


    // ================================== onRefreshFinish ==================================

    @Bind(R.id.fl_snack_bar_container)
    FrameLayout flSnackBarContainer;

    @Subscriber(tag = "workListFragment_onRefreshFinish")
    private void onRefreshFinish(RefreshResult refreshResult) {
        BadgeView badgeView = TabHelper.getBadgeView(tabLayout.getTabAt(refreshResult.getTabIndex()));
        badgeView.setValue(refreshResult.getTotal(), true);
        showSnackBar("共 " + refreshResult.getTotal() + " 条记录");
    }

    private void showSnackBar(String text) {
        SnackbarManager.show(Snackbar.with(getActivity())
                .position(Snackbar.SnackbarPosition.TOP)
                .color(getResources().getColor(R.color.blue))
                .textColor(getResources().getColor(R.color.white))
                .text(text)
                .duration(800), flSnackBarContainer);
    }

}
