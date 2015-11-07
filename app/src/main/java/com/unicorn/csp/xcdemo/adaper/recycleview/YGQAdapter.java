package com.unicorn.csp.xcdemo.adaper.recycleview;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.technician.SuspendActivity;
import com.unicorn.csp.xcdemo.activity.LLActivity;
import com.unicorn.csp.xcdemo.model.Model;
import com.wangqiang.libs.labelviewlib.LabelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class YGQAdapter extends RecyclerView.Adapter<YGQAdapter.ViewHolder> {

    private Activity activity;

    private List<Model> modelList = new ArrayList<>();

    public void addModel() {

        if (modelList.size() == 0) {
            modelList.add(new Model());
            notifyDataSetChanged();
        }
    }

    public YGQAdapter(Activity activity) {

        this.activity = activity;
    }

    // 绑定视图，添加事件
    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.label)
        LabelView labelView;

        @Bind(R.id.cardview)
        CardView cardView;

        @Bind(R.id.btn_ll)
        AppCompatButton btnLL;

        @Bind(R.id.btn_arrival)
        AppCompatButton btnArrail;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            btnLL.setVisibility(View.INVISIBLE);
            btnArrail.setText("操作");
        }



        @OnClick(R.id.btn_ll)
        public void get() {
            Intent intent = new Intent(activity, LLActivity.class);
            activity.startActivity(intent);
        }

        @OnClick(R.id.btn_arrival)
        public void arrival() {

//            if (btnArrail.getText().equals("操作")){
//
//                Intent intent = new Intent(activity, OperationActivity.class);
//                activity.startActivity(intent);
//                return;
//            }



        }


    }

    public void startFinishActivity(View shareView) {
        Intent intent = new Intent(activity, SuspendActivity.class);
        activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity, shareView, "cardview").toBundle());
    }


    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_yjd, viewGroup, false));
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
