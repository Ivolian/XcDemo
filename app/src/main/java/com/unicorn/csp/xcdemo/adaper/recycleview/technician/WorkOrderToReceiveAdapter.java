package com.unicorn.csp.xcdemo.adaper.recycleview.technician;

import android.support.annotation.NonNull;
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


//@P
public class WorkOrderToReceiveAdapter extends RecyclerView.Adapter<WorkOrderToReceiveAdapter.ViewHolder> implements RefreshAdapter {


    // ================================== refreshEventTag ==================================

    String refreshEventTag = "workOrderToReceiveFragment_refresh";


    // ================================== data ==================================

    private List<WorkOrderProcessInfo> workOrderProcessInfoList = new ArrayList<>();

    @Override
    public void reload(Object workOrderProcessInfoList) {
        this.workOrderProcessInfoList = (List<WorkOrderProcessInfo>)workOrderProcessInfoList;
        notifyDataSetChanged();
    }

    @Override
    public void loadMore(Object workOrderProcessInfoList) {
        this.workOrderProcessInfoList.addAll((List<WorkOrderProcessInfo>)workOrderProcessInfoList);
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

        @OnClick(R.id.cardview)
        public void toggle() {
            workOrderCard.expandableLayout.toggle();
        }

        @OnClick(R.id.btn_receive)
        public void receiveConfirm(PaperButton paperButton) {
            WorkOrderProcessInfo workOrderProcessInfo = workOrderProcessInfoList.get(getAdapterPosition());
            final String workOrderId = workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId();
            DialogUtils.showConfirm(paperButton.getContext(), "确认接单？", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                    receive(workOrderId);
                }
            });
        }

        private void receive(final String workOrderId) {
            StringRequest stringRequest = new StringRequestWithSessionCheck(
                    Request.Method.PUT,
                    ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderId + "/receive",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ToastUtils.show("接单成功！");
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
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_order_to_receive, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        WorkOrderInfo workOrderInfo = workOrderProcessInfoList.get(position).getWorkOrderInfo();
        workOrderInfo.setLabelText(workOrderInfo.getStatusTag().equals("Distribute") ? "派" : "抢");
        viewHolder.workOrderCard.setWorkOrderInfo(workOrderInfo);
        viewHolder.workOrderCard.expandableLayout.setExpanded(workOrderProcessInfoList.get(position).isExpand());
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderProcessInfoList.size();
    }

}
