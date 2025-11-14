package com.yunbao.live.utils.mpchart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.yunbao.live.R;
import com.yunbao.live.bean.LiveGameDataBean;

import java.text.DecimalFormat;
import java.util.List;

public class LineChartMarkView extends MarkerView {

    private TextView tvDate;
    private TextView tvValue1;
    private TextView tvValue2;
    private IAxisValueFormatter xAxisValueFormatter;
    //    DecimalFormat df = new DecimalFormat("0.00");
    DecimalFormat df = new DecimalFormat("0");
    private LiveGameDataBean dataBean;

    public LineChartMarkView(Context context, LiveGameDataBean dataBean, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.custom_marker_view);
        this.xAxisValueFormatter = xAxisValueFormatter;
        this.dataBean = dataBean;
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvValue1 = (TextView) findViewById(R.id.tv_value1);
        tvValue2 = (TextView) findViewById(R.id.tv_value2);
    }

//    float y0 = 0;
//    float y1 = 0;

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Chart chart = getChartView();
        if (chart instanceof LineChart) {
            LineData lineData = ((LineChart) chart).getLineData();
            //获取到图表中的所有曲线
            List<ILineDataSet> dataSetList = lineData.getDataSets();
            for (int i = 0; i < dataSetList.size(); i++) {
                LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                float y = dataSet.getValues().get((int) e.getX()).getY();
//                if (i == 0) {
//                    tvValue1.setText(dataSet.getLabel() + "：" + df.format(y));
//                }
//                if (i == 1) {
//                    tvValue2.setText(dataSet.getLabel() + "：" + df.format(y));
//                }

                if (y >= 0) {
                    tvValue1.setText(dataBean.getTeam_a_name() + "领先经济：");
                } else {
                    tvValue1.setText(dataBean.getTeam_b_name() + "领先经济：");
                }
                tvValue2.setText(df.format(Math.abs(y)));
            }
            tvDate.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}