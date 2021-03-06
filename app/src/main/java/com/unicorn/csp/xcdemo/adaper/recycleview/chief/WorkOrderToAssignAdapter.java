package com.unicorn.csp.xcdemo.adaper.recycleview.chief;

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
import com.unicorn.csp.xcdemo.activity.chief.AssignActivity;
import com.unicorn.csp.xcdemo.activity.shared.SuspendActivity;
import com.unicorn.csp.xcdemo.activity.shared.base.WorkOrderCardActivity;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.component.WorkOrderFrameLayout;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WorkOrderToAssignAdapter extends RecyclerView.Adapter<WorkOrderToAssignAdapter.ViewHolder> implements RefreshAdapter {


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

        @BindView(R.id.cardview)
        CardView cardView;

        @OnClick(R.id.cardview)
        public void toggle() {
            workOrderCard.expandableLayout.toggle();
        }

        String refreshEventTag = "workOrderAssignedFragment_refresh";

        @OnClick(R.id.btn_suspend)
        public void startSuspendActivity(PaperButton paperButton) {
            Activity activity = (Activity) paperButton.getContext();
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cardView, WorkOrderCardActivity.SHARED_VIEW);
            Intent intent = new Intent(paperButton.getContext(), SuspendActivity.class);
            intent.putExtra("workOrderProcessInfo", workOrderProcessInfoList.get(getAdapterPosition()));
            intent.putExtra("refreshEventTag", refreshEventTag);
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        }

        @OnClick(R.id.btn_assign)
        public void startAssignActivity(PaperButton paperButton) {
            Activity activity = (Activity) paperButton.getContext();
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, cardView, WorkOrderCardActivity.SHARED_VIEW);
            Intent intent = new Intent(paperButton.getContext(), AssignActivity.class);
            intent.putExtra("workOrderProcessInfo", workOrderProcessInfoList.get(getAdapterPosition()));
            intent.putExtra("refreshEventTag", refreshEventTag);
            ActivityCompat.startActivity(activity, intent, options.toBundle());
        }

    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_order_assigned, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        WorkOrderInfo workOrderInfo = workOrderProcessInfoList.get(position).getWorkOrderInfo();
        workOrderInfo.setLabelText("指");
        viewHolder.workOrderCard.setWorkOrderInfo(workOrderInfo);
        viewHolder.workOrderCard.expandableLayout.setExpanded(workOrderProcessInfoList.get(position).isExpand());
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderProcessInfoList.size();
    }

}
