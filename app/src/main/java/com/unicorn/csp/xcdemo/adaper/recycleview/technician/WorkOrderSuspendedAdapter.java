package com.unicorn.csp.xcdemo.adaper.recycleview.technician;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.kennyc.bottomsheet.menu.BottomSheetMenuItem;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.technician.PackActivity;
import com.unicorn.csp.xcdemo.activity.technician.PhotoConfirmActivity;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.component.WorkOrderFrameLayout;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class WorkOrderSuspendedAdapter extends RecyclerView.Adapter<WorkOrderSuspendedAdapter.ViewHolder> implements RefreshAdapter {


    // ================================== data ==================================

    private List<WorkOrderProcessInfo> workOrderProcessInfoList = new ArrayList<>();

    @Override
    public void reload(List<WorkOrderProcessInfo> workOrderProcessInfoList) {
        this.workOrderProcessInfoList = workOrderProcessInfoList;
        notifyDataSetChanged();
    }

    @Override
    public void loadMore(List<WorkOrderProcessInfo> workOrderProcessInfoList) {
        this.workOrderProcessInfoList.addAll(workOrderProcessInfoList);
        notifyDataSetChanged();
    }


    // ================================== viewHolder ==================================

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.work_order_card)
        WorkOrderFrameLayout workOrderFrameLayout;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            workOrderFrameLayout.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
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

        @OnClick(R.id.cardview)
        public void toggle() {
            workOrderFrameLayout.expandableLayout.toggle();
        }

        @OnClick(R.id.btn_pack)
        public void startPackActivity(PaperButton paperButton) {
            Context context = paperButton.getContext();
            Intent intent = new Intent(context, PackActivity.class);
            intent.putExtra("workOrderProcessInfo", workOrderProcessInfoList.get(getAdapterPosition()));
            context.startActivity(intent);
        }

        private MenuItem getMenuItem(Context context, String title, GoogleMaterial.Icon icon) {
            return new BottomSheetMenuItem(context, title, new IconicsDrawable(context)
                    .icon(icon)
                    .colorRes(R.color.primary)
                    .sizeDp(48));
        }

        @OnClick(R.id.btn_operation)
        public void showChooseOperationDialog(PaperButton paperButton) {
            final Activity activity = (Activity) paperButton.getContext();
            new BottomSheet.Builder(activity)
                    .setTitle("选择操作")
                    .grid()
                    .setMenuItems(Arrays.asList(
                            getMenuItem(activity, "拍照", GoogleMaterial.Icon.gmd_camera),
                            getMenuItem(activity, "录音", GoogleMaterial.Icon.gmd_mic),
                            getMenuItem(activity, "摄像", GoogleMaterial.Icon.gmd_videocam),
                            getMenuItem(activity, "结单", GoogleMaterial.Icon.gmd_check_circle),
                            getMenuItem(activity, "挂单", GoogleMaterial.Icon.gmd_cloud_upload),
                            getMenuItem(activity, "移单", GoogleMaterial.Icon.gmd_accounts_add),
                            getMenuItem(activity, "退单", GoogleMaterial.Icon.gmd_delete)
                    ))
                    .setListener(new BottomSheetListener() {
                                     @Override
                                     public void onSheetShown() {

                                     }

                                     @Override
                                     public void onSheetItemSelected(MenuItem menuItem) {
                                         switch (menuItem.getTitle().toString()) {
                                             case "拍照":
                                                 Intent intent = new Intent(activity, PhotoConfirmActivity.class);
                                                 activity.startActivity(intent);
                                                 break;
                                             case "录音":
                                                 break;
                                             case "结单":
                                                 break;
                                             case "挂单":
                                                 break;
                                             case "移单":
                                             case "退单":
                                                 ToastUtils.show("暂不支持");
                                                 break;
                                         }
                                     }

                                     @Override
                                     public void onSheetDismissed(int i) {

                                     }
                                 }
                    ).show();
        }

    }


    // ================================== onCreateViewHolder ==================================

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_work_order_suspended, viewGroup, false));
    }


    // ================================== onBindViewHolder ==================================

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        WorkOrderInfo workOrderInfo = workOrderProcessInfoList.get(position).getWorkOrderInfo();
//        viewHolder.workOrderCard.setWorkOrderInfo(workOrderInfo);
        viewHolder.workOrderFrameLayout.expandableLayout.setExpanded(workOrderProcessInfoList.get(position).isExpand());
    }


    // ================================== getItemCount ==================================

    @Override
    public int getItemCount() {
        return workOrderProcessInfoList.size();
    }

}
