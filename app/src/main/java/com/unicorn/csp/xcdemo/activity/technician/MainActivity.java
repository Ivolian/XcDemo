package com.unicorn.csp.xcdemo.activity.technician;

import android.content.Intent;
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
import com.unicorn.csp.xcdemo.activity.shared.equipment.EquipmentInfoActivity;
import com.unicorn.csp.xcdemo.fragment.shared.WorkOrderQueryFragment;
import com.unicorn.csp.xcdemo.fragment.technician.MyWorkFragment;
import com.unicorn.csp.xcdemo.utils.DialogUtils;

import java.util.ArrayList;


public class MainActivity extends com.unicorn.csp.xcdemo.activity.shared.base.MainActivity {

    @Override
    public String[] getTitles() {
        return new String[]{"我的工作", "工单查询", "我的收益", "用户登出"};
    }

    @Override
    public GoogleMaterial.Icon[] getIcons() {
        return new GoogleMaterial.Icon[]{
                GoogleMaterial.Icon.gmd_assignment,
                GoogleMaterial.Icon.gmd_search_in_file,
                GoogleMaterial.Icon.gmd_assignment_account,
                GoogleMaterial.Icon.gmd_sign_in
        };
    }

    @Override
    public Fragment getFirstFragment() {
        return new MyWorkFragment();
    }

    @Override
    public ArrayList<IDrawerItem> getDrawerItems() {
        ArrayList<IDrawerItem> drawerItems = new ArrayList<>();
        for (int i = 0; i != getTitles().length; i++) {
            IDrawerItem drawerItem = new PrimaryDrawerItem().withName(getTitles()[i]).withIcon(getIcons()[i]).withIdentifier(i);
            drawerItems.add(drawerItem);
        }
        drawerItems.get(2).withSelectable(false);
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
                        return true;
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

    //


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 处理扫描条码返回结果
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//        if (result != null && result.getContents() != null) {
//            ToastUtils.show(result.getContents());

            startActivity(EquipmentInfoActivity.class);

//        }


    }

}
