package com.unicorn.csp.xcdemo.adaper.recycleview.technician;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.technician.OperationActivity;
import com.unicorn.csp.xcdemo.activity.technician.DetailActivity;
import com.unicorn.csp.xcdemo.activity.technician.PackActivity;
import com.unicorn.csp.xcdemo.model.Model;
import com.wangqiang.libs.labelviewlib.LabelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AlreadyReceiveAdapter extends RecyclerView.Adapter<AlreadyReceiveAdapter.ViewHolder> {


    // ================================== data  ==================================

    private List<Model> modelList = new ArrayList<>();

    public List<Model> getModelList() {
        return modelList;
    }

    public void setModelList(List<Model> modelList) {
        this.modelList = modelList;
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

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

        @OnClick(R.id.btn_pack)
        public void startPackActivity(PaperButton paperButton) {
            Context context = paperButton.getContext();
            Intent intent = new Intent(context, PackActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        @OnClick(R.id.btn_arrival_or_operation)
        public void arrivalOrOperate(PaperButton btnArrivalOrOperation) {

            switch (btnArrivalOrOperation.getText()) {
                case "到达":
                    showConfirmArrivalDialog(btnArrivalOrOperation);
                    break;
                case "操作":
                    startOperationActivity(btnArrivalOrOperation);
                    break;
            }
        }

        private void showConfirmArrivalDialog(final PaperButton btnArrivalOrOperate) {

            new MaterialDialog.Builder(btnArrivalOrOperate.getContext())
                    .content("确认到达？")
                    .positiveText("确认")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            btnArrivalOrOperate.setText("操作");
                        }
                    })
                    .show();
        }

        private void startOperationActivity(final PaperButton btnArrivalOrOperation){

            Context context = btnArrivalOrOperation.getContext();
            Intent intent = new Intent(context, OperationActivity.class);
            context.startActivity(intent);
            ((Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

    }


    // ================================== item layout ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_already_receive, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {

        return modelList.size();
    }

}
