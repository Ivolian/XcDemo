package com.unicorn.csp.xcdemo.adaper.recycleview;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.ArrivalActivity;
import com.unicorn.csp.xcdemo.activity.FinishActivity;
import com.unicorn.csp.xcdemo.activity.GetActivity;
import com.unicorn.csp.xcdemo.model.Model;
import com.wangqiang.libs.labelviewlib.LabelView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.ViewHolder> {

    private Activity activity;

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

    public CustomAdapter2(Activity activity, List<Model> modelList) {
        this.activity = activity;
        this.modelList = modelList;
    }

    // 绑定视图，添加事件
    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.label)
        LabelView labelView;

        @Bind(R.id.cardview)
        CardView cardView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }

        @OnClick(R.id.btn_get)
        public void get(){
            Intent intent = new Intent(activity,GetActivity.class);
            activity.startActivity(intent);
        }

        @OnClick(R.id.btn_arrival)
        public void arrival(){

            new MaterialDialog.Builder(activity)
                    .content("确认到达？")
                    .positiveText("确认")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            Intent intent = new Intent(activity, ArrivalActivity.class);
                            activity.startActivity(intent);
                        }
                    })
                    .show();

        }


    }

    public void startFinishActivity(View shareView) {
        Intent intent = new Intent(activity, FinishActivity.class);
        activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity,shareView , "cardview").toBundle());
    }


    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_custom2, viewGroup, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        if (position % 2 != 0) {
            viewHolder.labelView.setBackgroundResource(R.color.blue);
            viewHolder.labelView.setText("新");
        } else {
            viewHolder.labelView.setBackgroundResource(R.color.colorAccent);
            viewHolder.labelView.setText("限");
        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

}
