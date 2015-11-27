package com.unicorn.csp.xcdemo.activity.technician;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.liangfeizc.flowlayout.FlowLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.OptionButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JSONArrayRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;


public class CameraConfirmActivity extends ToolbarActivity {


    // ========================== extra ==========================

    // todo
//    @InjectExtra("workOrderProcessInfo")
//    WorkOrderProcessInfo workOrderProcessInfo;


    // ========================== onCreate ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_confirm);
        initToolbar("摄像确认", true);
        initViews();
        enableSlideFinish();
        autoStartCamera();
    }

    private void initViews() {
        fetchOptions();
        initEtDescription();
    }


    // ========================== options ==========================

    @Bind(R.id.fl_options)
    FlowLayout flOptions;

    private void fetchOptions() {
        JsonArrayRequest jsonArrayRequest = new JSONArrayRequestWithSessionCheck(
                Request.Method.GET,
                ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/picture/options",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i != response.length(); i++) {
                            JSONObject jsonObject = JSONUtils.getJSONObject(response, i);
                            String objectId = JSONUtils.getString(jsonObject, "objectId", "");
                            String name = JSONUtils.getString(jsonObject, "name", "");
                            flOptions.addView(getOptionButton(name, objectId), getLayoutParams());
                        }
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(jsonArrayRequest);
    }

    private OptionButton getOptionButton(String name, String objectId) {
        final OptionButton optionButton = new OptionButton(this);
        optionButton.name = name;
        optionButton.objectId = objectId;
        optionButton.setText(name);
        optionButton.setPadding(4, 4, 4, 4);
        optionButton.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        optionButton.setBootstrapSize(DefaultBootstrapSize.MD);
        optionButton.setRounded(true);
        optionButton.setShowOutline(true);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optionButton.isShowOutline()) {
                    for (int i = 0; i != flOptions.getChildCount(); i++) {
                        View child = flOptions.getChildAt(i);
                        if (child != v) {
                            ((OptionButton) child).setShowOutline(true);
                        }
                    }
                }
                optionButton.setShowOutline(!optionButton.isShowOutline());
            }
        });
        return optionButton;
    }

    private ViewGroup.LayoutParams getLayoutParams() {
        // TODO: Is this a correct use of layoutParams ?
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    // ========================== description ==========================

    @Bind(R.id.et_description)
    BootstrapEditText etDescription;

    private void initEtDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


    // ========================== start camera ==========================

    final int CAMERA_REQUEST_CODE = 2333;

    private void autoStartCamera() {
        new MaterialCamera(this)
                .saveDir(ConfigUtils.getBaseDirPath())
                .showPortraitWarning(false)
                .allowRetry(true)
                .defaultToFrontFacing(false)
                .start(CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                URL videoUrl = new URL(data.getDataString());
                uploadPhoto(videoUrl);
            } catch (Exception e) {
                //
            }
        }else {
            finish();
        }
    }


    // ========================== upload video ==========================

    String videoTempFileName;

    private void uploadPhoto(URL videoUrl) {
        final MaterialDialog mask = DialogUtils.showMask(this,"上传摄像中","请稍后");
        AsyncHttpClient client = new AsyncHttpClient();
        String url = ConfigUtils.getBaseUrl() + "/api/v1/system/file/upload";
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("attachment", new File(videoUrl.toURI()));
        } catch (Exception e) {
            //
        }
        client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                mask.dismiss();
                String jsonObjectString = new String(bytes);
                try {
                    JSONObject jsonObject = new JSONObject(jsonObjectString);
                    videoTempFileName = JSONUtils.getString(jsonObject, "tempFileName", "");
                } catch (Exception e) {
                    //
                }
                ToastUtils.show("上传成功");
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                mask.dismiss();
                ToastUtils.show("上传失败");
            }
        });
    }


    // ========================== confirm video ==========================

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        final OptionButton optionSelected = getOptionSelected();
        if (optionSelected == null) {
            ToastUtils.show("至少选择一个摄像选项");
            return;
        }
        if (videoTempFileName == null) {
            ToastUtils.show("上传摄像失败，请重新摄像");
            return;
        }

        // todo
//        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId() + "/picture";
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                "",
//                        url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show("确认完成!");
                        CameraConfirmActivity.this.finish();
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject code = new JSONObject();
                    code.put("name", optionSelected.name);
                    code.put("objectId", optionSelected.objectId);
                    JSONObject result = new JSONObject();
                    result.put("option", code);
                    result.put("remark", etDescription.getText().toString().trim());
                    result.put("picture", videoTempFileName);
                    return result.toString().getBytes("UTF-8");
                } catch (Exception e) {
                    //
                }
                return null;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                String jsessionid = TinyDB.getInstance().getString(ConfigUtils.JSESSION_ID);
                map.put("Cookie", "JSESSIONID=" + jsessionid);
                // 不加这个会出现 415 错误
                map.put("Content-Type", "application/json");
                return map;
            }
        };
        SimpleVolley.addRequest(stringRequest);
    }

    private OptionButton getOptionSelected() {
        OptionButton optionSelected = null;
        for (int i = 0; i != flOptions.getChildCount(); i++) {
            OptionButton optionButton = (OptionButton) flOptions.getChildAt(i);
            if (!optionButton.isShowOutline()) {
                optionSelected = optionButton;
            }
        }
        return optionSelected;
    }

}
