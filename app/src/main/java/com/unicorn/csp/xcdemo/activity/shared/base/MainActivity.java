package com.unicorn.csp.xcdemo.activity.shared.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.utils.UpdateUtils;

import java.util.ArrayList;

import butterknife.Bind;


public abstract class MainActivity extends ToolbarActivity {


    // ================================== abstract methods ==================================

    public abstract String[] getTitles();

    public abstract GoogleMaterial.Icon[] getIcons();

    public abstract Fragment getFirstFragment();

    public abstract ArrayList<IDrawerItem> getDrawerItems();

    public abstract Drawer.OnDrawerItemClickListener getOnDrawerItemClickListener();


    // ================================== searchView ==================================

    @Bind(R.id.search_view)
    public MaterialSearchView searchView;


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar(getTitles()[0], false);
        init();
    }

    private void init() {
        initDrawer();
        replaceFragment_(getFirstFragment());
        UpdateUtils.checkUpdate(this);
    }

    public void replaceFragment_(Fragment fragment) {
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
//                .withHeader(R.layout.drawer_header)
//                .withHeaderDivider(false)
                .withDrawerItems(getDrawerItems())
                .withOnDrawerItemClickListener(getOnDrawerItemClickListener())
                .build();
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


    //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // todo 设备扫码
        // 处理扫描条码返回结果
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String equipmentId = result.getContents();
            ToastUtils.show("设备码: " + equipmentId);
//            Intent intent = new Intent(this, EquipmentInfoActivity.class);
//            intent.putExtra("equipmentId", equipmentId);
//            startActivity(intent);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}

