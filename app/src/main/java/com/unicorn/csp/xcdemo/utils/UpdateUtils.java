package com.unicorn.csp.xcdemo.utils;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.unicorn.csp.xcdemo.volley.JSONObjectRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;


public class UpdateUtils {

    private static Activity currentActivity;

    private static void init(Activity activity) {
        currentActivity = activity;
    }

    private static void clear() {
        currentActivity = null;
    }

    //

    public static void checkUpdate(Activity activity) {
        init(activity);

        String url = ConfigUtils.getBaseUrl() + "/api/v1/system/apk/WorkOrderApp/update";
        JsonObjectRequest jsonObjectRequest = new JSONObjectRequestWithSessionCheck(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final String versionName = JSONUtils.getString(response, "version", "");
                        final String code = JSONUtils.getString(response, "code", "");
                        if (!versionName.equals(AppUtils.getVersionName())) {
                            DialogUtils.showConfirm(currentActivity, "检测到新版本，是否立即更新？", new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    String apkUrl = ConfigUtils.getBaseUrl() + "/apk/download/" + code;
                                    downloadApk(apkUrl);
                                }
                            });
                        }
                    }

                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonObjectRequest);
    }

    private static void downloadApk(String apkUrl) {
        final MaterialDialog mask = DialogUtils.showMask2(currentActivity, "下载 APK 中", "请稍后");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(apkUrl, new FileAsyncHttpResponseHandler(currentActivity) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, File response) {
                mask.dismiss();
                installApk(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                mask.dismiss();
                ToastUtils.show("下载失败");
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                mask.setProgress((int) (bytesWritten * 100 / totalSize));
            }
        });
    }

    private static void installApk(File response) {
        String apkPath = ConfigUtils.getBaseDirPath() + "/hems.apk";
        File apk = new File(apkPath);
        if (apk.exists()) {
            apk.delete();
        }
        try {
            FileUtils.copyFile(response, apk);
        } catch (Exception e) {
            //
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
        currentActivity.startActivity(intent);

        clear();
    }

}
