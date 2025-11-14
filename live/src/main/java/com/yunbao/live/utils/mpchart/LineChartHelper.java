package com.yunbao.live.utils.mpchart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.yunbao.common.utils.AppLog;
import com.yunbao.common.utils.StringUtil;
import com.yunbao.live.R;
import com.yunbao.live.bean.LineChartDataBean;
import com.yunbao.live.bean.LiveGameDataBean;

import java.util.ArrayList;
import java.util.List;


public class LineChartHelper {

    private final YAxis leftYAxis;
    private final YAxis rightYaxis;
    private final Legend legend;
    private XAxis xAxis;
    private Context mContext;
    private LineChart lineChart;

    public static LineChartHelper init(Context context, LineChart lineChart) {
        return new LineChartHelper(context, lineChart);
    }

    /**
     * 初始化图表
     */
    private LineChartHelper(Context context, LineChart lineChart) {
        mContext = context;
        this.lineChart = lineChart;
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(true);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //是否缩放
        lineChart.setScaleEnabled(false);
        lineChart.setBackgroundColor(Color.WHITE);
        //是否显示边界
        lineChart.setDrawBorders(false);

        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
//        leftYAxis.setAxisMinimum(0f);
//        leftYAxis.setAxisMaximum(0f);
        rightYaxis.setAxisMinimum(0f);
        xAxis.setDrawGridLines(false);
        rightYaxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(true);
        //设置X Y轴网格线为虚线（实体线长度、间隔距离、偏移量：通常使用 0）
//        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        //网格线颜色
        leftYAxis.setGridColor(mContext.getResources().getColor(R.color.black6));
        leftYAxis.setAxisLineColor(mContext.getResources().getColor(R.color.white));
        //去掉右侧X轴
//        leftYAxis.setEnabled(false);
        //去掉右侧Y轴
        rightYaxis.setEnabled(false);
        //右下角还有一个描述标签 Descripition Lable
        Description description = new Description();
//        description.setText("需要展示的内容");
        description.setEnabled(false);
        lineChart.setDescription(description);
        //左侧字体颜色
        leftYAxis.setTextColor(Color.GRAY);
        //X轴字体颜色
        xAxis.setTextColor(Color.GRAY);

        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);
        //不显示曲线名称
        legend.setEnabled(false);
        //显示的个数
//        xAxis.setLabelCount(6,false);

        //与X轴值得自定义类似，并按照目标图的分割要求一样 将Y轴分为 8份
//        leftYAxis.setValueFormatter(new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                return ((int) (value * 100)) + "%";
//            }
//        });
//        leftYAxis.setLabelCount(8);
    }

    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     * @param mode
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, LineDataSet.Mode mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        //圆半径
        lineDataSet.setCircleRadius(2f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1f);
        lineDataSet.setFormSize(15.f);
        //不显示值
        lineDataSet.setDrawValues(false);
        //不显示点
        lineDataSet.setDrawCircles(true);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighLightColor(Color.LTGRAY); // 设置点击某个点时，横竖两条线的颜色

        if (mode == null) {
            //设置曲线展示为圆滑曲线（如果不设置则默认折线）
            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        } else {
            lineDataSet.setMode(mode);
        }
    }

    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param name     曲线名称
     * @param color    曲线颜色
     */
    public void showLineChart(final List<LineChartDataBean> dataList, String name, int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            LineChartDataBean data = dataList.get(i);
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, (float) data.getValue());
            entries.add(entry);
            //X轴自定义
        }

        //X轴定义
        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                try {
                    //设置 xAxis.setGranularity(1);后 value是从0开始的，每次加1，
                    int v = (int) value;
                    if (v <= dataList.size() && v >= 0) {
                        return dataList.get(v).getTradeDate();
                    } else {
                        return "";
                    }
                } catch (Exception e) {
                    AppLog.e("", e.toString());
                }
                return "";
            }
        };
        xAxis.setValueFormatter(formatter);

        //Y轴定义
        IndexAxisValueFormatter formatterY = new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value >= 0) {
                    return StringUtil.formatBigNum(StringUtil.formatDouble(value));
                } else {
                    return "-" + StringUtil.formatBigNum(StringUtil.formatDouble(Math.abs(value)));
                }

            }
        };

        leftYAxis.setValueFormatter(formatterY);

        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
    }

    /**
     * 添加曲线
     */
    public void addLine(List<LineChartDataBean> dataList, String name, int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            LineChartDataBean data = dataList.get(i);
            Entry entry = new Entry(i, (float) data.getValue());
            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);
        lineChart.getLineData().addDataSet(lineDataSet);
        lineChart.invalidate();
    }

    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(int position, Drawable drawable) {
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(position);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }

    /**
     * 设置 可以显示X Y 轴自定义值的 MarkerView
     *
     * @param dataBean
     */
    public void setMarkerView(LiveGameDataBean dataBean) {
        if (dataBean != null) {
            LineChartMarkView mv = new LineChartMarkView(mContext, dataBean, xAxis.getValueFormatter());
            mv.setChartView(lineChart);
            lineChart.setMarker(mv);
            lineChart.invalidate();
        }
    }

    /**
     * 重置折线数据
     *
     * @param position
     * @param dataList
     * @param name
     * @param color
     */
    public void resetLine(int position, List<LineChartDataBean> dataList, String name, int color) {
        LineData lineData = lineChart.getData();
        List<ILineDataSet> list = lineData.getDataSets();
        if (list.size() <= position) {
            return;
        }

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            LineChartDataBean data = dataList.get(i);
            Entry entry = new Entry(i, (float) data.getValue());
            entries.add(entry);
        }

        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color, LineDataSet.Mode.LINEAR);

        lineData.getDataSets().set(position, lineDataSet);
        lineChart.invalidate();
    }
}
