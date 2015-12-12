package com.unicorn.csp.xcdemo.adaper.recycleview.technician;

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
import com.unicorn.csp.xcdemo.activity.shared.base.WorkOrderCardActivity;
import com.unicorn.csp.xcdemo.activity.technician.PackActivity;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.component.OperationUtils;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


//@P
public class WorkOrderReceivedAdapter extends RecyclerView.Adapter<WorkOrderReceivedAdapter.ViewHolder> implements RefreshAdapter {


    // ================================== refreshEventTag ==================================

    String refreshEventTag = "workOrderReceivedFragment_refresh";


    // ================================== data ==================================

    private List<WorkOrderProcessInfo> workOrderProcessInfoList = new ArrayList<>();

    @Override
    public void reload(List<WorkOrderProcessInfo> workOrderProcessInfoList) {
        this.workOrderProcessInfoList = workOrderProcessInfoList;
        notifyDataSetChanged();
    }

    @Override
    public void loadMore(List<WorkOrderProcessInfo> workOrderProcessInfoList) {
        this.workOrderProcessInfoList.addAll(workOrderProcessInfoList);
        notifyDataSetChanged();
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.work_order_card)
        WorkOrderFrameLayout workOrderCard;

        @Bind(R.id.btn_arrival_or_operation)
        PaperButton btnArrivalOrOperation;

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

        @Bind(R.id.cardview)
        CardView cardView;

        @OnClick(R.id.cardview)
        public void toggle() {
            workOrderCard.expandableLayout.toggle();
        }


        // ================================== 领料 ==================================

        @OnClick(R.id.btn_pack)
        public void startPackActivity(PaperButton paperButton) {
            Activity activity = (Activity) paperButton.getContext();
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cardView, WorkOrderCardActivity.SHARED_VIEW);
            Intent intent = new Intent(paperButton.getContext(), PackActivity.class);
            intent.putExtra("refreshEventTag", refreshEventTag);
            intent.putExtra("workOrderProcessInfo", workOrderProcessInfoList.get(getAdapterPosition()));
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        }


        // ================================== 到达 & 操作 ==================================

        @OnClick(R.id.btn_arrival_or_operation)
        public void arrivalOrOperate(PaperButton btnArrivalOrOperation) {
            final WorkOrderProcessInfo workOrderProcessInfo = workOrderProcessInfoList.get(getAdapterPosition());
            final WorkOrderInfo workOrderInfo = workOrderProcessInfo.getWorkOrderInfo();
            String statusTag = workOrderInfo.getStatusTag();
            switch (statusTag) {
                case "Receive":
                    DialogUtils.showConfirm(btnArrivalOrOperation.getContext(), "确认到达？", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            arrive(workOrderInfo.getWorkOrderId());
                        }
                    });
                    break;
                case "Arrive":
                    OperationUtils.showChooseOperationDialog((Activity) btnArrivalOrOperation.getContext(), workOrderProcessInfo, refreshEventTag, true);
                    break;
            }
        }

        private void arrive(String workOrderId) {
            StringRequest stringRequest = new StringRequestWithSessionCheck(
                    Request.Method.PUT,
                    ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderId + "/arrive",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ToastUtils.show("到达！");
                            EventBus.getDefault().post(new Object(), refreshEventTag);
                        }
                    },
                    SimpleVolley.getDefaultErrorListener()
            );
            SimpleVolley.addRequest(stringRequest);
        }
    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_order_received, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        WorkOrderInfo workOrderInfo = workOrderProcessInfoList.get(position).getWorkOrderInfo();
        workOrderInfo.setLabelText("接");
        viewHolder.workOrderCard.setWorkOrderInfo(workOrderInfo);
        viewHolder.workOrderCard.expandableLayout.setExpanded(workOrderProcessInfoList.get(position).isExpand());
        viewHolder.btnArrivalOrOperation.setText(workOrderInfo.getStatusTag().equals("Receive") ? "到达" : "操作");
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderProcessInfoList.size();
    }

}
