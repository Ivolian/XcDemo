package com.unicorn.csp.xcdemo.activity.chief;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.activity.shared.LoginActivity;
import com.unicorn.csp.xcdemo.fragment.chief.WorkOrderTodoFragment;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import butterknife.Bind;

//@P
public class ChiefMainActivity extends ToolbarActivity {


    // ================================== titles ==================================

    String[] TITLES = {"我的工作", "工单查询", "我的收益", "用户登出"};


    // ================================== searchView ==================================

    @Bind(R.id.search_view)
    public MaterialSearchView searchView;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar(TITLES[0], false);
        initViews();
    }

    private void initViews() {
        initDrawer();
        replaceFragment_(new WorkOrderTodoFragment());
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
                new PrimaryDrawerItem().withName(TITLES[0]).withIcon(GoogleMaterial.Icon.gmd_assignment),
                new PrimaryDrawerItem().withName(TITLES[1]).withIcon(GoogleMaterial.Icon.gmd_assignment_check),
                new PrimaryDrawerItem().withName(TITLES[2]).withIcon(GoogleMaterial.Icon.gmd_notifications_active),
                new PrimaryDrawerItem().withName(TITLES[3]).withIcon(GoogleMaterial.Icon.gmd_sign_in).withIdentifier(3).withSelectable(false)
        };
    }


    private Drawer.OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                switch (drawerItem.getIdentifier()) {
                    case 0:
                        if (!getToolbarTitle().equals(TITLES[0])) {
                            setToolbarTitle(TITLES[0]);
                            replaceFragment_(new WorkOrderTodoFragment());
                        }
                        break;
                    case 3:
                        DialogUtils.showConfirm(ChiefMainActivity.this, "确认登出？", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivityAndFinish(LoginActivity.class);
                            }
                        });
                        break;
                }
                if (searchView.isSearchOpen()) {
                    searchView.closeSearch();
                }
                drawer.closeDrawer();
                return true;
            }
        };
    }


    // ========================== 再按一次退出 ==========================

    long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            if (searchView.isSearchOpen()) {
                searchView.closeSearch();
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