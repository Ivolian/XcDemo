package com.unicorn.csp.xcdemo.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.google.zxing.integration.android.IntentIntegrator;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.kennyc.bottomsheet.menu.BottomSheetMenuItem;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.AssistActivity;
import com.unicorn.csp.xcdemo.activity.shared.SuspendActivity;
import com.unicorn.csp.xcdemo.activity.technician.AchieveActivity;
import com.unicorn.csp.xcdemo.activity.technician.MicActivity;
import com.unicorn.csp.xcdemo.activity.technician.PhotoConfirmActivity;
import com.unicorn.csp.xcdemo.activity.technician.VideoConfirmActivity;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;


public class OperationUtils {

    public static void showChooseOperationDialog(final Activity activity, final WorkOrderProcessInfo workOrderProcessInfo, final String refreshEventTag, boolean showSuspend) {
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(getMenuItem(activity, "拍照", GoogleMaterial.Icon.gmd_camera));
        menuItems.add(getMenuItem(activity, "录音", GoogleMaterial.Icon.gmd_mic));
        menuItems.add(getMenuItem(activity, "摄像", GoogleMaterial.Icon.gmd_videocam));
        menuItems.add(getMenuItem(activity, "协助", FontAwesome.Icon.faw_users));
        menuItems.add(getMenuItem(activity, "扫码", GoogleMaterial.Icon.gmd_fullscreen));
        menuItems.add(getMenuItem(activity, "结单", GoogleMaterial.Icon.gmd_check_circle));
        if (showSuspend) {
            menuItems.add(getMenuItem(activity, "挂单", GoogleMaterial.Icon.gmd_cloud_upload));
        }
        menuItems.add(getMenuItem(activity, "移单", FontAwesome.Icon.faw_reply_all));
        menuItems.add(getMenuItem(activity, "退单", GoogleMaterial.Icon.gmd_delete));
        new BottomSheet.Builder(activity)
                .setTitle("选择操作")
                .grid()
                .setMenuItems(menuItems)
                .setListener(new BottomSheetListener() {
                                 @Override
                                 public void onSheetShown() {

                                 }

                                 @Override
                                 public void onSheetItemSelected(MenuItem menuItem) {
                                     Intent intent;
                                     switch (menuItem.getTitle().toString()) {
                                         case "拍照":
                                             intent = new Intent(activity, PhotoConfirmActivity.class);
                                             intent.putExtra("workOrderId", workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId());
                                             activity.startActivity(intent);
                                             break;
                                         case "录音":
                                             intent = new Intent(activity, MicActivity.class);
                                             intent.putExtra("workOrderId", workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId());
                                             activity.startActivity(intent);
                                             break;
                                         case "摄像":
                                             intent = new Intent(activity, VideoConfirmActivity.class);
                                             intent.putExtra("workOrderId", workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId());
                                             activity.startActivity(intent);
                                             break;
                                         case "协助":
                                             intent = new Intent(activity, AssistActivity.class);
                                             intent.putExtra("workOrderId", workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId());
                                             activity.startActivity(intent);
                                             break;
                                         case "扫码":
                                             new IntentIntegrator(activity).initiateScan();
                                             break;
                                         case "结单":
                                             intent = new Intent(activity, AchieveActivity.class);
                                             intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
                                             intent.putExtra("refreshEventTag", refreshEventTag);
                                             activity.startActivity(intent);
                                             break;
                                         case "挂单":
                                             intent = new Intent(activity, SuspendActivity.class);
                                             intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
                                             intent.putExtra("refreshEventTag",refreshEventTag);
                                             activity.startActivity(intent);
                                             break;
                                         case "移单":
                                         case "退单":
                                             ToastUtils.show("暂无此功能");
                                             break;
                                     }
                                 }

                                 @Override
                                 public void onSheetDismissed(int i) {

                                 }
                             }
                ).show();
    }

    private static MenuItem getMenuItem(Context context, String title, IIcon icon) {
        return new BottomSheetMenuItem(context, title, new IconicsDrawable(context)
                .icon(icon)
                .colorRes(R.color.primary)
                .sizeDp(48));
    }

}
