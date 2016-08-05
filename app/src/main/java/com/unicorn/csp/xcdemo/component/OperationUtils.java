package com.unicorn.csp.xcdemo.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
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
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.StringRequestWithSessionCheck;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        menuItems.add(getMenuItem(activity, "故障原因", FontAwesome.Icon.faw_exclamation_triangle));
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
                                             isTakePhoto(activity, workOrderProcessInfo, refreshEventTag);
//                                             intent = new Intent(activity, AchieveActivity.class);
//                                             intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
//                                             intent.putExtra("refreshEventTag", refreshEventTag);
//                                             activity.startActivity(intent);
                                             break;
                                         case "挂单":
                                             intent = new Intent(activity, SuspendActivity.class);
                                             intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
                                             intent.putExtra("refreshEventTag", refreshEventTag);
                                             activity.startActivity(intent);
                                             break;
                                         case "故障原因":
                                             showFaultDescriptionDialog(activity, workOrderProcessInfo.getWorkOrderInfo());
                                             break;
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


    // 确认拍摄维修前后照片后方可结单
    private static void isTakePhoto(final Activity activity, final WorkOrderProcessInfo workOrderProcessInfo, final String refreshEventTag) {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId() + "/allowComplete";
        StringRequest stringRequest = new StringRequestWithSessionCheck(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseString) {
                        try {
                            JSONObject response = new JSONObject(responseString);
                            int value = response.getInt("value");
                            if (value == 0) {
                                ToastUtils.show("请拍摄维修前后照片并填写故障原因");
                            } else if (value == 1) {
                                Intent intent = new Intent(activity, AchieveActivity.class);
                                intent.putExtra("workOrderProcessInfo", workOrderProcessInfo);
                                intent.putExtra("refreshEventTag", refreshEventTag);
                                activity.startActivity(intent);
                            }
                        } catch (Exception e) {
                            //
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(stringRequest);

    }

    //

    private static void showFaultDescriptionDialog(final Context context, final WorkOrderInfo workOrderInfo) {
        String faultDescription = workOrderInfo.getFaultDescription() == null ? "" : workOrderInfo.getFaultDescription();
        new MaterialDialog.Builder(context)
                .title("输入故障原因")
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input("", faultDescription, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (!TextUtils.isEmpty(input)) {
                            commitFaultDescription(input.toString(), workOrderInfo);
                        } else {
                            // todo 确保不为空
                            ToastUtils.show("故障原因不能为空!");
                        }
                    }
                }).show();
    }


    public static void commitFaultDescription(final String faultDescription, final WorkOrderInfo workOrderInfo) {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderInfo.getWorkOrderId() + "/faultDescription";
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show("提交故障原因成功!");
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject result = new JSONObject();
                    result.put("value", faultDescription);
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
