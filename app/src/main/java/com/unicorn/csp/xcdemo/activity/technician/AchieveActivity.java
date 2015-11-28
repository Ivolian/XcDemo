package com.unicorn.csp.xcdemo.activity.technician;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.f2prateek.dart.InjectExtra;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.WorkOrderCardActivity;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.component.UploadUtils;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import io.techery.properratingbar.ProperRatingBar;


//@P
public class AchieveActivity extends WorkOrderCardActivity {


    // ================================== onCreate ==================================

    @InjectExtra("workOrderInfo")
    WorkOrderInfo workOrderInfo;


    // ================================== onCreate & onDestroy ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_achieve);
        initToolbar("结单", true);
        initViews();
        enableSlideFinish();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initViews() {
        initRatingBar();
        initDescription();
    }


    // ================================== sign ==================================


    @OnClick(R.id.btn_sign)
    public void showSignDialog() {
        new MaterialDialog.Builder(this)
                .customView(R.layout.sign_pad, false)
                .negativeText("重写")
                .positiveText("确认")
                .autoDismiss(false)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        if (materialDialog.getCustomView() != null) {
                            final SignaturePad signaturePad = (SignaturePad) materialDialog.getCustomView().findViewById(R.id.signature_pad);
                            if (dialogAction == DialogAction.NEGATIVE) {
                                signaturePad.clear();
                            } else if (dialogAction == DialogAction.POSITIVE) {
                                materialDialog.dismiss();
                                Bitmap signBitmap = signaturePad.getSignatureBitmap();
                                String signPath = ConfigUtils.getBaseDirPath() + "/sign.png";
                                File signFile = new File(signPath);
                                if (signFile.exists()) {
                                    signFile.delete();
                                }
                                try {
                                    signFile.createNewFile();
                                    FileOutputStream out = new FileOutputStream(signFile);
                                    signBitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                    //
                                }
                                uploadSign(signPath);
                            }
                        }
                    }
                }).show();
    }


    // ================================== upload ==================================

    private String signTempFileName;

    private void uploadSign(String signPath) {
        UploadUtils.upload(new File(signPath), "achieveActivity_onUploadFinish", DialogUtils.showMask(this, "上传签名中", "请稍后"));
    }

    @Subscriber(tag = "achieveActivity_onUploadFinish")
    private void onUploadFinish(String tempFileName) {
        signTempFileName = tempFileName;
    }


    // ================================== rating ==================================

    @Bind(R.id.ratingBar)
    ProperRatingBar ratingBar;

    private void initRatingBar() {
        ratingBar.setTickNormalDrawable(getNormalDrawable());
        ratingBar.setTickSelectedDrawable(getSelectedDrawable());
        ratingBar.redrawChildren();
    }

    private Drawable getSelectedDrawable() {
        return new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_star)
                .color(ContextCompat.getColor(this, R.color.md_orange_400))
                .sizeDp(28);
    }

    private Drawable getNormalDrawable() {
        return new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_star_outline)
                .color(ContextCompat.getColor(this, R.color.md_orange_400))
                .sizeDp(28);
    }


    // ================================== description ==================================

    @Bind(R.id.et_description)
    BootstrapEditText etDescription;

    private void initDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


    // ================================== achieve ==================================

    @OnClick(R.id.btn_achieve)
    public void achieveConfrim() {
        DialogUtils.showConfirm(this, "确认结单？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                achieve("/complete");
            }
        });
    }

    @OnClick(R.id.btn_review)
    public void receiveConfrim() {
        DialogUtils.showConfirm(this, "确认待复核？", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                achieve("/review");
            }
        });
    }

    private void achieve(final String lastPartUrl) {
        if (signTempFileName == null) {
            ToastUtils.show("请先签名");
            return;
        }
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderInfo.getWorkOrderId() + lastPartUrl;
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show(lastPartUrl.equals("/review") ? "带复核成功" : "结单成功!");
                        // todo
//                                EventBus.getDefault().post("", "suspendRefresh");
                        finish();
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject result = new JSONObject();
                    result.put("sign", signTempFileName);
                    result.put("evaluate", ratingBar.getRating());
                    result.put("remark", etDescription.getText().toString().trim());
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
                String jsessionid = TinyDB.getInstance().getString(ConfigUtils.JSESSION_ID);
                map.put("Cookie", "JSESSIONID=" + jsessionid);
                // 不加这个会出现 415 错误
                map.put("Content-Type", "application/json");
                return map;
            }
        };
        SimpleVolley.addRequest(stringRequest);
    }

}

