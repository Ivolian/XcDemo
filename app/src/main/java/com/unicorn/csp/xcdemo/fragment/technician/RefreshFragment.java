package com.unicorn.csp.xcdemo.fragment.technician;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.adaper.recycleview.technician.RefreshAdapter;
import com.unicorn.csp.xcdemo.fragment.base.LazyLoadFragment;
import com.unicorn.csp.xcdemo.model.RefreshResult;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.GsonUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.RecycleViewUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JSONObjectRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import kale.recycler.ExRecyclerView;
import kale.recycler.OnRecyclerViewScrollListener;


//@P
public abstract class RefreshFragment extends LazyLoadFragment {


    // ================================== abstract methods ==================================

    abstract public RefreshAdapter getAdapter();

    abstract public String getLatterPartUrl();

    abstract public int getFragmentIndex();


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
        return R.layout.fragment_basic_refresh_recycle;
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
        int orientation = getResources().getConfiguration().orientation;
        recyclerView.setLayoutManager(orientation == Configuration.ORIENTATION_PORTRAIT ?
                RecycleViewUtils.getLinearLayoutManager(getActivity()) : RecycleViewUtils.getStaggeredGridLayoutManager());
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
        JsonObjectRequest jsonObjectRequest = new JSONObjectRequestWithSessionCheck(
                getCompleteUrl(pageNo),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        stopRefreshing();
                        JSONArray jsonArray = JSONUtils.getJSONArray(response, "content", null);
                        List<WorkOrderProcessInfo> workOrderProcessInfoList = GsonUtils.parseWorkOrderProcessInfoList(jsonArray.toString());

                        // todo delete
//                        if (getFragmentIndex()==2){
//                            for (int i=0;i!=10;i++){
//                                workOrderProcessInfoList.add(new WorkOrderProcessInfo());
//                            }
//                        }

                        adapter.reload(workOrderProcessInfoList);
                        checkLastPage(response);

                        RefreshResult refreshResult = new RefreshResult();
                        refreshResult.setTabIndex(getFragmentIndex());
                        refreshResult.setTotal(totalElements(response));
                        EventBus.getDefault().post(refreshResult,"workListFragment_onRefreshFinish");
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
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(500, 1, 1.0f));
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
                        List<WorkOrderProcessInfo> workOrderProcessInfoList = GsonUtils.parseWorkOrderProcessInfoList(jsonArray.toString());
                        adapter.loadMore(workOrderProcessInfoList);
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
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + getLatterPartUrl()).buildUpon();
        builder.appendQueryParameter("pageNo", pageNo.toString());
        builder.appendQueryParameter("pageSize", PAGE_SIZE.toString());
        return builder.toString();
    }

    private void checkLastPage(JSONObject response) {
        if (lastPage = isLastPage(response)) {
//            ToastUtils.show(noData(response) ? "暂无数据" : "已加载全部数据");
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
