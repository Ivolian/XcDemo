package com.unicorn.csp.xcdemo.activity.technician;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapSize;
import com.liangfeizc.flowlayout.FlowLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.base.ToolbarActivity;
import com.unicorn.csp.xcdemo.component.OptionButton;
import com.unicorn.csp.xcdemo.component.PaperButton;
import com.unicorn.csp.xcdemo.component.TinyDB;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.utils.JSONUtils;
import com.unicorn.csp.xcdemo.utils.ToastUtils;
import com.unicorn.csp.xcdemo.volley.JSONArrayRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;


public class MicActivity extends ToolbarActivity {


    // ========================== extra ==========================

//    @InjectExtra("workOrderProcessInfo")
//    WorkOrderProcessInfo workOrderProcessInfo;


    // ========================== onCreate ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mic);
        initToolbar("录音", true);
        initViews();
        enableSlideFinish();
    }

    private void initViews() {
        fetchOptions();
        initEtDescription();
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


    // ========================== description ==========================

    @Bind(R.id.et_description)
    BootstrapEditText etDescription;

    private void initEtDescription() {
        etDescription.setGravity(Gravity.TOP);
        etDescription.setPadding(20, 20, 20, 20);
        etDescription.setBootstrapSize(DefaultBootstrapSize.MD);
    }


    // ========================== record ==========================

    @Bind(R.id.btn_record)
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
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            //
        }
        mRecorder.start();
        btnRecord.setText("停止录音");
    }

    private String getRecordFilePath() {
        return ConfigUtils.getBaseDirPath() + File.separator + "1.mp3";
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        btnRecord.setText("播放");

        uploadRecord(DialogUtils.showMask(this, "上传录音中", "请稍后"));
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


    // ========================== upload record ==========================

    String recordTempFileName;

    private void uploadRecord(final MaterialDialog mask) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = ConfigUtils.getBaseUrl() + "/api/v1/system/file/upload";
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("attachment", new File(getRecordFilePath()));
        } catch (Exception e) {
            //
        }
        client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                mask.dismiss();
                String jsonObjectString = new String(bytes);
                try {
                    JSONObject jsonObject = new JSONObject(jsonObjectString);
                    recordTempFileName = JSONUtils.getString(jsonObject, "tempFileName", "");
                } catch (Exception e) {
                    //
                }
                ToastUtils.show("上传成功");
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                mask.dismiss();
                ToastUtils.show("上传失败");
            }
        });
    }


    // ========================== confirm record ==========================

    @OnClick(R.id.btn_confirm)
    public void confirm() {
        final OptionButton optionSelected = getOptionSelected();
        if (optionSelected == null) {
            ToastUtils.show("至少选择一个录音选项");
            return;
        }

        if (recordTempFileName == null) {
            ToastUtils.show("上传录音失败，请重新录音");
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
                        MicActivity.this.finish();
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
                    result.put("picture", recordTempFileName);
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
