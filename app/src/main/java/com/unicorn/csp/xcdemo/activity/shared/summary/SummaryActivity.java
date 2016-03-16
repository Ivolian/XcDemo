package com.unicorn.csp.xcdemo.activity.shared.summary;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.devspark.robototextview.widget.RobotoTextView;
import com.google.gson.Gson;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ButterKnifeActivity;
import com.unicorn.csp.xcdemo.activity.shared.summary.model.DayData;
import com.unicorn.csp.xcdemo.activity.shared.summary.model.SummaryData;
import com.unicorn.csp.xcdemo.activity.shared.summary.model.WorkOrderStatus;
import com.unicorn.csp.xcdemo.utils.ConfigUtils;
import com.unicorn.csp.xcdemo.utils.DialogUtils;
import com.unicorn.csp.xcdemo.volley.JsonObjectRequestWithSessionCheck;
import com.unicorn.csp.xcdemo.volley.SimpleVolley;

import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class SummaryActivity extends ButterKnifeActivity {


    // ================================== onCreate ==================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        initView();
    }

    private void initView() {
        fetchSummaryData();
        fetchDayData();
    }


    // ================================== 总汇信息 ==================================

    private void fetchSummaryData() {
        Request request = new JsonObjectRequestWithSessionCheck(
                getSummaryDataUrl(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SummaryData summaryData = new Gson().fromJson(response.toString(), SummaryData.class);
                        afterFetchSummaryData(summaryData);
                    }
                },
                SimpleVolley.getDefaultErrorListener()
        );
        SimpleVolley.addRequest(request);
    }

    @Bind(R.id.summary)
    RobotoTextView rtvSummary;

    @Bind(R.id.avg)
    TextView tvAvg;

    @Bind(R.id.issue)
    TextView tvIssue;

    @Bind(R.id.complete)
    TextView tvComplete;

    private void afterFetchSummaryData(SummaryData summaryData) {
        WorkOrderStatus workOrderStatus = summaryData.getWorkOrderStatus();
        rtvSummary.setText(workOrderStatus.getIssue() + "");
        long avgArriveTime = summaryData.getAvgArriveTime() / 3600 / 1000;
        long avgCompleteTime = summaryData.getAvgCompleteTime() / 3600 / 1000;
        String avg = "平均响应时间:" + avgArriveTime + "小时,  平均完成时间:" + avgCompleteTime + "小时";
        tvAvg.setText(avg);
        tvIssue.setText(workOrderStatus.getIssue() + "项");
        tvComplete.setText(workOrderStatus.getComplete() + "项");
    }

    private String getSummaryDataUrl() {
        Uri.Builder builder = Uri.parse(ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/summary?").buildUpon();
        builder.appendQueryParameter("startDate", "2016-01-10");
        builder.appendQueryParameter("endDate", "2016-03-16");
        return builder.toString();
    }


    // ================================== 每日数据 ==================================

    private void fetchDayData() {
        String url = ConfigUtils.getBaseUrl() + "/api/v1/hems/workOrder/statistics/day";
        final MaterialDialog mask = DialogUtils.showMask(this, "加载数据中", "请稍后");
        Request request = new JsonObjectRequestWithSessionCheck(
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mask.dismiss();
                        DayData dayData = new Gson().fromJson(response.toString(), DayData.class);
                        showChart(dayData);
                    }
                },
                SimpleVolley.getDefaultErrorListener(mask)
        );
        SimpleVolley.addRequest(request);
    }

    @Bind(R.id.chart1)
    LineChartView mChart;

    private void showChart(DayData dayData) {
        List<String> categorys = dayData.getCategories();
        List<Integer> total = dayData.getSeries().getTotal();
        List<Integer> complete = dayData.getSeries().getComplete();

        String[] labels = new String[categorys.size()];
        for (String category : categorys) {
            labels[categorys.indexOf(category)] = category.substring(5);
        }

        float[] values = new float[total.size()];
        for (Integer t : total) {
            int index = total.indexOf(t);
            values[index] = t;
        }

        LineSet datasetIssue = new LineSet(labels, values);
        datasetIssue.setColor(getResources().getColor(R.color.md_light_blue_500))
                .setSmooth(true)
                .setDotsColor(getResources().getColor(R.color.md_light_blue_600))
                .setDashed(new float[]{10f, 10f})
                .setThickness(4);
        mChart.addData(datasetIssue);

        for (Integer c : complete) {
            int index = complete.indexOf(c);
            values[index] = c;
        }
        datasetIssue = new LineSet(labels, values);
        datasetIssue.setColor(getResources().getColor(R.color.md_teal_300))
                .setDotsColor(getResources().getColor(R.color.md_teal_400))
                .setThickness(4);
        mChart.addData(datasetIssue);

        mChart
                .setXAxis(true)
                .setYAxis(true)
                .setAxisThickness(2)
                .setBorderSpacing(Tools.fromDpToPx(15))
                .setStep(4)
                .setAxisColor(getResources().getColor(R.color.md_grey_600))
                .setLabelsColor(getResources().getColor(R.color.md_grey_600))
                .show();
    }


    // ================================== 基础方法 ==================================

    @OnClick(R.id.section)
    public void sectionOnClick() {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(SummaryActivity.this, findViewById(R.id.section));
        DroppyMenuPopup droppyMenu = droppyBuilder.fromMenu(R.menu.menu_summary)
                .triggerOnAnchorClick(false)
                .setOnClick(new DroppyClickCallbackInterface() {
                    @Override
                    public void call(View v, int id) {
                    }
                })
                .setPopupAnimation(new DroppyFadeInAnimation())
                .setXOffset(5)
                .setYOffset(5)
                .build();
        droppyMenu.show();
    }


    @OnClick(R.id.cancel)
    public void cancelOnClick() {
        finish();
    }

}
