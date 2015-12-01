package com.unicorn.csp.xcdemo.component;

import com.afollestad.materialdialogs.MaterialDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class UploadUtils {

    public static void upload(File fileToUpload, final String postEventTag, final MaterialDialog mask) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = ConfigUtils.getBaseUrl() + "/api/v1/system/file/upload";
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("attachment", fileToUpload);
        } catch (Exception e) {
            //
        }
        client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (mask != null) {
                    mask.dismiss();
                }
                try {
                    String jsonObjectString = new String(bytes);
                    JSONObject jsonObject = new JSONObject(jsonObjectString);
                    String tempFileName = JSONUtils.getString(jsonObject, "tempFileName", "");
                    EventBus.getDefault().post(tempFileName, postEventTag);
                } catch (Exception e) {
                    //
                }
                ToastUtils.show("上传成功");
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                mask.setProgress((int)(bytesWritten*100/totalSize));
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (mask != null) {
                    mask.dismiss();
                }
                ToastUtils.show("上传失败");
            }
        });
    }

}
