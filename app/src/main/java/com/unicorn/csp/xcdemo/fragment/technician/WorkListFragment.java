package com.unicorn.csp.xcdemo.fragment.technician;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.technician.TechnicianMainActivity;
import com.unicorn.csp.xcdemo.adaper.viewpager.technician.WorkListPagerAdapter;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;

import org.simple.eventbus.EventBus;

import butterknife.Bind;
import su.levenetc.android.badgeview.BadgeView;

public class WorkListFragment extends ButterKnifeFragment {


    @Override
    public void onCreateOptionsMenu(android.view.Menu menu,MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setIcon(getSearchIconDrawable());
        initSearchView(menuItem);
         super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void initSearchView(MenuItem menuItem) {
        TechnicianMainActivity technicianMainActivity  = (TechnicianMainActivity)getActivity();
        MaterialSearchView searchView = technicianMainActivity.searchView;
        searchView.setMenuItem(menuItem);
        searchView.setHint("请输入查询内容");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                EventBus.getDefault().post("some word", "search");
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setHasOptionsMenu(true);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_work_list;
    }

    @Override
    public void initViews() {
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new WorkListPagerAdapter(getActivity().getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
               final TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setCustomView(R.layout.test);
//tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BadgeView badgeView = (BadgeView) tab.getCustomView().findViewById(R.id.badgeView);
                badgeView.setValue(100, true);
            }
        }, 1000);
    }



    // ================================== views ==================================

    @Bind(R.id.tabs_layout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // ================================== 下拉刷新提示语 ==================================

//    @Subscriber(tag = "onFragmentRefreshFinish")
//    private void onFragmentRefreshFinish(String text) {
//        showSnackBar(text);
//    }
//
//    private void showSnackBar(String text) {
//        SnackbarManager.show(Snackbar.with(this)
//                        .position(Snackbar.SnackbarPosition.TOP)
//                        .color(getResources().getColor(R.color.blue))
//                        .textColor(getResources().getColor(R.color.white))
//                        .text(text)
//                        .duration(800),
//                (android.view.ViewGroup) findViewById(R.id.fl_for_snack_bar));
//    }

}
