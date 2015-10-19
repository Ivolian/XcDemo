package com.unicorn.csp.xcdemo.activity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.OnClick;

public class OverActivity extends ToolbarActivity {

    private MediaRecorder mRecorder = null;

    private MediaPlayer mPlayer = null;


    @Bind(R.id.btn_record)
    AppCompatButton btnRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over);
        initToolbar("结单", true);
    }


    @OnClick(R.id.btn_record)
    public void record() {
        String btnText = btnRecord.getText().toString();
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

    @OnClick(R.id.btn_sign)
    public void sign() {

        new MaterialDialog.Builder(this)
                .customView(R.layout.sign_pad, false)
                .autoDismiss(false)
                .negativeText("重写")
                .positiveText("确认")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        if (materialDialog.getCustomView() != null) {
                            SignaturePad signaturePad = (SignaturePad) materialDialog.getCustomView().findViewById(R.id.signature_pad);
                            signaturePad.clear();
                        }
                    }
                }).show();
    }

    public String get3GpPath() {

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "csp");
        if (!dir.exists()) {
           boolean result =  dir.mkdir();
        }
        return dir.getAbsolutePath() + "/audiorecordtest.3";
    }

    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(get3GpPath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

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
            mPlayer.setDataSource(get3GpPath());
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

}
