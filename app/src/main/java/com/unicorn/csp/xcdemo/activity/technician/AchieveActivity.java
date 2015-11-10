package com.unicorn.csp.xcdemo.activity.technician;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.Gravity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;
import io.techery.properratingbar.ProperRatingBar;


//@P
public class AchieveActivity extends ToolbarActivity {




    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve);
        initToolbar("结单", true);
        initViews();
        slidr();
    }

    private void initViews() {

        initRatingBar();
        initAchieveDescription();
    }


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
                            SignaturePad signaturePad = (SignaturePad) materialDialog.getCustomView().findViewById(R.id.signature_pad);
                            if (dialogAction == DialogAction.NEGATIVE) {
                                signaturePad.clear();
                            } else if (dialogAction == DialogAction.POSITIVE) {
                                materialDialog.dismiss();
                            }
                        }
                    }
                })
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

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "csp");
        if (!dir.exists()) {
            boolean result = dir.mkdir();
        }
        return dir.getAbsolutePath() + "/1.mp3";
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
                .color(getResources().getColor(R.color.md_orange_300))
                .sizeDp(28);
    }

    private Drawable getNormalDrawable() {

        return new IconicsDrawable(this)
                .icon(GoogleMaterial.Icon.gmd_star_outline)
                .color(getResources().getColor(R.color.md_orange_300))
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

}

