package com.unicorn.csp.xcdemo.activity.base;

import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.unicorn.csp.xcdemo.R;

import butterknife.Bind;


public abstract class ToolbarActivity extends ButterKnifeActivity {


    // ========================== views ==========================

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tv_toolbar_title)
    public TextView tvToolbarTitle;


    // ========================== home键后退 ==========================

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // ========================== toolbar ==========================

    protected void initToolbar(String toolbarTitle, boolean displayHomeAsUpEnable) {

        // 隐藏默认标题，使用自定义标题
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(displayHomeAsUpEnable);
        }
        tvToolbarTitle.setText(toolbarTitle);
    }

    protected void setToolbarTitle(@StringRes int toolbarTitle) {

        tvToolbarTitle.setText(toolbarTitle);
    }

    protected boolean isToolbarHidden() {

        return toolbar.getVisibility() == View.GONE;
    }

    protected void hideToolbar() {

        toolbar.setVisibility(View.GONE);
    }

    protected void showToolbar() {

        toolbar.setVisibility(View.VISIBLE);
    }

    protected void toggleToolbar() {

        if (isToolbarHidden()) {
            showToolbar();
        } else {
            hideToolbar();
        }
    }

    protected Toolbar getToolbar() {

        return toolbar;
    }

}
