package com.unicorn.csp.xcdemo.activity.technician;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
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
import com.unicorn.csp.xcdemo.activity.shared.base.WorkOrderCardActivity;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.component.UploadUtils;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.techery.properratingbar.ProperRatingBar;


//@P
public class AchieveActivity extends WorkOrderCardActivity {


    // ================================== extra ==================================

    @InjectExtra("refreshEventTag")
    String refreshEventTag;


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
        initRatingBars();
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

    private String signTempFileName = "";

    private void uploadSign(String signPath) {
        UploadUtils.upload(new File(signPath), "achieveActivity_onUploadFinish", DialogUtils.showMask(this, "上传签名中", "请稍后"));
    }

    @Subscriber(tag = "achieveActivity_onUploadFinish")
    private void onUploadFinish(String tempFileName) {
        signTempFileName = tempFileName;
    }


    // ================================== rating ==================================

    @BindView(R.id.rbResponseSpeed)
    ProperRatingBar rbResponseSpeed;

    @BindView(R.id.rbServiceAttitude)
    ProperRatingBar rbServiceAttitude;

    @BindView(R.id.rbPreservation)
    ProperRatingBar rbPreservation;

    @BindView(R.id.rbSkillLevel)
    ProperRatingBar rbSkillLevel;

    @BindView(R.id.rbEvaluate)
    ProperRatingBar rbEvaluate;

    private void initRatingBars() {
        initRatingBar(rbResponseSpeed);
        initRatingBar(rbServiceAttitude);
        initRatingBar(rbPreservation);
        initRatingBar(rbSkillLevel);
        initRatingBar(rbEvaluate);
    }

    private void initRatingBar(ProperRatingBar ratingBar) {
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

    @BindView(R.id.et_description)
    BootstrapEditText etDescription;

    private void initDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


    // ================================== achieve ==================================

    @OnClick(R.id.btn_achieve)
    public void achieveConfirm() {
        if (isUserInputValid()) {
            DialogUtils.showConfirm(this, "确认结单？", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                    achieve("/complete");
                }
            });
        }
    }

    @OnClick(R.id.btn_review)
    public void receiveConfrim() {
        if (isUserInputValid()) {
            DialogUtils.showConfirm(this, "确认待复核？", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                    achieve("/review");
                }
            });
        }
    }

    private boolean isUserInputValid() {
        if (recordTempFileName.equals("") && signTempFileName.equals("")) {
            ToastUtils.show("需要报修人签名或录音");
            return false;
        }
        return true;
    }

    private void achieve(final String lastPartUrl) {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/" + workOrderProcessInfo.getWorkOrderInfo().getWorkOrderId() + lastPartUrl;
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ToastUtils.show(lastPartUrl.equals("/review") ? "待复核成功" : "结单成功!");
                        EventBus.getDefault().post(new Object(), refreshEventTag);
                        finish();
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject result = new JSONObject();
                    result.put("soundRecord", recordTempFileName);
                    result.put("sign", signTempFileName);
                    result.put("responseSpeed", rbResponseSpeed.getRating());
                    result.put("serviceAttitude", rbServiceAttitude.getRating());
                    result.put("preservation", rbPreservation.getRating());
                    result.put("skillLevel", rbSkillLevel.getRating());
                    result.put("evaluate", rbEvaluate.getRating());
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


    // 录音

    @BindView(R.id.btn_record)
    PaperButton btnRecord;

    MediaRecorder mRecorder = null;

    MediaPlayer mPlayer = null;

    @OnClick(R.id.btn_record)
    public void record() {
        switch (btnRecord.getText()) {
            case "录音":
                startRecording();
                break;
            case "停止录音":
                stopRecording();
                break;
            case "播放":
                startPlaying();
                break;
            case "停止播放":
                stopPlaying();
                break;
        }
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setOutputFile(getRecordFilePath());
        // 编码问题可能会导致有些播放器无法播放
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            //
        }
        mRecorder.start();
        btnRecord.setText("停止录音");
    }

    private String getRecordFilePath() {
        return ConfigUtils.getBaseDirPath() + File.separator + "record.mp3";
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        btnRecord.setText("播放");
        uploadRecord();
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


    // ========================== upload ==========================

    String recordTempFileName = "";

    private void uploadRecord() {
        UploadUtils.upload(new File(getRecordFilePath()), "achieve_onUploadFinish", DialogUtils.showMask2(this, "上传录音中", "请稍后"));
    }

    @Subscriber(tag = "achieve_onUploadFinish")
    private void onAchieveUploadFinish(String tempFileName) {
        recordTempFileName = tempFileName;
    }

}

