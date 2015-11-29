package com.unicorn.csp.xcdemo.adaper.recycleview.technician;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.WorkOrderCardActivity;
import com.unicorn.csp.xcdemo.activity.technician.PackActivity;
import com.unicorn.csp.xcdemo.component.OperationUtils;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.component.WorkOrderFrameLayout;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import tyrantgit.explosionfield.ExplosionField;


public class WorkOrderSuspendedAdapter extends RecyclerView.Adapter<WorkOrderSuspendedAdapter.ViewHolder> implements RefreshAdapter {


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

        // todo delete
        @OnLongClick(R.id.cardview)
        public boolean explode(View v) {
            Activity activity = (Activity) v.getContext();
            ExplosionField.attach2Window(activity).explode(v);
            workOrderProcessInfoList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            return true;
        }

        @OnClick(R.id.btn_pack)
        public void startPackActivity(PaperButton paperButton) {
            Activity activity = (Activity) paperButton.getContext();
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cardView, WorkOrderCardActivity.SHARED_VIEW);
            Intent intent = new Intent(paperButton.getContext(), PackActivity.class);
            WorkOrderProcessInfo workOrderProcessInfo = workOrderProcessInfoList.get(getAdapterPosition());
            intent.putExtra("refreshEventTag","workOrderSuspendedFragment_refresh");
            intent.putExtra("workOrderInfo", workOrderProcessInfo.getWorkOrderInfo());
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        }


        @OnClick(R.id.btn_operation)
        public void showChooseOperationDialog(PaperButton btnOperation) {
            WorkOrderInfo workOrderInfo = workOrderProcessInfoList.get(getAdapterPosition()).getWorkOrderInfo();
            OperationUtils.showChooseOperationDialog((Activity) btnOperation.getContext(), workOrderInfo,"workOrderSuspendedFragment_refresh", false);
        }

    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_order_suspended, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        WorkOrderInfo workOrderInfo = workOrderProcessInfoList.get(position).getWorkOrderInfo();
        viewHolder.workOrderCard.setWorkOrderInfo(workOrderInfo);
        viewHolder.workOrderCard.expandableLayout.setExpanded(workOrderProcessInfoList.get(position).isExpand());
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderProcessInfoList.size();
    }

}
