package com.unicorn.csp.xcdemo.adaper.recycleview.shared;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorn.csp.xcdemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


public class SparePartAdapter extends RecyclerView.Adapter<SparePartAdapter.ViewHolder>  {


    // ================================== data ==================================

    private List<String> dataList = new ArrayList<>();

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {



        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_spare_part, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
