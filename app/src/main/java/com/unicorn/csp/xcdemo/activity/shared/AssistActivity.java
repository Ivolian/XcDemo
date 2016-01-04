package com.unicorn.csp.xcdemo.activity.shared;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.AssistAdapter;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.DrawableUtils;
import com.unicorn.csp.xcdemo.utils.RecycleViewUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;


public class AssistActivity extends ToolbarActivity {


    // ================================== onCreate & onDestroy ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_assist);
        initToolbar("协助", true);
        initViews();
        enableSlideFinish();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        initRecyclerView();
    }


    // ================================== recyclerView ==================================

    @Bind(R.id.recyclerView)
    ExRecyclerView recyclerView;

    AssistAdapter assistAdapter;

    private void initRecyclerView() {
        recyclerView.setLayoutManager(RecycleViewUtils.getLinearLayoutManager(this));
        assistAdapter = new AssistAdapter();
        recyclerView.setAdapter(assistAdapter);
        int dividerColor = ContextCompat.getColor(this, R.color.bootstrap_gray_lighter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(dividerColor).build());
    }


    // ================================== select callback ==================================

    @Subscriber(tag = "assist_select")
    private void onAssistSelected(Object object) {
        if (assistAdapter.getSelectedCount() == 0) {
            hideCab();
            return;
        }
        if (!isCabVisible()) {
            showCab();
        }
        String title = "选中 " + assistAdapter.getSelectedCount();
        setToolbarTitle(title);
    }


    // ================================== onBackPressed ==================================

    @Override
    public void onBackPressed() {
        if (isCabVisible()) {
            hideCab();
            assistAdapter.clearSelected();
        } else {
            super.onBackPressed();
        }
    }


    // ================================== menu ==================================

    MenuItem itemSelectAll;

    MenuItem itemConfirm;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_all:
                if (assistAdapter.getSelectedCount() == assistAdapter.getItemCount()) {
                    assistAdapter.clearSelected();
                } else {
                    assistAdapter.selectAll();
                }
                return true;
            case R.id.confirm:
                DialogUtils.showConfirm(this, "确认协助?", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_assist, menu);
        itemSelectAll = menu.findItem(R.id.select_all);
        itemSelectAll.setIcon(DrawableUtils.getIconDrawable(this, GoogleMaterial.Icon.gmd_select_all, Color.WHITE, 18));
        itemConfirm = menu.findItem(R.id.confirm);
        itemConfirm.setIcon(DrawableUtils.getIconDrawable(this, GoogleMaterial.Icon.gmd_check, Color.WHITE, 18));
        hideCab();
        return super.onCreateOptionsMenu(menu);
    }


    // ================================== cab methods ==================================

    private void showCab() {
        itemSelectAll.setVisible(true);
        itemConfirm.setVisible(true);
    }

    private void hideCab() {
        itemSelectAll.setVisible(false);
        itemConfirm.setVisible(false);
        setToolbarTitle("协助");
    }

    private boolean isCabVisible(){
        return itemSelectAll.isVisible();
    }


}
