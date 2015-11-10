package com.unicorn.csp.xcdemo.adaper.recycleview.chief;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.SuspendActivity;
import com.unicorn.csp.xcdemo.activity.technician.DetailActivity;
import com.unicorn.csp.xcdemo.model.Model;
import com.wangqiang.libs.labelviewlib.LabelView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {


    // ================================== data  ==================================

    private List<Model> modelList;

    public List<Model> getModelList() {
        return modelList;
    }

    public void setModelList(List<Model> modelList) {
        this.modelList = modelList;
    }


    // ================================== viewHolder ==================================

    public  class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.labelview)
        LabelView labelView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.cardview)
        public void startDetailActivity(CardView cardView) {
            Context context = cardView.getContext();
            Intent intent = new Intent(context, DetailActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @OnClick(R.id.btn_suspend)
        public void startSuspendActivity(PaperButton paperButton) {
            Context context = paperButton.getContext();
            Intent intent = new Intent(context, SuspendActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @OnClick(R.id.btn_assign)
        public void startAssignActivity(PaperButton paperButton) {

            // todo
        }

    }


    // ================================== item layout ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_todo, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

//        if (position % 2 != 0) {
//            viewHolder.labelView.setBackgroundResource(R.color.blue);
//            viewHolder.labelView.setText("新");
//        } else {
//            viewHolder.labelView.setBackgroundResource(R.color.orange);
//            viewHolder.labelView.setText("限");
//        }
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {

        return modelList.size();
    }

}
