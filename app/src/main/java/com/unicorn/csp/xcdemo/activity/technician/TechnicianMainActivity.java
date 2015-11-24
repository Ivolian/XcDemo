package com.unicorn.csp.xcdemo.activity.technician;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.fragment.technician.TestFragment;
import com.unicorn.csp.xcdemo.fragment.technician.WorkListFragment;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import butterknife.Bind;


//@PP
public class TechnicianMainActivity extends ToolbarActivity {


    // ================================== searchView ==================================

    @Bind(R.id.search_view)
    public MaterialSearchView searchView;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar("工作清单", false);
        initViews();
    }

    private void initViews() {
        initDrawer();
        replaceFragment_(new WorkListFragment());
    }

    private void replaceFragment_(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
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
                .withOnDrawerItemClickListener(getOnDrawerItemClickListener())
                .build();
    }

    private IDrawerItem[] getDrawerItems() {
        return new IDrawerItem[]{
                new PrimaryDrawerItem().withName("工作清单").withIcon(GoogleMaterial.Icon.gmd_assignment).withIdentifier(0),
                new PrimaryDrawerItem().withName("统计分析").withIcon(GoogleMaterial.Icon.gmd_chart).withIdentifier(1),
                new PrimaryDrawerItem().withName("我的收益").withIcon(GoogleMaterial.Icon.gmd_assignment_account).withIdentifier(2)
        };
    }

    private Drawer.OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch (drawerItem.getIdentifier()) {
                    case 0:
                        ToastUtils.show("hehe");
                        replaceFragment_(new WorkListFragment());
                        break;
                    case 1:
                        // todo
                        replaceFragment_(new TestFragment());
                        break;
                }
                return true;
            }
        };
    }


    // ========================== 再按一次退出 ==========================

    long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
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

}
