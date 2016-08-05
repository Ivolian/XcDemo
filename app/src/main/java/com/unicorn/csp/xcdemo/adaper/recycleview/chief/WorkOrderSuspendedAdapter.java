package com.unicorn.csp.xcdemo.adaper.recycleview.chief;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.chief.AssignActivity;
import com.unicorn.csp.xcdemo.activity.shared.base.WorkOrderCardActivity;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.component.WorkOrderFrameLayout;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.StringRequestWithSessionCheck;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WorkOrderSuspendedAdapter extends RecyclerView.Adapter<WorkOrderSuspendedAdapter.ViewHolder> implements RefreshAdapter {


    // ================================== data ==================================

    private List<WorkOrderProcessInfo> workOrderProcessInfoList = new ArrayList<>();

    @Override
    public void reload(Object workOrderProcessInfoList) {
        this.workOrderProcessInfoList = (List<WorkOrderProcessInfo>) workOrderProcessInfoList;
        notifyDataSetChanged();
    }

    @Override
    public void loadMore(Object workOrderProcessInfoList) {
        this.workOrderProcessInfoList.addAll((List<WorkOrderProcessInfo>) workOrderProcessInfoList);
        notifyDataSetChanged();
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.work_order_card)
        WorkOrderFrameLayout workOrderCard;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            workOrderCard.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    workOrderProcessInfoList.get(getAdapterPosition()).setExpand(true);
                }

                @Override
                public void onPreClose() {
                    workOrderProcessInfoList.get(getAdapterPosition()).setExpand(false);
                }
            });
        }

        @BindView(R.id.cardview)
        CardView cardView;

        @BindView(R.id.btn_assign)
        PaperButton btnAssign;

        @BindView(R.id.btn_request)
        PaperButton btnRequest;


        @OnClick(R.id.cardview)
        public void toggle() {
            workOrderCard.expandableLayout.toggle();
        }

        String refreshEventTag = "chief_workOrderSuspendedFragment_refresh";

        @OnClick(R.id.btn_assign)
        public void startAssignActivity(PaperButton paperButton) {
            Activity activity = (Activity) paperButton.getContext();
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cardView, WorkOrderCardActivity.SHARED_VIEW);
            Intent intent = new Intent(paperButton.getContext(), AssignActivity.class);
            intent.putExtra("workOrderProcessInfo", workOrderProcessInfoList.get(getAdapterPosition()));
            intent.putExtra("refreshEventTag", refreshEventTag);
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        }

        @OnClick(R.id.btn_request)
        public void requestOnClick(View view) {
            DialogUtils.showConfirm(view.getContext(), "确认挂单申请", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfoList.get(getAdapterPosition()).getWorkOrderInfo().getWorkOrderId() + "/hangUpRequest";
                    StringRequest stringRequest = new StringRequestWithSessionCheck(
                            Request.Method.PUT,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    EventBus.getDefault().post(new Object(), refreshEventTag);
                                    ToastUtils.show("挂单申请成功!");
                                }
                            },
                            SimpleVolley.getDefaultErrorListener()
                    );
                    SimpleVolley.addRequest(stringRequest);
                }
            });
        }
    }
    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_order_suspended_chief, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        WorkOrderInfo workOrderInfo = workOrderProcessInfoList.get(position).getWorkOrderInfo();
        workOrderInfo.setLabelText("挂");
        viewHolder.workOrderCard.setHangupDescription(workOrderProcessInfoList.get(position).getWorkOrderProcess().getWorkOrderHangUp().getRemark());
        viewHolder.workOrderCard.setWorkOrderInfo(workOrderInfo);
        viewHolder.workOrderCard.expandableLayout.setExpanded(workOrderProcessInfoList.get(position).isExpand());

        viewHolder.btnAssign.setVisibility((workOrderInfo.getHangUpStatus() == 0 || workOrderInfo.getHangUpStatus() == 1) ? View.VISIBLE : View.GONE);
        viewHolder.btnRequest.setVisibility(workOrderInfo.getHangUpStatus() == 1 ? View.VISIBLE : View.GONE);
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderProcessInfoList.size();
    }

}
