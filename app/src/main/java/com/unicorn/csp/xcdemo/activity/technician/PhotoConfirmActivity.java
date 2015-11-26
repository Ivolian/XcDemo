package com.unicorn.csp.xcdemo.activity.technician;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.OptionButton;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.ImageUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JSONArrayRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;


public class PhotoConfirmActivity extends ToolbarActivity {


    // ========================== extra ==========================

//    @InjectExtra("workOrderProcessInfo")
//    WorkOrderProcessInfo workOrderProcessInfo;


    // ========================== view ==========================


    @Bind(R.id.et_description)
    BootstrapEditText etDescription;



    // ========================== onCreate ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_confirm);
        initToolbar("拍照确认", true);
        enableSlideFinish();
        autoTakePhoto();
    }


    // ========================== take photo ==========================

    final int TAKE_PHOTO_REQUEST_CODE = 2333;

    String photoPath;

    @Bind(R.id.iv_photo)
    ImageView ivPhoto;

    private void autoTakePhoto() {
        ivPhoto.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                takePhoto();
                ivPhoto.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri randomUri = ImageUtils.getRandomPhotoUri();
        photoPath = randomUri.getPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, randomUri);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
    }


    // ========================== after take photo ==========================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            displayPhoto();
            fetchOptions();
        } else {
            finish();
        }
    }

    private void displayPhoto() {
        BitmapFactory.Options options = ImageUtils.getFactoryOptions(ivPhoto.getWidth(), ivPhoto.getHeight(), photoPath);
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        ivPhoto.setImageBitmap(bitmap);
        new PhotoViewAttacher(ivPhoto);
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

    private void uploadPhoto() {
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams requestParams = new RequestParams();
//
//        String compressPhotoPath = ImageUtils.compressPhoto(photoPath);
//        try {
//            requestParams.put("attachment", new File(compressPhotoPath));
//        } catch (Exception e) {
//            //
//        }
//
//
//        String url = ConfigUtils.getBaseUrl() + "/api/v1/system/file/upload";
//        client.post(url, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//
//                String str = new String(bytes);
//
//                JSONObject jsonObject = null;
//                try {
//                    jsonObject = new JSONObject(str);
//                } catch (Exception e) {
//                    //
//                }
//
//                photoTempFileName = JSONUtils.getString(jsonObject, "tempFileName", "");
//
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                ToastUtils.show("失败");
//            }
//        });
    }


    private void initEtDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


    // ========================== onClick ==========================


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

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        OptionButton optionSelected = getOptionSelected();
        if (optionSelected == null) {
            ToastUtils.show("至少选择一个拍照选项");
            return;
        }

//        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId() + "/picture";
//        SimpleVolley.getRequestQueue().add(
//                new StringRequest(
//                        Request.Method.PUT,
//                        "",
////                        url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//                                ToastUtils.show("确认完成!");
//                                PhotoConfirmActivity.this.finish();
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                ToastUtils.show(VolleyErrorHelper.getErrorMessage(error));
//                            }
//                        }
//                ) {
//                    @Override
//                    public byte[] getBody() throws AuthFailureError {
//                        JSONObject result = new JSONObject();
//                        OptionButton btnSelected = null;
//                        for (OptionButton optionButton : buttonList) {
//                            if (!optionButton.isShowOutline()) {
//                                btnSelected = optionButton;
//                            }
//                        }
//                        String codeName = btnSelected.name;
//                        String codeId = btnSelected.objectId;
//                        JSONObject code = new JSONObject();
//
//                        try {
//                            code.put("name", codeName);
//                            code.put("objectId", codeId);
//                            result.put("option", code);
//                            result.put("remark", etDescription.getText().toString().trim());
//                            result.put("picture", photoTempFileName);
//                        } catch (Exception e) {
//                            //
//                        }
//                        String json = result.toString();
//                        byte[] bytes = null;
//                        try {
//                            bytes = json.getBytes("UTF-8");
//                        } catch (Exception e) {
//                            //
//                        }
//                        return bytes;
//                    }
//
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> map = new HashMap<>();
//                        String jsessionid = TinyDB.getInstance().getString(ConfigUtils.JSESSION_ID);
//                        map.put("Cookie", "JSESSIONID=" + jsessionid);
//                        // 不加这个会出现 415 错误
//                        map.put("Content-Type", "application/json");
//                        return map;
//                    }
//                }
//        );
    }

}
