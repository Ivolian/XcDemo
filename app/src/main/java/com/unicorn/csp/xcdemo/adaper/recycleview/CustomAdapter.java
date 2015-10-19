package com.unicorn.csp.xcdemo.adaper.recycleview;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.FinishActivity;
import com.unicorn.csp.xcdemo.model.Model;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.wangqiang.libs.labelviewlib.LabelView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

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

    public CustomAdapter(Activity activity, List<Model> modelList) {
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

        @OnClick(R.id.btn_finish)
        public void finish(){

            startFinishActivity(cardView);

        }

        @OnClick(R.id.btn_get)
        public void get(){

                        new MaterialDialog.Builder(activity)
//                    .title("")
                    .content("确认接单？")
                    .positiveText("确认")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            ToastUtils.show("接单成功！");
                            CustomAdapter.this.notifyItemRemoved(getLayoutPosition());
                        }
                    })
                    .show();
        }

        @OnClick(R.id.cardview)
        public void showDialog() {

            new MaterialDialog.Builder(activity)
                    .title("选择操作")
                    .items(new CharSequence[]{"接单", "挂单"})
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which == 0) {
                                ToastUtils.show("接单成功！");
                                CustomAdapter.this.notifyItemRemoved(getLayoutPosition());
                            } else {
                                startFinishActivity(cardView);
                            }
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

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_custom, viewGroup, false));
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
