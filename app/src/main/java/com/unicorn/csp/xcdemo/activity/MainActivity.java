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
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.adaper.viewpager.ViewPagerAdapter;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import org.simple.eventbus.EventBus;

import butterknife.Bind;


public class MainActivity extends ToolbarActivity {


    // ================================== views ==================================

    @Bind(R.id.tabslayout)
    TabLayout tabLayout;

    @Bind(R.id.viewpager)
    ViewPager viewPager;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar("工作清单", false);
        initViews(savedInstanceState);
    }

    public void initViews(Bundle savedInstanceState) {

        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        initDrawer(savedInstanceState);
    }


    // ================================== drawer ==================================

    Drawer drawer;

    MiniDrawer miniDrawer;

    private void initDrawer(Bundle savedInstanceState) {

        CrossfadeDrawerLayout crossfadeDrawerLayout = new CrossfadeDrawerLayout(this);
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(getToolbar())
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentStatusBar(false)
                .withDrawerLayout(crossfadeDrawerLayout)
                .withDrawerWidthDp(72)
                .withHeader(R.layout.drawer_header)
                .withHeaderDivider(false)
                .addDrawerItems(getDrawerItems())
                .withOnDrawerItemClickListener(getOnDrawItemClickListener())
                .withSavedInstance(savedInstanceState)
                .build();

        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        miniDrawer = new MiniDrawer().withDrawer(drawer);
        View view = miniDrawer.build(this);
        view.setBackgroundColor(getResources().getColor(R.color.white));
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private IDrawerItem[] getDrawerItems() {

        return new IDrawerItem[]{
                new PrimaryDrawerItem().withName("工作清单").withIcon(GoogleMaterial.Icon.gmd_assignment),
                new PrimaryDrawerItem().withName("统计分析").withIcon(GoogleMaterial.Icon.gmd_insert_chart),
                new PrimaryDrawerItem().withName("统计分析").withIcon(GoogleMaterial.Icon.gmd_insert_chart)
        };
    }

    private Drawer.OnDrawerItemClickListener getOnDrawItemClickListener() {

        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                // todo delete
                if (drawerItem instanceof Nameable) {
                    Toast.makeText(MainActivity.this, ((Nameable) drawerItem).getName().getText(MainActivity.this), Toast.LENGTH_SHORT).show();
                }
                return miniDrawer.onItemClick(drawerItem);
            }
        };
    }


    // ================================== onSaveInstanceState ==================================

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState = drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    // ================================== toolbar menu ==================================

    @Bind(R.id.searchview)
    MaterialSearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setIcon(getSearchDrawable());
        initSearchView(menuItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private Drawable getSearchDrawable() {

        return new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_search)
                .color(Color.WHITE)
                .sizeDp(18);
    }

    private void initSearchView(MenuItem menuItem) {

        searchView.setMenuItem(menuItem);
        searchView.setHint("请输入查询内容");
//        searchView.setSuggestions(new String[]{"给排水", "风扇"});
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                EventBus.getDefault().post("sdfs", "search");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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


}
