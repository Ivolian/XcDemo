package com.unicorn.csp.xcdemo.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.adaper.viewpager.ViewPagerAdapter;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import butterknife.Bind;
import io.karim.MaterialTabs;


public class MainActivity extends ToolbarActivity {

    @Bind(R.id.tabs)
    MaterialTabs tabs;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    Drawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar("工作清单", false);
        initViews();
    }

    public void initViews() {

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
        initDrawer();
    }

    private void initDrawer() {

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withToolbar(getToolbar())
                .withActionBarDrawerToggleAnimated(true)
                .withHeader(R.layout.drawer_header)
                .withHeaderDivider(false)
                .addDrawerItems(
                        getFirstDrawerItem(),
                        getSecondDrawerItem()
                )
                .build();
    }

    private PrimaryDrawerItem getFirstDrawerItem() {

        return new PrimaryDrawerItem().
                withName("工作清单")
                .withIcon(GoogleMaterial.Icon.gmd_assignment)
                .withBadge("10");
    }

    private PrimaryDrawerItem getSecondDrawerItem() {

        return new PrimaryDrawerItem().
                withName("待定")
                .withIcon(GoogleMaterial.Icon.gmd_assignment_ind);
    }

    // ========================== 再按一次退出 ==========================

    long exitTime = 0;

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen()) {
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
