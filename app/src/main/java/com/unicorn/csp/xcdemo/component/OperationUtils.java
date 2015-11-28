package com.unicorn.csp.xcdemo.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;
import com.kennyc.bottomsheet.menu.BottomSheetMenuItem;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.technician.AchieveActivity;
import com.unicorn.csp.xcdemo.activity.technician.CameraConfirmActivity;
import com.unicorn.csp.xcdemo.activity.technician.MicActivity;
import com.unicorn.csp.xcdemo.activity.technician.PhotoConfirmActivity;

import java.util.ArrayList;
import java.util.List;


public class OperationUtils {

    public static void showChooseOperationDialog(final Activity activity, boolean showSuspend) {

        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(getMenuItem(activity, "拍照", GoogleMaterial.Icon.gmd_camera));
        menuItems.add(getMenuItem(activity, "录音", GoogleMaterial.Icon.gmd_mic));
        menuItems.add(getMenuItem(activity, "摄像", GoogleMaterial.Icon.gmd_videocam));
        menuItems.add(getMenuItem(activity, "结单", GoogleMaterial.Icon.gmd_check_circle));
        if (showSuspend) {
            menuItems.add(getMenuItem(activity, "挂单", GoogleMaterial.Icon.gmd_cloud_upload));
        }
        menuItems.add(getMenuItem(activity, "移单", GoogleMaterial.Icon.gmd_accounts_add));
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
                                             activity.startActivity(intent);
                                             break;
                                         case "录音":
                                             intent = new Intent(activity, MicActivity.class);
                                             activity.startActivity(intent);
                                             break;
                                         case "摄像":
                                             intent = new Intent(activity, CameraConfirmActivity.class);
                                             activity.startActivity(intent);
                                             break;
                                         case "结单":
                                             intent = new Intent(activity, AchieveActivity.class);
                                             activity.startActivity(intent);
                                             break;
                                         case "挂单":
                                             break;
                                         case "移单":
                                         case "退单":
                                             break;
                                     }
                                 }

                                 @Override
                                 public void onSheetDismissed(int i) {

                                 }
                             }
                ).show();
    }

    private static MenuItem getMenuItem(Context context, String title, GoogleMaterial.Icon icon) {
        return new BottomSheetMenuItem(context, title, new IconicsDrawable(context)
                .icon(icon)
                .colorRes(R.color.primary)
                .sizeDp(48));
    }

}