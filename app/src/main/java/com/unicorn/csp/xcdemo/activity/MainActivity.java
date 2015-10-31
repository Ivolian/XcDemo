package com.unicorn.csp.xcdemo.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.adaper.viewpager.ViewPagerAdapter;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import butterknife.Bind;


public class MainActivity extends ToolbarActivity {

    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;

    @Bind(R.id.tabs)
    TabLayout tabs;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    Drawer drawer;

    MiniDrawer miniResult = null;

//    MaterialSearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar("工作清单", false);
        initViews(savedInstanceState);

//        searchView = (MaterialSearchView) findViewById(R.id.search_view);
//        searchView.setVoiceSearch(false);
    }

    public void initViews(Bundle savedInstanceState) {

        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabs.setupWithViewPager(viewPager);
        initDrawer(savedInstanceState);

    }

    private void initDrawer(Bundle savedInstanceState) {

        crossfadeDrawerLayout = new CrossfadeDrawerLayout(this);
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withToolbar(getToolbar())
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerLayout(crossfadeDrawerLayout)
                .withSavedInstance(savedInstanceState)
                .withHeader(R.layout.drawer_header)
                .withHasStableIds(true)
                .withDrawerWidthDp(72)
                .withHeaderDivider(false)
                .addDrawerItems(
                        getFirstDrawerItem(),
                        getSecondDrawerItem(),
                        getThindDrawerItem()
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(MainActivity.this, ((Nameable) drawerItem).getName().getText(MainActivity.this), Toast.LENGTH_SHORT).show();
                        }

                        return miniResult.onItemClick(drawerItem);
                    }
                })
                .withShowDrawerOnFirstLaunch(true)

                .build();


        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)

        miniResult = new MiniDrawer().withDrawer(drawer);
        View view = miniResult.build(this);
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = drawer.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        super.onSaveInstanceState(outState);
    }


    private PrimaryDrawerItem getFirstDrawerItem() {

        return new PrimaryDrawerItem().
                withName("工作清单")
                .withIcon(GoogleMaterial.Icon.gmd_assignment);
    }

    private PrimaryDrawerItem getSecondDrawerItem() {

        return new PrimaryDrawerItem().
                withName("统计分析")
                .withIcon(GoogleMaterial.Icon.gmd_insert_chart);
    }

    private PrimaryDrawerItem getThindDrawerItem() {

        return new PrimaryDrawerItem().
                withName("我的收益")
                .withIcon(GoogleMaterial.Icon.gmd_assignment_ind);
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

    @Bind(R.id.search_view)
    MaterialSearchView searchView;


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setIcon(getActionDrawable());

        searchView.setMenuItem(item);
        searchView.setHint("请输入查询内容");
        searchView.setSuggestions(new String[]{"给排水", "风扇"});
//        searchView.setVoiceSearch(false);
        return super.onCreateOptionsMenu(menu);

    }

    private Drawable getActionDrawable() {

        return new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_search)
                .color(Color.WHITE)
                .sizeDp(18);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}
