package com.unicorn.csp.xcdemo.activity.chief;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.unicorn.csp.xcdemo.adaper.viewpager.chief.TodoActivityAdapter;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;


// 移单，退单都会进待办工单。
//@P
public class TodoActivity extends ToolbarActivity {


    // ================================== views ==================================

    @Bind(R.id.tabs_layout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        initToolbar("待办工单", false);
        initViews(savedInstanceState);
    }

    public void initViews(Bundle savedInstanceState) {

        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new TodoActivityAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        initDrawer(savedInstanceState);
    }


    // ================================== drawer ==================================

    Drawer drawer;

    private void initDrawer(Bundle savedInstanceState) {

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(getToolbar())
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(false)
                .withHeader(R.layout.drawer_header)
                .withHeaderDivider(false)
                .addDrawerItems(getDrawerItems())
//                .withOnDrawerItemClickListener(getOnDrawItemClickListener())
                .withSavedInstance(savedInstanceState)
                .build();
    }

    private IDrawerItem[] getDrawerItems() {

        return new IDrawerItem[]{
                new PrimaryDrawerItem().withName("待办工单").withIcon(GoogleMaterial.Icon.gmd_assignment),
                new PrimaryDrawerItem().withName("工单监控").withIcon(GoogleMaterial.Icon.gmd_assignment_check),
                new PrimaryDrawerItem().withName("工单预警").withIcon(GoogleMaterial.Icon.gmd_notifications_active)
        };
    }


    // ================================== onSaveInstanceState ==================================

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState = drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    // ================================== searchView & menu ==================================

    @Bind(R.id.search_view)
    MaterialSearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setIcon(getSearchDrawable());
        initSearchView(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

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

    private Drawable getSearchDrawable() {

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


    //

    @Subscriber(tag = "onFragmentRefreshFinish")
    private void onFragmentRefreshFinish(String text) {

        showSnackBar(text);
    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void showSnackBar(String text) {

        SnackbarManager.show(
                Snackbar.with(this)
                        .position(Snackbar.SnackbarPosition.TOP)
                        .color(getResources().getColor(R.color.blue))
                        .textColor(getResources().getColor(R.color.white))
                        .text(text)
                        .duration(800)
                , (android.view.ViewGroup) findViewById(R.id.fl_for_snack_bar));
    }
}
