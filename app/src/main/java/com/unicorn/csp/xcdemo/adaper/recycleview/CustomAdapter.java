package com.unicorn.csp.xcdemo.adaper.recycleview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.model.Model;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<Model> modelList;

    public List<Model> getModelList() {
        return modelList;
    }

    public void setModelList(List<Model> modelList) {
        this.modelList = modelList;
    }

    public void addModelList(List<Model> modelList) {
       this.modelList.addAll(modelList);
    }

    public CustomAdapter(List<Model> modelList) {
        this.modelList = modelList;
    }



    // 绑定视图，添加事件
    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_book_name)
        TextView tvBookName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_custom, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        Model model = modelList.get(position);
        viewHolder.tvBookName.setText(model.getText());
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

}
