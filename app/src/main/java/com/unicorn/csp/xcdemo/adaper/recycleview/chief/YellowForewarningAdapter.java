package com.unicorn.csp.xcdemo.adaper.recycleview.chief;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.WorkOrderDetailActivity;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.model.WorkOrderWarn;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class YellowForewarningAdapter extends RecyclerView.Adapter<YellowForewarningAdapter.ViewHolder> implements RefreshAdapter {


    // ================================== data ==================================

    private List<WorkOrderWarn> workOrderWarnList = new ArrayList<>();

    @Override
    public void reload(Object workOrderWarnList) {
        this.workOrderWarnList = (List<WorkOrderWarn>) workOrderWarnList;
        notifyDataSetChanged();
    }

    @Override
    public void loadMore(Object workOrderWarnList) {
        this.workOrderWarnList.addAll((List<WorkOrderWarn>) workOrderWarnList);
        notifyDataSetChanged();
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_message)
        TextView tvMessage;

        @BindView(R.id.tv_create_time)
        TextView tvCreateTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cardview)
        public void cardViewOnClick() {
            Context context =tvMessage.getContext();
            Intent intent = new Intent(context, WorkOrderDetailActivity.class);
            WorkOrderWarn workOrderWarn = workOrderWarnList.get(getAdapterPosition());
            intent.putExtra("workOrderId", workOrderWarn.getWorkOrderId());
            context.startActivity(intent);
        }
    }

    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_forewarning, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        WorkOrderWarn workOrderWarn = workOrderWarnList.get(position);
        viewHolder.tvMessage.setText(workOrderWarn.getMessage());
        viewHolder.tvMessage.setTextColor(ContextCompat.getColor(viewHolder.tvMessage.getContext(), R.color.md_yellow_800));
        viewHolder.tvCreateTime.setText(new DateTime(workOrderWarn.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderWarnList.size();
    }

}
