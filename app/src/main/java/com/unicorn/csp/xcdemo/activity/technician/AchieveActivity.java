package com.unicorn.csp.xcdemo.activity.technician;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.f2prateek.dart.InjectExtra;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.model.WorkOrderInfo;
import com.unicorn.csp.xcdemo.model.WorkOrderProcessInfo;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;
import com.unicorn.csp.xcdemo.volley.VolleyErrorHelper;
import com.wangqiang.libs.labelviewlib.LabelView;

import org.joda.time.DateTime;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import io.techery.properratingbar.ProperRatingBar;


// todo change achieve to complete
//@P
public class AchieveActivity extends ToolbarActivity {


    @Bind(R.id.labelview)
    LabelView labelView;

    @Bind(R.id.tv_request_user_and_call_number)
    TextView tvRequestUserAndCallNumber;

    @Bind(R.id.tv_request_time)
    TextView tvRequestTime;

    @Bind(R.id.tv_building_and_address)
    TextView tvBuildingAndAddress;

    @Bind(R.id.tv_type)
    TextView tvType;

    @Bind(R.id.tv_equipment_and_fault_type)
    TextView tvEquipmentAndFaultType;

    @Bind(R.id.tv_processing_time_limit)
    TextView tvProcessingTimeLimit;

    @Bind(R.id.tv_issuer)
    TextView tvIssuer;

    @Bind(R.id.tv_distribute_time)
    TextView tvDistributeTime;

    @Bind(R.id.tv_distributor)
    TextView tvDistributor;

    @InjectExtra("workOrderProcessInfo")
    WorkOrderProcessInfo workOrderProcessInfo;

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
        WorkOrderInfo workOrderInfo = workOrderProcessInfo.getWorkOrderInfo();
        String requestUserAndCallNumber = "报修电话: " + workOrderInfo.getCallNumber() + " " + workOrderInfo.getRequestUser();
        tvRequestUserAndCallNumber.setText(requestUserAndCallNumber);
        String requestTime = "报修时间: " + new DateTime(workOrderInfo.getRequestTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvRequestTime.setText(requestTime);
        String buildingAndAddress = "保修地点: " + workOrderInfo.getBuilding() + "(" + workOrderInfo.getAddress() + ")";
        tvBuildingAndAddress.setText(buildingAndAddress);
        String type = "维修类型: " + workOrderInfo.getType();
        tvType.setText(type);
        String equipmentAndFaultType = "维修内容: " + workOrderInfo.getEquipment() + "(" + workOrderInfo.getFaultType() + ")";
        tvEquipmentAndFaultType.setText(equipmentAndFaultType);
        String processingTimeLimit = "是否时限: " + workOrderInfo.getProcessingTimeLimit();
        tvProcessingTimeLimit.setText(processingTimeLimit);
        String issuer = "受理人员: " + workOrderInfo.getIssuer();
        tvIssuer.setText(issuer);
        String distributeTime = "派单时间: " + new DateTime(workOrderInfo.getDistributeTime()).toString("yyyy-MM-dd HH:mm:ss");
        tvDistributeTime.setText(distributeTime);
        String distributor = "拍单人员: " + workOrderInfo.getDistributor();

        tvDistributor.setText(distributor);

        String statusTag = workOrderInfo.getStatusTag();
        labelView.setText(statusTag.equals("Distribute") ? "派" : "抢");

        if (workOrderInfo.getDistributor() == null) {
            tvDistributeTime.setVisibility(View.GONE);
            tvDistributor.setVisibility(View.GONE);
        }
        initRatingBar();
        initAchieveDescription();
    }

    private String signTempFileName;

    private String recordTempFileName;

    // ================================== 签名功能 ==================================

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

                                final MaterialDialog mask = showMask("上传签名中");
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


    private MaterialDialog showMask(String title) {
        return new MaterialDialog.Builder(this)
                .title(title)
                .content("请稍后...")
                .progress(true, 0)
                .cancelable(false)
                .show();
    }


    // ================================== 录音和播放功能 ==================================

    @Bind(R.id.btn_record)
    PaperButton btnRecord;

    MediaRecorder mRecorder = null;

    MediaPlayer mPlayer = null;

    @OnClick(R.id.btn_record)
    public void record() {
        String btnText = btnRecord.getText();
        if (btnText.equals("录音")) {
            startRecording();
        }
        if (btnText.equals("停止录音")) {
            stopRecording();
        }
        if (btnText.equals("播放")) {
            startPlaying();
        }
        if (btnText.equals("停止播放")) {
            stopPlaying();
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(getRecordFilePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            //
        }
        mRecorder.start();
        btnRecord.setText("停止录音");
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        btnRecord.setText("播放");

        final MaterialDialog mask = showMask("上传录音中");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("attachment", new File(getRecordFilePath()));
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

                recordTempFileName = JSONUtils.getString(jsonObject, "tempFileName", "");
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

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(getRecordFilePath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            //
        }
        btnRecord.setText("停止播放");
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
        btnRecord.setText("录音");
    }

    private String getRecordFilePath() {

        return ConfigUtils.getBaseDirPath() + "/1.mp3";
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

    @Bind(R.id.et_achieve_description)
    BootstrapEditText etAchieveDescription;

    private void initAchieveDescription() {

        etAchieveDescription.setGravity(Gravity.TOP);
        etAchieveDescription.setPadding(20, 20, 20, 20);
        etAchieveDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


    // ================================== finish ==================================

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @OnClick(R.id.btn_achieve)
    public void achieveConfrim() {
        new MaterialDialog.Builder(this)
                .content("确认结单？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        achieve("/complete");
                    }
                })
                .show();
    }

    @OnClick(R.id.btn_review)
    public void receiveConfrim() {
        new MaterialDialog.Builder(this)
                .content("确认待复核？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        achieve("/review");
                    }
                })
                .show();
    }


    public void achieve(final String lastPartUrl) {
        if (recordTempFileName == null) {
            ToastUtils.show("请先录音");
            return;
        }
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
                        try {
                            result.put("sign", signTempFileName);
                            result.put("soundRecord", recordTempFileName);
                            result.put("evaluate", ratingBar.getRating());
                            result.put("remark", etAchieveDescription.getText().toString().trim());
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

