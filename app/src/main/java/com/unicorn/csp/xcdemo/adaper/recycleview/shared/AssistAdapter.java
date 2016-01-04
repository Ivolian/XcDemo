package com.unicorn.csp.xcdemo.adaper.recycleview.shared;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.unicorn.csp.xcdemo.R;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AssistAdapter extends RecyclerView.Adapter<AssistAdapter.ViewHolder> {


    // ================================== data ==================================

    private List<Object> dataList = new ArrayList<>();

    private List<Integer> selectedList = new ArrayList<>();

    public AssistAdapter() {
        for (int i = 0; i != 20; i++) {
            dataList.add(new Object());
        }
    }

    // ================================== 公开的方法 ==================================

    public void clearSelected() {
        selectedList.clear();
        notifyDataSetChanged();
        notifyCab();
    }

    public int getSelectedCount() {
        return selectedList.size();
    }

    public void selectAll() {
        for (int i = 0; i != 20; i++) {
            if (!selectedList.contains(i)) {
                selectedList.add(i);
            }
        }
        notifyDataSetChanged();
        notifyCab();
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.checkbox)
        CheckBox checkBox;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.root)
        public void onCliCK() {
            select();
        }

        private void select() {
            Integer position = getAdapterPosition();
            if (selectedList.contains(position)) {
                selectedList.remove(position);
            } else {
                selectedList.add(position);
            }
            notifyItemChanged(position);
            notifyCab();
        }
    }

    private void notifyCab(){
        EventBus.getDefault().post(new Object(), "assist_select");
    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_assist, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.checkBox.setChecked(selectedList.contains(position));
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
