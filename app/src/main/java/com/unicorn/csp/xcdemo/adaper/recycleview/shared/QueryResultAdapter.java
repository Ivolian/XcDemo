package com.unicorn.csp.xcdemo.adaper.recycleview.shared;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.component.WorkOrderFrameLayout;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class QueryResultAdapter extends RecyclerView.Adapter<QueryResultAdapter.ViewHolder> implements RefreshAdapter {


    // ================================== data ==================================

    private List<WorkOrderInfo> workOrderInfoList = new ArrayList<>();

    public void reload(Object workOrderInfoList) {
        this.workOrderInfoList = (List<WorkOrderInfo>)workOrderInfoList;
        notifyDataSetChanged();
    }

    public void loadMore(Object workOrderInfoList) {
this.workOrderInfoList.addAll((List<WorkOrderInfo>)workOrderInfoList);
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
                    workOrderInfoList.get(getAdapterPosition()).setExpand(true);
                }

                @Override
                public void onPreClose() {
                    workOrderInfoList.get(getAdapterPosition()).setExpand(false);
                }
            });
        }

        @OnClick(R.id.cardview)
        public void toggle() {
            workOrderCard.expandableLayout.toggle();
        }

    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_order_achieved, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        WorkOrderInfo workOrderInfo = workOrderInfoList.get(position);
        viewHolder.workOrderCard.setWorkOrderInfo(workOrderInfo);
        viewHolder.workOrderCard.expandableLayout.setExpanded(workOrderInfoList.get(position).isExpand());
        viewHolder.workOrderCard.label.setVisibility(View.INVISIBLE);
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderInfoList.size();
    }

}
