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
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.LoginActivity;
import com.unicorn.csp.xcdemo.activity.technician.DetailActivity;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.model.WorkOrder;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WaitReceiveAdapter extends RecyclerView.Adapter<WaitReceiveAdapter.ViewHolder> {


    // ================================== data  ==================================

    private List<WorkOrder> workOrderList = new ArrayList<>();

    public List<WorkOrder> getWorkOrderList() {
        return workOrderList;
    }

    public void setWorkOrderList(List<WorkOrder> workOrderList) {
        this.workOrderList = workOrderList;
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.labelview)
        LabelView labelView;

        @Bind(R.id.tv_request_user_and_call_number)
        TextView tvRequestUserAndallNumber;

        @Bind(R.id.tv_request_time)
        TextView tvRequestTime;

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

        @OnClick(R.id.btn_receive)
        public void showConfirmReceiveDialog(PaperButton paperButton) {

            new MaterialDialog.Builder(paperButton.getContext())
                    .content("确认接单？")
                    .positiveText("确认")
                    .negativeText("取消")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            receiveWorkOrder(getAdapterPosition());
                        }
                    })
                    .show();
        }

    }


    // ================================== item layout ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wait_receive, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        WorkOrder workOrder = getWorkOrderList().get(position);
        viewHolder.tvRequestUserAndallNumber.setText("报修电话: " + workOrder.getCallNumber() + " " + workOrder.getRequestUser());
        viewHolder.tvRequestTime.setText("报修时间:" + new DateTime(workOrder.getRequestTime()).toString("yyyy-MM-dd HH:mm:ss"));

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

        return workOrderList.size();
    }


    private void receiveWorkOrder(final int position) {
        WorkOrder workOrder = workOrderList.get(position);
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrder.getObjectId() + "/receive";
        SimpleVolley.addRequest(
                new StringRequest(
                        Request.Method.PUT,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ToastUtils.show("接单成功！");
                                WaitReceiveAdapter.this.notifyItemRemoved(position);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                ToastUtils.show(VolleyErrorHelper.getErrorMessage(volleyError));
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        String jsessionid = TinyDB.getInstance().getString(LoginActivity.JSESSION_ID);
                        map.put("Cookie", "JSESSIONID=" + jsessionid);
                        return map;
                    }
                }
        );
    }

}
