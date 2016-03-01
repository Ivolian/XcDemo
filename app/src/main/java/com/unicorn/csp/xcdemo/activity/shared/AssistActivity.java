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
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.f2prateek.dart.InjectExtra;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.AssistAdapter;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.model.AssistObject;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.DrawableUtils;
import com.unicorn.csp.xcdemo.utils.GsonUtils;
import com.unicorn.csp.xcdemo.utils.RecycleViewUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.StringRequestWithSessionCheck;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;


public class AssistActivity extends ToolbarActivity {


    private List<AssistObject> assistObjectList = new ArrayList<>();

    private List<Integer> selectedList = new ArrayList<>();


    // ================================== extra ==================================

    @InjectExtra("workOrderId")
    String workOrderId;


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
        assistAdapter = new AssistAdapter(assistObjectList, selectedList);
        recyclerView.setAdapter(assistAdapter);
        int dividerColor = ContextCompat.getColor(this, R.color.bootstrap_gray_lighter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(dividerColor).build());
        fetchAssistList();
    }


    // ================================== select callback ==================================

    @Subscriber(tag = "assist_select")
    private void onAssistSelected(Object object) {
//        if (assistAdapter.getSelectedCount() == 0) {
//            hideCab();
//            return;
//        }
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
                        assist();
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

    private boolean isCabVisible() {
        return itemSelectAll.isVisible();
    }


    //

    private void fetchAssistList() {
        String url = ConfigUtils.getBaseUrl() + "/hems/workOrder/" + workOrderId + "/assist/artificer";
        StringRequest request = new StringRequestWithSessionCheck(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        copeResponse(response);
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    private void copeResponse(String response) {
        assistObjectList = GsonUtils.parseAssistObjectList(response);
        for (AssistObject assistObject : assistObjectList) {
            if (assistObject.isAssist()) {
                selectedList.add(assistObjectList.indexOf(assistObject));
            }
        }
        assistAdapter.setDataList(assistObjectList);
        assistAdapter.setSelectedList(selectedList);
        assistAdapter.notifyDataSetChanged();
    }


    //

    public void assist() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderId + "/assist";
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show("协助完成!");
                        finish();
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {

                    JSONArray result = new JSONArray();
                    for (Integer position : selectedList) {
                        JSONObject item = new JSONObject();
                        JSONObject artificer = new JSONObject();
                        AssistObject assistObject = assistObjectList.get(position);
                        artificer.put("objectId", assistObject.getObjectId());
                        item.put("artificer", artificer);
                        result.put(item);
                    }
                    return result.toString().getBytes("UTF-8");
                } catch (Exception e) {

                }
                return null;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                String jsessionid = TinyDB.getInstance().getString(ConfigUtils.JSESSION_ID);
                map.put("Cookie", "JSESSIONID=" + jsessionid);
                // 不加这个会出现 415 错误
                map.put("Content-Type", "application/json");
                return map;
            }
        };
        SimpleVolley.addRequest(stringRequest);
    }


}
