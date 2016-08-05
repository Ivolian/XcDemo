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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.f2prateek.dart.InjectExtra;
import com.liangfeizc.flowlayout.FlowLayout;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.OptionButton;
import com.unicorn.csp.xcdemo.component.UploadUtils;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.ImageUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JSONArrayRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;


public class PhotoConfirmActivity extends ToolbarActivity {


    // ========================== extra ==========================

    @InjectExtra("workOrderId")
    String workOrderId;


    // ========================== onCreate & onDestroy ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_photo_confirm);
        initToolbar("拍照确认", true);
        enableSlideFinish();
        autoTakePhoto();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    // ========================== take photo ==========================

    final int TAKE_PHOTO_REQUEST_CODE = 2333;

    String photoPath;

    @BindView(R.id.iv_photo)
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
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            showPhoto();
            uploadPhoto();
            initOtherViews();
        } else {
            finish();
        }
    }

    private void showPhoto() {
        BitmapFactory.Options options = ImageUtils.getFactoryOptions(ivPhoto.getWidth(), ivPhoto.getHeight(), photoPath);
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        ivPhoto.setImageBitmap(bitmap);
        new PhotoViewAttacher(ivPhoto);
    }

    private void initOtherViews() {
        fetchOptions();
        initEtDescription();
    }


    // ========================== options ==========================

    @BindView(R.id.fl_options)
    FlowLayout flOptions;

    private void fetchOptions() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/attachment/options?type=WorkOrderPictureOptions";
        JsonArrayRequest jsonArrayRequest = new JSONArrayRequestWithSessionCheck(
                Request.Method.GET,
                url,
        new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i != response.length(); i++) {
                    JSONObject jsonObject = JSONUtils.getJSONObject(response, i);
                    String objectId = JSONUtils.getString(jsonObject, "objectId", "");
                    String name = JSONUtils.getString(jsonObject, "name", "");
                    flOptions.addView(getOptionButton(name, objectId), getOptionButtonLayoutParams());
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

    private ViewGroup.LayoutParams getOptionButtonLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    // ========================== description ==========================

    @BindView(R.id.et_description)
    BootstrapEditText etDescription;

    private void initEtDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


    // ========================== upload ==========================

    String photoTempFileName;

    private void uploadPhoto() {
        String compressPhotoPath = ImageUtils.compressPhoto(photoPath);
        UploadUtils.upload(new File(compressPhotoPath), "photoConfirmActivity_onUploadFinish", DialogUtils.showMask2(this, "上传照片中", "请稍后"));
    }

    @Subscriber(tag = "photoConfirmActivity_onUploadFinish")
    private void onUploadFinish(String tempFileName) {
        photoTempFileName = tempFileName;
    }


    // ========================== confirm ==========================

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        final OptionButton optionSelected = getOptionSelected();
        if (optionSelected == null) {
            ToastUtils.show("至少选择一个拍照选项");
            return;
        }
        if (photoTempFileName == null) {
            ToastUtils.show("上传照片失败，请重新拍照");
            return;
        }
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderId + "/picture";
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show("确认完成!");
                        PhotoConfirmActivity.this.finish();
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
                    result.put("filename", photoTempFileName);
                    return result.toString().getBytes("UTF-8");
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
