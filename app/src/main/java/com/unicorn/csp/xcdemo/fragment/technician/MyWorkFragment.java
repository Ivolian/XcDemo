package com.unicorn.csp.xcdemo.fragment.technician;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.technician.MainActivity;
import com.unicorn.csp.xcdemo.adaper.viewpager.technician.MyWorkFragmentPagerAdapter;
import com.unicorn.csp.xcdemo.component.TabHelper;
import com.unicorn.csp.xcdemo.fragment.shared.base.TabLayoutFragment;
import com.unicorn.csp.xcdemo.model.RefreshResult;

import org.simple.eventbus.Subscriber;

import su.levenetc.android.badgeview.BadgeView;


public class MyWorkFragment extends TabLayoutFragment {


    // ================================== abstract method ==================================

    FragmentStatePagerAdapter adapter;

    @Override
    public FragmentStatePagerAdapter getPagerAdapter() {
        if (adapter == null) {
            adapter = new MyWorkFragmentPagerAdapter(getActivity().getSupportFragmentManager());
        }
        return adapter;
    }


    // ================================== search ==================================

    @Override
    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_work, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        menuItem.setIcon(getSearchIconDrawable());
        initSearchView(menuItem);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void initSearchView(MenuItem menuItem) {
        MainActivity mainActivity = (MainActivity) getActivity();
        MaterialSearchView searchView = mainActivity.searchView;
        searchView.setMenuItem(menuItem);
        searchView.setHint("请输入查询内容");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private Drawable getSearchIconDrawable() {
        return new IconicsDrawable(getActivity())
                .icon(GoogleMaterial.Icon.gmd_search)
                .color(Color.WHITE)
                .sizeDp(18);
    }


    // ================================== onRefreshFinish ==================================

    @Subscriber(tag = "myWorkFragment_onRefreshFinish")
    private void onRefreshFinish(RefreshResult refreshResult) {
        BadgeView badgeView = TabHelper.getBadgeView(tabLayout.getTabAt(refreshResult.getTabIndex()));
        badgeView.setValue(refreshResult.getTotal(), true);
        showSnackBar("共 " + refreshResult.getTotal() + " 条记录");
    }


}
