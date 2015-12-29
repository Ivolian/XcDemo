package com.unicorn.csp.xcdemo.equipment;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.unicorn.csp.xcdemo.R;
import com.unicorn.csp.xcdemo.fragment.shared.base.LazyLoadFragment;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;


public class CostFragment extends LazyLoadFragment {

    @Bind(R.id.chart_cost)
    PieChart mChart;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_equipment_cost;
    }


    //

    @Override
    public void initViews() {
        mChart.setUsePercentValues(false);
        mChart.setCenterText("总成本:600");
        mChart.setDrawHoleEnabled(true);
        mChart.setDescription("");
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);



        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener

        // mChart.spin(2000, 0, 360);


        setData();


        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(60f);
        l.setTextSize(14);

    }

    private void setData() {


        ArrayList<Entry> yVals1 = new ArrayList<>();
        yVals1.add(new Entry(100,0));
        yVals1.add(new Entry(200,1));
        yVals1.add(new Entry(300,2));

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("设备折旧");
        xVals.add("设备零件");
        xVals.add("人工维修");

        PieDataSet dataSet = new PieDataSet(yVals1,"成本类型");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors





        dataSet.setColors(
                Arrays.asList(
                        ContextCompat.getColor(getActivity(),R.color.md_blue_500) ,
                        ContextCompat.getColor(getActivity(),R.color.md_brown_400) ,
                        ContextCompat.getColor(getActivity(),R.color.md_cyan_700)
                )

        );
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);



        mChart.invalidate();
    }

}
