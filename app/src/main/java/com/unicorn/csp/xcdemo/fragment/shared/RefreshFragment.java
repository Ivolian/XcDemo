package com.unicorn.csp.xcdemo.fragment.shared;

import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.fragment.shared.base.LazyLoadFragment;
import com.unicorn.csp.xcdemo.model.RefreshResult;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.RecycleViewUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JsonObjectRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;
import kale.recycler.OnRecyclerViewScrollListener;


public abstract class RefreshFragment extends LazyLoadFragment {

    public String getRefreshEventTag(){
        return  "myWorkFragment_onRefreshFinish";
    }

    // ================================== abstract methods ==================================

    abstract public RefreshAdapter getAdapter();

    abstract public String getLatterPartUrl();

    abstract public int getFragmentIndex();

    abstract public Object parseDataList(String jsonArrayString);


    // ================================== views ==================================

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.recyclerView)
    ExRecyclerView recyclerView;


    // ================================== adapter ==================================

    RefreshAdapter adapter;


    // ================================== paging fields ==================================

    final Integer PAGE_SIZE = 10;

    Integer pageNo = 1;

    boolean lastPage = false;

    boolean loadingMore;


    // ================================== getLayoutResId ==================================

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_refresh;
    }


    // ================================== onFirstUserVisible ==================================

    @Override
    public void onFirstUserVisible() {
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
        recyclerView.setLayoutManager(RecycleViewUtils.getLinearLayoutManager(getActivity()));
        adapter = getAdapter();
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequestWithSessionCheck(
                getCompleteUrl(pageNo),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        stopRefreshing();
                        JSONArray jsonArray = JSONUtils.getJSONArray(response, "content", null);
                        adapter.reload(parseDataList(jsonArray.toString()));
                        checkLastPage(response);

                        RefreshResult refreshResult = new RefreshResult();
                        refreshResult.setTabIndex(getFragmentIndex());
                        refreshResult.setTotal(totalElements(response));
                        EventBus.getDefault().post(refreshResult, getRefreshEventTag());
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequestWithSessionCheck(getCompleteUrl(pageNo + 1),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingMore = false;
                        pageNo++;
                        JSONArray jsonArray = JSONUtils.getJSONArray(response, "content", null);
                        adapter.loadMore(parseDataList(jsonArray.toString()));
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

    public String getCompleteUrl(Integer pageNo) {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + getLatterPartUrl()).buildUpon();
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

    private int totalElements(JSONObject response) {
        return JSONUtils.getInt(response, "totalElements", 0);
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
