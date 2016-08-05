package com.unicorn.csp.xcdemo.adaper.recycleview.supervisor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.adaper.recycleview.shared.RefreshAdapter;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.component.WorkOrderFrameLayout;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.StringRequestWithSessionCheck;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WorkOrderSuspendedAdapter extends RecyclerView.Adapter<WorkOrderSuspendedAdapter.ViewHolder> implements RefreshAdapter {


    // ================================== data ==================================

    private List<WorkOrderProcessInfo> workOrderProcessInfoList = new ArrayList<>();

    @Override
    public void reload(Object workOrderProcessInfoList) {
        this.workOrderProcessInfoList = (List<WorkOrderProcessInfo>) workOrderProcessInfoList;
        notifyDataSetChanged();
    }

    @Override
    public void loadMore(Object workOrderProcessInfoList) {
        this.workOrderProcessInfoList.addAll((List<WorkOrderProcessInfo>) workOrderProcessInfoList);
        notifyDataSetChanged();
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.work_order_card)
        WorkOrderFrameLayout workOrderCard;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            workOrderCard.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
                @Override
                public void onPreOpen() {
                    workOrderProcessInfoList.get(getAdapterPosition()).setExpand(true);
                }

                @Override
                public void onPreClose() {
                    workOrderProcessInfoList.get(getAdapterPosition()).setExpand(false);
                }
            });
        }

        @BindView(R.id.cardview)
        CardView cardView;

        @BindView(R.id.btn_agree)
        PaperButton btnAgree;

        @BindView(R.id.btn_disagree)
        PaperButton btnDisagree;

        @OnClick(R.id.cardview)
        public void toggle() {
            workOrderCard.expandableLayout.toggle();
        }

        String refreshEventTag = "supervisor_workOrderSuspendedFragment_refresh";

        @OnClick(R.id.btn_agree)
        public void agreeOnClick(View view) {
            DialogUtils.showConfirm(view.getContext(), "确认同意", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfoList.get(getAdapterPosition()).getWorkOrderInfo().getWorkOrderId() + "/hangUpConfirm";
                    StringRequest stringRequest = new StringRequestWithSessionCheck(
                            Request.Method.PUT,
                            url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    ToastUtils.show("处理成功!");
                                    EventBus.getDefault().post(new Object(), refreshEventTag);
                                }
                            },
                            SimpleVolley.getDefaultErrorListener()
                    ) {
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                JSONObject result = new JSONObject();
                                result.put("result", 1);
                                String jsonString = result.toString();
                                return jsonString.getBytes("UTF-8");
                            } catch (Exception e) {
                                //
                            }
                            return null;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("Cookie", "JSESSIONID=" + ConfigUtils.getJsessionId());
                            // 不加这个会出现 415 错误
                            map.put("Content-Type", "application/json");
                            return map;
                        }
                    };
                    SimpleVolley.addRequest(stringRequest);
                }
            });
        }


        // 1 2 同意
        // remark
        @OnClick(R.id.btn_disagree)
        public void requestOnClick(final View view) {
            DialogUtils.showConfirm(view.getContext(), "确认不同意", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    showDisagreeDescriptionDialog(view.getContext());
                }
            });
        }

        private void showDisagreeDescriptionDialog(final Context context) {
            new MaterialDialog.Builder(context)
                    .title("输入不同意原因")
                    .inputType(InputType.TYPE_CLASS_TEXT)
                    .input("", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                            disagree(input.toString());
                        }
                    }).show();
        }

        private void disagree(final String remark) {
            String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfoList.get(getAdapterPosition()).getWorkOrderInfo().getWorkOrderId() + "/hangUpConfirm";
            StringRequest stringRequest = new StringRequestWithSessionCheck(
                    Request.Method.PUT,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            EventBus.getDefault().post(new Object(), refreshEventTag);
                            ToastUtils.show("处理成功!");
                        }
                    },
                    SimpleVolley.getDefaultErrorListener()
            ) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        JSONObject result = new JSONObject();
                        result.put("result", 2);
                        result.put("remark", remark);
                        String jsonString = result.toString();
                        return jsonString.getBytes("UTF-8");
                    } catch (Exception e) {
                        //
                    }
                    return null;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("Cookie", "JSESSIONID=" + ConfigUtils.getJsessionId());
                    // 不加这个会出现 415 错误
                    map.put("Content-Type", "application/json");
                    return map;
                }
            };
            SimpleVolley.addRequest(stringRequest);
        }

    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_order_suspended_supervisor, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        WorkOrderInfo workOrderInfo = workOrderProcessInfoList.get(position).getWorkOrderInfo();
        workOrderInfo.setLabelText("挂");
        if (workOrderProcessInfoList.get(position).getWorkOrderProcess().getWorkOrderHangUp() != null) {
            viewHolder.workOrderCard.setHangupDescription(workOrderProcessInfoList.get(position).getWorkOrderProcess().getWorkOrderHangUp().getRemark());
        }
        viewHolder.workOrderCard.setWorkOrderInfo(workOrderInfo);
        viewHolder.workOrderCard.expandableLayout.setExpanded(workOrderProcessInfoList.get(position).isExpand());

        viewHolder.btnAgree.setVisibility(workOrderInfo.getHangUpStatus() == 2 ? View.VISIBLE : View.GONE);
        viewHolder.btnDisagree.setVisibility(workOrderInfo.getHangUpStatus() == 2 ? View.VISIBLE : View.GONE);

    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderProcessInfoList.size();
    }

}
