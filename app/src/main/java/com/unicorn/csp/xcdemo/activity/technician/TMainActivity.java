package com.unicorn.csp.xcdemo.activity.technician;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.adaper.viewpager.technician.WorkOrderActivityAdapter;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import su.levenetc.android.badgeview.BadgeView;


//@PP
public class TMainActivity extends ToolbarActivity {


    // ================================== views ==================================

    @Bind(R.id.tabs_layout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // ================================== onCreate & onDestroy ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        initToolbar("工作清单", false);
        initViews();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void initViews() {
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new WorkOrderActivityAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        initDrawer();

       final TabLayout.Tab tab = tabLayout.getTabAt(0);
        tab.setCustomView(R.layout.test);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                BadgeView badgeView = (BadgeView) tab.getCustomView().findViewById(R.id.badgeView);
                badgeView.setValue(100, true);
            }
        }, 1000);
    }


    // ================================== drawer ==================================

    Drawer drawer;

    private void initDrawer() {
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(getToolbar())
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(false)
                .withHeader(R.layout.drawer_header)
                .withHeaderDivider(false)
                .addDrawerItems(getDrawerItems())
                .build();
    }

    private IDrawerItem[] getDrawerItems() {
        return new IDrawerItem[]{
                new PrimaryDrawerItem().withName("工作清单").withIcon(GoogleMaterial.Icon.gmd_assignment),
                new PrimaryDrawerItem().withName("统计分析").withIcon(GoogleMaterial.Icon.gmd_chart),
                new PrimaryDrawerItem().withName("我的收益").withIcon(GoogleMaterial.Icon.gmd_assignment_account)
        };
    }


    // ================================== searchView & menu ==================================

    @Bind(R.id.search_view)
    MaterialSearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setIcon(getSearchIconDrawable());
        initSearchView(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }

    private void initSearchView(MenuItem menuItem) {

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
        return new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_search)
                .color(Color.WHITE)
                .sizeDp(18);
    }


    // ========================== 再按一次退出 ==========================

    long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (searchView != null && searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            if (drawer != null && drawer.isDrawerOpen()) {
                drawer.closeDrawer();
            } else {
                if (System.currentTimeMillis() - exitTime > 2000) {
                    ToastUtils.show("再按一次退出");
                    exitTime = System.currentTimeMillis();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }


    // ================================== 下拉刷新提示语 ==================================

    @Subscriber(tag = "onFragmentRefreshFinish")
    private void onFragmentRefreshFinish(String text) {
        showSnackBar(text);
    }

    private void showSnackBar(String text) {
        SnackbarManager.show(Snackbar.with(this)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .color(getResources().getColor(R.color.blue))
                        .textColor(getResources().getColor(R.color.white))
                        .text(text)
                        .duration(800),
                (android.view.ViewGroup) findViewById(R.id.fl_for_snack_bar));
    }

}
