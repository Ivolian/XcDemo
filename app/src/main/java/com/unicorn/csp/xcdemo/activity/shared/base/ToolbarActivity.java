package com.unicorn.csp.xcdemo.activity.shared.base;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.unicorn.csp.xcdemo.R;

import butterknife.BindView;


public abstract class ToolbarActivity extends ButterKnifeActivity {


    // ========================== views ==========================

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_toolbar_title)
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

    protected String getToolbarTitle() {
        return tvToolbarTitle.getText().toString().trim();
    }

    protected void setToolbarTitle(String toolbarTitle) {
        tvToolbarTitle.setText(toolbarTitle);
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

}
