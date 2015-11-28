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
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import io.techery.properratingbar.ProperRatingBar;


//@P
public class AchieveActivity extends ToolbarActivity {


    //

//    @InjectExtra("workOrderProcessInfo")
//    WorkOrderProcessInfo workOrderProcessInfo;

    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve);
        initToolbar("结单", true);
        initViews();
        enableSlideFinish();
    }

    private void initViews() {
        initRatingBar();
        initDescription();
    }


    private String signTempFileName;

    MaterialDialog signPadDialog = null;


    @OnClick(R.id.btn_sign)
    public void showSignDialog() {

        if (signPadDialog != null) {
            signPadDialog.show();
            return;
        }
        signPadDialog = new MaterialDialog.Builder(this)
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

                                final MaterialDialog mask = DialogUtils.showMask(AchieveActivity.this, "上传签名中", "请稍后");
                                Bitmap bitmap = signaturePad.getSignatureBitmap();
                                String signPngPath = ConfigUtils.getBaseDirPath() + "/1.png";
                                File signFile = new File(signPngPath);
                                if (signFile.exists()) {
                                    signFile.delete();
                                }
                                try {
                                    signFile.createNewFile();

                                    FileOutputStream out = new FileOutputStream(signFile);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                                    out.flush();
                                    out.close();
                                } catch (Exception e) {
                                    //
                                }


                                AsyncHttpClient client = new AsyncHttpClient();
                                RequestParams requestParams = new RequestParams();
                                try {
                                    requestParams.put("attachment", new File(signPngPath));
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

                                        signTempFileName = JSONUtils.getString(jsonObject, "tempFileName", "");
                                        ToastUtils.show("上传成功");
                                        mask.dismiss();
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                                        ToastUtils.show("上传失败");
                                        mask.dismiss();
                                    }
                                });

                            }
                        }
                    }
                })
                .show();
    }


    // ================================== 评分功能 ==================================

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


    // ================================== 结单说明 ==================================

    @Bind(R.id.et_description)
    BootstrapEditText etDescription;

    private void initDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


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

        WorkOrderProcessInfo workOrderProcessInfo = (WorkOrderProcessInfo) getIntent().getSerializableExtra("workOrderProcessInfo");
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId() + lastPartUrl;
        SimpleVolley.getRequestQueue().add(
                new StringRequest(
                        Request.Method.PUT,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ToastUtils.show(lastPartUrl.equals("/review") ? "带复核成功" : "结单成功!");
//                                EventBus.getDefault().post("", "suspendRefresh");
                                setResult(333);
                                finish();
                            }
                        },
                        SimpleVolley.getDefaultErrorListener()
                ) {
                    @Override
                    public byte[] getBody() throws AuthFailureError {

                        JSONObject result = new JSONObject();
                        try {
                            result.put("sign", signTempFileName);
//                            result.put("soundRecord", recordTempFileName);
                            result.put("evaluate", ratingBar.getRating());
                            result.put("remark", etDescription.getText().toString().trim());
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

