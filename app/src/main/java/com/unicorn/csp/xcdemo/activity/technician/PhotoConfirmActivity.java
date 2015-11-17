package com.unicorn.csp.xcdemo.activity.technician;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.f2prateek.dart.InjectExtra;
import com.liangfeizc.flowlayout.FlowLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.MyButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.ImageUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import uk.co.senab.photoview.PhotoViewAttacher;


public class PhotoConfirmActivity extends ToolbarActivity {

    // ========================== extra ==========================

    @InjectExtra("photoPath")
    String photoPath;

    @InjectExtra("workOrderProcessInfo")
    WorkOrderProcessInfo workOrderProcessInfo;


    // ========================== view ==========================

    @Bind(R.id.iv_photo)
    ImageView ivPhoto;

    @Bind(R.id.et_description)
    BootstrapEditText etDescription;

    String photoTempFileName = "";

    @Bind(R.id.fl_options)
    FlowLayout flOptions;

    List<MyButton> buttonList = new ArrayList<>();


    // ========================== onCreate ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_confirm);
        initToolbar("拍照确认", true);
        initViews();
        enableSlideFinish();
    }

    private void initViews() {
        initIvPhoto();
        initEtDescription();
        uploadPhoto();
        fetchOptions();
    }


    private void fetchOptions() {

        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/picture/options";
        SimpleVolley.getRequestQueue().add(
                new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                for (int i = 0; i != response.length(); i++) {
                                    JSONObject jsonObject = JSONUtils.getJSONObject(response, i);
                                    String objectId = JSONUtils.getString(jsonObject, "objectId", "");
                                    String name = JSONUtils.getString(jsonObject, "name", "");
                                    MyButton myButton = getSuspendOptionButton(name);
                                    myButton.name = name;
                                    myButton.objectId = objectId;
                                    flOptions.addView(myButton, layoutParams);
                                    buttonList.add(myButton);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        String jsessionid = TinyDB.getInstance().getString(ConfigUtils.JSESSION_ID);
                        map.put("Cookie", "JSESSIONID=" + jsessionid);
                        return map;
                    }
                }
        );

    }


    private MyButton getSuspendOptionButton(String suspendOptionText) {

        final MyButton btnSuspendOption = new MyButton(this);
        btnSuspendOption.setText(suspendOptionText);
        btnSuspendOption.setPadding(4, 4, 4, 4);
        btnSuspendOption.setBootstrapBrand(DefaultBootstrapBrand.INFO);
        btnSuspendOption.setBootstrapSize(DefaultBootstrapSize.MD);
        btnSuspendOption.setRounded(true);
        btnSuspendOption.setShowOutline(true);
        btnSuspendOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSuspendOption.isShowOutline()) {
                    for (MyButton myButton : buttonList) {
                        if (myButton != v) {
                            myButton.setShowOutline(true);
                        }
                    }
                }

                btnSuspendOption.setShowOutline(!btnSuspendOption.isShowOutline());
            }
        });
        return btnSuspendOption;
    }


    private void uploadPhoto() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("attachment", new File(photoPath));
        } catch (Exception e) {
            //
        }
        String url = ConfigUtils.getBaseUrl() + "/api/v1/system/file/upload";
        client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                String str = new String(bytes);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(str);
                } catch (Exception e) {
                    //
                }

                photoTempFileName = JSONUtils.getString(jsonObject, "tempFileName", "");

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    private void initIvPhoto() {
        ivPhoto.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        BitmapFactory.Options options = ImageUtils.getFactoryOptions(ivPhoto.getWidth(), ivPhoto.getHeight(), photoPath);
                        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
                        ivPhoto.setImageBitmap(bitmap);
                        new PhotoViewAttacher(ivPhoto);
                        ivPhoto.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    private void initEtDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


    // ========================== onClick ==========================


//    public void complete(@PathVariable("objectId") String objectId, @RequestBody WorkOrderPicture workOrderPicture)
//       private Code option;

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        if (photoTempFileName.equals("")) {
            ToastUtils.show("照片上传中...");
            return;
        }


        MyButton btnSelected = null;
        for (MyButton myButton : buttonList) {
            if (!myButton.isShowOutline()) {
                btnSelected = myButton;
            }
        }
        if (btnSelected == null) {
            ToastUtils.show("请至少选择一个拍照选项");
            return;
        }

        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId() + "/picture";
        SimpleVolley.getRequestQueue().add(
                new StringRequest(
                        Request.Method.PUT,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ToastUtils.show("确认完成!");
                                PhotoConfirmActivity.this.finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
                            }
                        }
                ) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        JSONObject result = new JSONObject();
                        MyButton btnSelected = null;
                        for (MyButton myButton : buttonList) {
                            if (!myButton.isShowOutline()) {
                                btnSelected = myButton;
                            }
                        }
                        String codeName = btnSelected.name;
                        String codeId = btnSelected.objectId;
                        JSONObject code = new JSONObject();

                        try {
                            code.put("name", codeName);
                            code.put("objectId", codeId);
                            result.put("option", code);
                            result.put("remark", etDescription.getText().toString().trim());
                            result.put("picture", photoTempFileName);
                        } catch (Exception e) {
                            //
                        }
                        String json = result.toString();
                        byte[] bytes = null;
                        try {
                            bytes = json.getBytes("UTF-8");
                        } catch (Exception e) {
                            //
                        }
                        return bytes;
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
                }
        );
    }

}
