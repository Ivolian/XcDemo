package com.unicorn.csp.xcdemo.activity.technician;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.OptionButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JSONArrayRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


public class VideoConfirmActivity extends ToolbarActivity {


    // ========================== extra ==========================

    // todo
//    @InjectExtra("workOrderProcessInfo")
//    WorkOrderProcessInfo workOrderProcessInfo;


    // ========================== onCreate ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_confirm);
        initToolbar("摄像确认", true);
        enableSlideFinish();
       autoStartCamera();
    }


    // ========================== take photo ==========================

    final int CAMERA_REQUEST_CODE = 2333;

    private void autoStartCamera() {
        new MaterialCamera(this)
                .saveDir(ConfigUtils.getBaseDirPath())
                .showPortraitWarning(false)
                .allowRetry(true)
                .primaryColorAttr(R.attr.colorPrimary)
//                .lengthLimitSeconds(30f)
                .autoSubmit(true)
                .defaultToFrontFacing(false)
                .start(CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();
            } else if(data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }


    // ========================== after take photo ==========================

    private void initViews() {
        fetchOptions();
        initEtDescription();
    }

    @Bind(R.id.et_description)
    BootstrapEditText etDescription;

    private void initEtDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
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


    // ========================== upload photo ==========================

    String photoTempFileName;



//    private void uploadPhoto() {
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = ConfigUtils.getBaseUrl() + "/api/v1/system/file/upload";
//        RequestParams requestParams = new RequestParams();
//        String compressPhotoPath = ImageUtils.compressPhoto(photoPath);
//        try {
//            requestParams.put("attachment", new File(compressPhotoPath));
//        } catch (Exception e) {
//            //
//        }
//        client.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                String jsonObjectString = new String(bytes);
//                try {
//                    JSONObject jsonObject = new JSONObject(jsonObjectString);
//                    photoTempFileName = JSONUtils.getString(jsonObject, "tempFileName", "");
//                } catch (Exception e) {
//                    //
//                }
//                lvUpload.setText("上传成功");
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                lvUpload.setText("上传失败");
//                lvUpload.setBackgroundColor(getResources().getColor(R.color.md_red_400));
//            }
//        });
//    }


    // ========================== confirm photo ==========================

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        final OptionButton optionSelected = getOptionSelected();
        if (optionSelected == null) {
            ToastUtils.show("至少选择一个拍照选项");
            return;
        }

        if (photoTempFileName == null) {
            ToastUtils.show("上传照片失败，请重新录音");
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
                        VideoConfirmActivity.this.finish();
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
                    result.put("picture", photoTempFileName);
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
