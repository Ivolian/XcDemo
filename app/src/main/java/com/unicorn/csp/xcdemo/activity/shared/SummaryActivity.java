package com.unicorn.csp.xcdemo.activity.shared;

import android.os.Bundle;
import android.view.View;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.activity.shared.base.ButterKnifeActivity;


public class SummaryActivity extends ButterKnifeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        initView();
    }

    private LineChartView mChart;

    private final String[] mLabels = {"3/15", "3/16", "3/17", "3/18", "3/19", "3/20", "3/21"};
    private final float[][] mValues = {{10f, 8f, 9f, 15f, 12f, 10f, 16f},
            {8f, 5f, 8f, 9f, 8f, 5f, 11f}};


    private void initView() {
        findViewById(R.id.section).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        mChart = (LineChartView) findViewById(R.id.chart1);

        LineSet dataset

                = new LineSet(mLabels, mValues[0]);
        dataset.setColor(getResources().getColor(R.color.md_light_blue_500))
//                .setFill(getResources().getColor(R.color.md_amber_200))
                .setSmooth(true)

                .setDotsColor(getResources().getColor(R.color.md_light_blue_600))
                .setDashed(new float[]{10f, 10f})
                .setThickness(4);
        mChart.addData(dataset);
        dataset = new LineSet(mLabels, mValues[1]);
        dataset.setColor(getResources().getColor(R.color.md_teal_300))
//                .setFill(getResources().getColor(R.color.md_teal_200))
                .setDotsColor(getResources().getColor(R.color.md_teal_400))
                .setThickness(4)
//                .setSmooth(true)
//                .setDashed(new float[]{10f, 10f})
        ;

        mChart.addData(dataset);
        mChart
                .setXAxis(true)
                .setYAxis(true)
//                .setLabelsFormat(new DecimalFormat("##'M'"))

                .setAxisThickness(2)
                .setBorderSpacing(Tools.fromDpToPx(15))
                .setStep(4)
//                .setYLabels(AxisController.LabelPosition.NONE)
                .setAxisColor(getResources().getColor(R.color.md_grey_600))
                .setLabelsColor(getResources().getColor(R.color.md_grey_600))
        ;

        mChart.show();
    }


}
