package com.unicorn.csp.xcdemo.fragment.technician;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.LoginActivity;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.AlreadyAchieveAdapter;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.fragment.base.ButterKnifeFragment;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.RecycleViewUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;
import kale.recycler.OnRecyclerViewScrollListener;


public class AlreadyAchieveFragment extends ButterKnifeFragment {


    // ================================== views ==================================

    @Bind(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    public ExRecyclerView recyclerView;


    // ================================== adapter ==================================

    AlreadyAchieveAdapter alreadyAchieveAdapter;


    // ================================== paging fields ==================================

    final Integer PAGE_SIZE = 10;

    Integer pageNo;

    boolean loadingMore;

    boolean lastPage;


    // ================================== getLayoutResId ==================================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_basic_refresh_recycle;
    }


    // ================================== onCreateView ==================================

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        initViews();
        return rootView;
    }


    // ================================== initViews ==================================

    private void initViews() {
        initSwipeRefreshLayout();
        initRecyclerView();
        reload();
    }

    public void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
    }

    public void initRecyclerView() {
        recyclerView.setLayoutManager(RecycleViewUtils.getLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(alreadyAchieveAdapter = new AlreadyAchieveAdapter());
        recyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
                if (lastPage || loadingMore) {
                    return;
                }
                loadMore();
            }

            @Override
            public void onMoved(int i, int i1) {

            }
        });
    }


    public void reload() {
        clearPageData();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                startRefreshing();
            }
        });
        SimpleVolley.addRequest(
                new JsonObjectRequest(
                        getUrl(pageNo),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                stopRefreshing();
                                JSONArray jsonArray = JSONUtils.getJSONArray(response, "content", null);
                                String json = jsonArray.toString();

                                Gson gson = new Gson();
                                // json转为带泛型的list
                                List<WorkOrderProcessInfo> workOrderProcessInfoList = gson.fromJson(json,
                                        new TypeToken<List<WorkOrderProcessInfo>>() {
                                        }.getType());
                                alreadyAchieveAdapter.setWorkOrderProcessInfoList(workOrderProcessInfoList);
                                alreadyAchieveAdapter.notifyDataSetChanged();
                                checkLastPage(response);
                                int total = totalElements(response);
                                EventBus.getDefault().post("共 " + total + " 条记录", "onFragmentRefreshFinish");
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                stopRefreshing();
                                ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        String jsessionid = TinyDB.getInstance().getString(LoginActivity.JSESSION_ID);
                        map.put("Cookie", "JSESSIONID=" + jsessionid);
                        return map;
                    }
                }
        );
    }

    private void loadMore() {
        loadingMore = true;
        SimpleVolley.addRequest(new JsonObjectRequest(getUrl(pageNo + 1),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingMore = false;
                        pageNo++;
                        JSONArray jsonArray = JSONUtils.getJSONArray(response, "content", null);
                        String json = jsonArray.toString();

                        Gson gson = new Gson();
                        // json转为带泛型的list
                        List<WorkOrderProcessInfo> workOrderProcessInfoList = gson.fromJson(json,
                                new TypeToken<List<WorkOrderProcessInfo>>() {
                                }.getType());
                        alreadyAchieveAdapter.getWorkOrderProcessInfoList().addAll(workOrderProcessInfoList);
                        alreadyAchieveAdapter.notifyDataSetChanged();
                        checkLastPage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loadingMore = false;
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                String jsessionid = TinyDB.getInstance().getString(LoginActivity.JSESSION_ID);
                map.put("Cookie", "JSESSIONID=" + jsessionid);
                return map;
            }
        });
    }


    // ========================== 基础方法 ==========================

    private void clearPageData() {
        pageNo = 1;
        lastPage = false;
    }

    private String getUrl(Integer pageNo) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/receive?").buildUpon();
        builder.appendQueryParameter("pageNo", pageNo.toString());
        builder.appendQueryParameter("pageSize", PAGE_SIZE.toString());
        return builder.toString();
    }

    private void checkLastPage(JSONObject response) {

        if (lastPage = isLastPage(response)) {
            ToastUtils.show(noData(response) ? "暂无数据" : "已加载全部数据");
        }
    }

    private boolean isLastPage(JSONObject response) {

        return JSONUtils.getBoolean(response, "lastPage", false);
    }

    private boolean noData(JSONObject response) {

        return JSONUtils.getInt(response, "totalPages", 0) == 0;
    }


    private void stopRefreshing() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void startRefreshing() {

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    private int totalElements(JSONObject response) {

        return JSONUtils.getInt(response, "totalElements", 0);
    }


}
