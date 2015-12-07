package com.unicorn.csp.xcdemo.activity.shared;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.f2prateek.dart.InjectExtra;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.QueryResultAdapter;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.utils.GsonUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.RecycleViewUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JSONObjectRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;
import kale.recycler.OnRecyclerViewScrollListener;


public class QueryResultActivity extends ToolbarActivity {

    @InjectExtra("queryUrl")
    String queryUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);
        initToolbar("查询结果", true);
        initViews();
        enableSlideFinish();
    }

    // ================================== views ==================================

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    ExRecyclerView recyclerView;


    // ================================== adapter ==================================

    QueryResultAdapter adapter;


    // ================================== paging fields ==================================

    final Integer PAGE_SIZE = 10;

    Integer pageNo = 1;

    boolean lastPage = false;

    boolean loadingMore;


    // ================================== initViews ==================================

    private void initViews() {
        initSwipeRefreshLayout();
        initRecyclerView();
        firstLoad();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
    }

    private void initRecyclerView() {
        int orientation = getResources().getConfiguration().orientation;
        recyclerView.setLayoutManager(orientation == Configuration.ORIENTATION_PORTRAIT ?
                RecycleViewUtils.getLinearLayoutManager(this) : RecycleViewUtils.getStaggeredGridLayoutManager());
        adapter = new QueryResultAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {
            @Override
            public void onScrollUp() {

            }

            @Override
            public void onScrollDown() {

            }

            @Override
            public void onBottom() {
                if (!loadingMore && !lastPage) {
                    loadMore();
                }
            }

            @Override
            public void onMoved(int i, int i1) {

            }
        });
    }

    private void firstLoad() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                startRefreshing();
            }
        });
        reload();
    }


    // ================================== reload ==================================

    public void reload() {
        clearPageFields();
        JsonObjectRequest jsonObjectRequest = new JSONObjectRequestWithSessionCheck(
                getCompleteUrl(pageNo),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        stopRefreshing();
                        JSONArray jsonArray = JSONUtils.getJSONArray(response, "content", null);
                        List<WorkOrderInfo> workOrderInfoList = GsonUtils.parseWorkOrderInfoList(jsonArray.toString());
                        adapter.reload(workOrderInfoList);
                        checkLastPage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        stopRefreshing();
                        ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                    }
                }
        );
        SimpleVolley.addRequest(jsonObjectRequest);
    }

    private void loadMore() {
        loadingMore = true;
        JsonObjectRequest jsonObjectRequest = new JSONObjectRequestWithSessionCheck(getCompleteUrl(pageNo + 1),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingMore = false;
                        pageNo++;
                        JSONArray jsonArray = JSONUtils.getJSONArray(response, "content", null);
                        List<WorkOrderInfo> workOrderInfoList = GsonUtils.parseWorkOrderInfoList(jsonArray.toString());
                        adapter.loadMore(workOrderInfoList);
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
        );
        SimpleVolley.addRequest(jsonObjectRequest);
    }

    private void clearPageFields() {
        pageNo = 1;
        lastPage = false;
    }


    // ========================== 基础方法 ==========================

    private String getCompleteUrl(Integer pageNo) {
        queryUrl += ("&pageNo" + "=" + pageNo.toString());
        queryUrl += ("&pageSize" + "=" + PAGE_SIZE.toString());
        return queryUrl;
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


    // ========================== 安全方法 ==========================

    private void stopRefreshing() {
        if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void startRefreshing() {
        if (swipeRefreshLayout != null && !swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }


}
