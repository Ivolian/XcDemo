package com.unicorn.csp.xcdemo.activity.chief;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.unicorn.csp.xcdemo.activity.shared.LoginActivity;
import com.unicorn.csp.xcdemo.fragment.chief.MyWorkFragment;
import com.unicorn.csp.xcdemo.fragment.chief.WorkOrderForewarningFragment;
import com.unicorn.csp.xcdemo.fragment.shared.WorkOrderQueryFragment;
import com.unicorn.csp.xcdemo.utils.DialogUtils;

import java.util.ArrayList;


public class MainActivity extends com.unicorn.csp.xcdemo.activity.shared.base.MainActivity {

    @Override
    public Fragment getFirstFragment() {
        return new MyWorkFragment();
    }

    @Override
    public GoogleMaterial.Icon[] getIcons() {
        return new GoogleMaterial.Icon[]{
                GoogleMaterial.Icon.gmd_assignment,
                GoogleMaterial.Icon.gmd_search_in_file,
                GoogleMaterial.Icon.gmd_notifications_active,
                GoogleMaterial.Icon.gmd_sign_in
        };
    }

    @Override
    public String[] getTitles() {
        return new String[]{"我的工作", "工单查询", "工单预警", "用户登出"};
    }

    @Override
    public ArrayList<IDrawerItem> getDrawerItems() {
        ArrayList<IDrawerItem> drawerItems = new ArrayList<>();
        for (int i = 0; i != getTitles().length; i++) {
            IDrawerItem drawerItem = new PrimaryDrawerItem().withName(getTitles()[i]).withIcon(getIcons()[i]).withIdentifier(i);
            drawerItems.add(drawerItem);
        }
        drawerItems.get(3).withSelectable(false);
        return drawerItems;
    }

    @Override
    public Drawer.OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (searchView.isSearchOpen()) {
                    searchView.closeSearch();
                }
                switch (drawerItem.getIdentifier()) {
                    case 0:
                        if (!getToolbarTitle().equals(getTitles()[0])) {
                            setToolbarTitle(getTitles()[0]);
                            replaceFragment_(new MyWorkFragment());
                        }
                        break;
                    case 1:
                        if (!getToolbarTitle().equals(getTitles()[1])) {
                            setToolbarTitle(getTitles()[1]);
                            replaceFragment_(new WorkOrderQueryFragment());
                        }
                        break;
                    case 2:
                        if (!getToolbarTitle().equals(getTitles()[2])) {
                            setToolbarTitle(getTitles()[2]);
                            replaceFragment_(new WorkOrderForewarningFragment());
                        }
                        break;
                    case 3:
                        DialogUtils.showConfirm(MainActivity.this, "确认登出？", new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivityAndFinish(LoginActivity.class);
                            }
                        });
                        break;
                }
                return false;
            }
        };
    }

}