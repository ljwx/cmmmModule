package com.sisensing.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.util.Log;

import com.blankj.utilcode.util.LanguageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.sisensing.common.R;
import com.sisensing.common.chart.PointTimeValueFormatter;
import com.sisensing.common.chart.TimeValueFormatter;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.BloodGlucoseEntity.MultiDayDateBean;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.view.CustomChartMarkView;
import com.sisensing.common.view.CustomYAxisRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ChartUtils {

    public final static String[] TIME_FORMAT = new String[]{"00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"};

    public static final int[] COLORS = new int[]{
            Color.parseColor("#00D5B8"),
            Color.parseColor("#368AF2"),
            Color.parseColor("#FF4848"),
            Color.parseColor("#FFD153"),
            Color.parseColor("#FF7700"),
            Color.parseColor("#68D391"),
            Color.parseColor("#F69B55"),
            Color.parseColor("#FC8181"),
            Color.parseColor("#05B4A4"),
            Color.parseColor("#63B3ED"),
            Color.parseColor("#7F9CF5"),
            Color.parseColor("#B794F4"),
            Color.parseColor("#F687B3"),
            Color.parseColor("#39BEFF"),
            Color.parseColor("#BA04FA"),
            Color.parseColor("#C851B1"),
    };


    /**
     * 初始化折线图背景、描述信息
     *
     * @param context
     * @param mChart
     */
    public static void initLineChart(Context context, LineChart mChart, boolean isShowMarkView) {
        mChart.setBackgroundColor(Color.WHITE);
        Description description = new Description();
        description.setText(ConfigUtils.getInstance().getUnit(UserInfoUtils.getUserId()));
        description.setPosition(120, 20);
        mChart.setDrawGridBackground(false);
        mChart.setDescription(description);
        mChart.getDescription().setEnabled(true);

        if (isShowMarkView) {
            CustomChartMarkView chartMarkView = new CustomChartMarkView(context, true);
            chartMarkView.setChartView(mChart);
            mChart.setMarker(chartMarkView);
            mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    if (e.getData() == null) {
                        mChart.highlightValue(null);
                    } else {
                        mChart.highlightValue(h);
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }

        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);

        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);
        mChart.setPinchZoom(false);
        //mChart.animateX(1000);
        if (context != null) {
            mChart.setNoDataText(context.getResources().getString(R.string.chart_noData));
        }

        //  mChart.setContentDescription(SettingManager.getInstance().getGlucoseUnit());
        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.NONE);
        l.setDrawInside(true);
    }


    /**
     * 初始化图表X、Y轴
     *
     * @param mChart
     * @param yAxisRightEnable
     */
    public static void initLineChartXYAxis(LineChart mChart, boolean yAxisRightEnable) {
        int color = Color.parseColor("#999999");
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setAvoidFirstLastClipping(true);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.parseColor("#00000000"));
        //xAxis.setGridColor(Color.parseColor("#dddddd"));
        xAxis.setLabelCount(7, true);
        xAxis.setValueFormatter(new TimeValueFormatter());

        YAxis yAxisLeft = mChart.getAxisLeft();
        yAxisLeft.setDrawZeroLine(false);
        yAxisLeft.setLabelCount(6, true);
        yAxisLeft.setDrawGridLines(true);
        yAxisLeft.enableGridDashedLine(12, 12, 0);
        yAxisLeft.setAxisLineDashedLine(new DashPathEffect(new float[]{12, 12}, 0));
        yAxisLeft.setAxisLineColor(color);
        yAxisLeft.setGridColor(color);
        yAxisLeft.setAxisMaximum(GlucoseUtil.getTransferValue(25f));
        yAxisLeft.setAxisMinimum(0);

        YAxis yAxisRight = mChart.getAxisRight();
        mChart.getAxisRight().setEnabled(yAxisRightEnable);
        yAxisRight.setDrawZeroLine(false);
        yAxisRight.setLabelCount(5, true);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setAxisLineColor(color);
        yAxisRight.setGridColor(color);


    }

    /**
     * 创建最高最低血糖限制线
     *
     * @param mChart
     * @param min
     * @param max
     */
    public static void createLimitLineChart(LineChart mChart, float max, float min) {

        Log.d("TAG", "createLimitLineChart: min = "+min +" max = "+max);

        min = GlucoseUtil.getTransferMgValue(min);
        max = GlucoseUtil.getTransferMgValue(max);
        Log.d("TAG", "createLimitLineChart: min = "+min +" max = "+max);

        XAxis xAxis = mChart.getXAxis();
        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setDrawLimitLinesBehindData(true);
        xAxis.setDrawLimitLinesBehindData(true);
        ViewPortHandler viewPortHandler = mChart.getViewPortHandler();
        CustomYAxisRenderer yAxisRenderer = new CustomYAxisRenderer(viewPortHandler, yAxis, mChart.getTransformer(YAxis.AxisDependency.LEFT),
                Color.parseColor("#F2FCFC"),
                Color.parseColor("#FF8800"),
                Color.parseColor("#D6000E"));
        yAxisRenderer.setMinValue(min);
        yAxisRenderer.setMaxValue(max);

        mChart.setRendererLeftYAxis(yAxisRenderer);
    }

    private static LineDataSet createLineChartSet(ArrayList<Entry> values, int color, int circleColor, boolean point) {
        LineDataSet set1;
        set1 = new LineDataSet(values, "");
        set1.setDrawIcons(false);
        set1.setColor(color);
        set1.setCircleColor(circleColor);
        set1.setLineWidth(1.5f);
        set1.setCircleRadius(1.5f);
        set1.setDrawCircles(point);
        set1.setDrawCircleHole(false);
        set1.setFormLineWidth(1f);
        //set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);
        set1.setDrawValues(false);
        //set1.setValueTextSize(9f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setDrawFilled(false);
        set1.setMode(point ? LineDataSet.Mode.POINT : LineDataSet.Mode.CUBIC_BEZIER);
        //set1.setHighlightEnabled(false);
/*        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(context, R.drawable.bg_fade_curve1);
            set1.setFillDrawable(drawable);
        } else {
            set1.setFillColor(Color.BLACK);
        }*/

        set1.setFillColor(Color.BLACK);
        return set1;
    }


    /**
     * 动态添加数据
     *
     * @param context
     * @param mChart
     * @param userId
     * @param index
     * @param entity
     * @param
     */
    public static void addLineChartEntry(Context context, LineChart mChart, String userId, int index, BloodGlucoseEntity entity, int hour) {


        long max = getHourAxisMaximum(System.currentTimeMillis());
        long min = getHourAxisMinimum(max, hour);

        mChart.getXAxis().setAxisMaximum(max);
        mChart.getXAxis().setAxisMinimum(min);

        LineData data = mChart.getData();
        if (data == null) {
            data = new LineData();
            mChart.setData(data);
        }
        ILineDataSet set = data.getDataSetByIndex(index);
        if (set == null) {
            set = createLineChartSet(null, Color.parseColor("#1AACEB"), Color.parseColor("#0EA8EA"), hour == 6);
            data.addDataSet(set);
        }
        float yValue = entity.getGlucoseValue();
        long xValue = getMillsWithoutSeconds(entity.getProcessedTimeMill());

        LogUtils.e("min:" + min + "\nxValue:" + xValue + "---yValue:" + yValue + "\nmax:" + max);

        data.addEntry(new Entry(xValue, GlucoseUtil.getTransferValue(yValue), entity), 0);
        data.notifyDataChanged();
        mChart.notifyDataSetChanged();
        mChart.moveViewToX(xValue);
        //  mChart.invalidate();
    }

    /**
     * 最近X小时血糖数据渲染
     *
     * @param
     * @param mChart
     * @param hour
     * @param entities
     */
    public static void setHourLineChartData(LineChart mChart, int hour, List<BloodGlucoseEntity> entities) {

        boolean point = (hour == 3|| hour == 6);
        long currentMills = System.currentTimeMillis();

        long maxMill= currentMills + 3600000 - currentMills % 3600000;
        long minMill = maxMill - hour * 60 * 60 * 1000l;

        mChart.getXAxis().setValueFormatter(new PointTimeValueFormatter(hour, maxMill));
        mChart.getXAxis().setAxisMaximum(12 * hour);
        mChart.getXAxis().setAxisMinimum(0);

        ChartUtils.setLineData( mChart, getPointEntryValues(entities, 12 * hour, maxMill, minMill, point), point);
    }

    private static ArrayList<Entry> getPointEntryValues(List<BloodGlucoseEntity> entities, long num, long maxMill, long minMill, boolean isPoint) {

        ArrayList<Entry> values = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(entities)) {
            for (int i = 0; i < entities.size(); i++) {
                BloodGlucoseEntity glucoseEntity = entities.get(i);

                long millValue = (long) (((glucoseEntity.getProcessedTimeMill() - minMill) / ((float) (maxMill - minMill))) * num);

                float value = GlucoseUtil.getTransferValue(glucoseEntity.getGlucoseValue());
                if (value < GlucoseUtil.getTransferValue(2.2f)) {
                    value = GlucoseUtil.getTransferValue(2.2f);
                }
                if (value > GlucoseUtil.getTransferValue(25f)) {
                    value = GlucoseUtil.getTransferValue(25f);
                }
                int alarmStatus = glucoseEntity.getAlarmStatus();
                //正常血糖数据
                if (alarmStatus == 1 || alarmStatus == 5 || alarmStatus == 6) {
                    values.add(new Entry(millValue, value, glucoseEntity));
                } else {
                    if (!isPoint) {
                        values.add(new Entry(millValue, value, null));
                    }
                }
            }
        }
        return values;
    }

    /**
     * 构造折线图数据
     *
     * @param
     * @param
     * @param userId
     * @param entities
     * @param minusMills 当前需要减少的对应时间戳
     * @param entities   数据实体类
     * @return
     */
    private static ArrayList<Entry> getEntryValues(long minusMills, String userId, List<BloodGlucoseEntity> entities) {

        ArrayList<Entry> values = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(entities)) {
            for (int i = 0; i < entities.size(); i++) {
                BloodGlucoseEntity glucoseEntity = entities.get(i);

                long millValue = getMillsWithoutSeconds(glucoseEntity.getProcessedTimeMill() - minusMills);

                float value = GlucoseUtil.getTransferValue(glucoseEntity.getGlucoseValue());
                if (value < GlucoseUtil.getTransferValue(2.2f)) {
                    value = GlucoseUtil.getTransferValue(2.2f);
                }
                if (value > GlucoseUtil.getTransferValue(25f)) {
                    value = GlucoseUtil.getTransferValue(25f);
                }
                int alarmStatus = glucoseEntity.getAlarmStatus();
                //正常血糖数据
                if (alarmStatus == 1 || alarmStatus == 5 || alarmStatus == 6) {
                    values.add(new Entry(millValue, value, glucoseEntity));
                } else {
                    values.add(new Entry(millValue, value, null));
                }
            }
        }
        return values;
    }

    /**
     * 构造折线图数据
     * 该方法不切换单位，传递的参数需要先切换单位数据
     *
     * @param
     * @param
     * @param entities
     * @param minusMills 当前需要减少的对应时间戳
     * @param entities   数据实体类
     * @return
     */
    private static ArrayList<Entry> getEntryValues(long minusMills, List<BloodGlucoseEntity> entities) {

        ArrayList<Entry> values = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(entities)) {
            for (BloodGlucoseEntity glucoseEntity : entities) {

                long millValue = getMillsWithoutSeconds(glucoseEntity.getProcessedTimeMill() - minusMills);
                float value = glucoseEntity.getGlucoseValue();
                int alarmStatus = glucoseEntity.getAlarmStatus();
                //正常血糖数据
                if (alarmStatus == 1 || alarmStatus == 5 || alarmStatus == 6) {
                    values.add(new Entry(millValue, value, glucoseEntity));
                } else {
                    values.add(new Entry(millValue, value, null));
                }
            }
        }
        return values;
    }

    /**
     * 每日血糖数据渲染
     *
     * @param
     * @param mChart
     * @param startTimeMill
     * @param endTimeMill
     * @param userId
     * @param entities
     */
    public static void setDailyLineChartData(LineChart mChart, long startTimeMill, long endTimeMill,
                                             String userId, List<BloodGlucoseEntity> entities) {
        long max = getMillsWithoutSeconds(endTimeMill);
        long min = getMillsWithoutSeconds(startTimeMill);

        mChart.getXAxis().setAxisMaximum(max);
        mChart.getXAxis().setAxisMinimum(min);

        ChartUtils.setLineData( mChart, getEntryValues(0, userId, entities), false);
    }


    private static void setLineData(LineChart mChart, ArrayList<Entry> values, boolean point) {
        LineDataSet set1;
        int color = Color.parseColor("#486CA4");
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setMode(point ? LineDataSet.Mode.POINT : LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCircleColor(color);
            set1.setDrawCircles(point);
            set1.setEntries(values);
            set1.setHighLightColor(Color.parseColor("#ADB7CB"));

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = ChartUtils.createLineChartSet(values, color, color, point);
            set1.setHighLightColor(Color.parseColor("#ADB7CB"));
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }
        mChart.invalidate();
    }


    /**
     * 将时间戳转换为分
     *
     * @return
     */
    public static long getMillsWithoutSeconds(long mills) {

        return mills / 60000;
    }

    /***
     * 获取X轴最大限制（即当前时间的下一个整点转换为分）
     * @return
     */
    public static long getHourAxisMaximum(long maxMills) {

        long mills = maxMills + 3600000 - maxMills % 3600000;

        return getMillsWithoutSeconds(mills);
    }

    /**
     * 获取X轴最小限制
     *
     * @param maximum
     * @param hour
     * @return
     */
    public static long getHourAxisMinimum(long maximum, int hour) {

        return maximum - hour * 60;
    }

    /**
     * 增加一条血糖数据
     *
     * @param context              上下文
     * @param mChart               当前图表
     * @param userid               用户名
     * @param multiDayDateBeanList 日期天数数据集合
     * @param clickPosition        当前点击的日期position
     * @param entities             血糖数据
     */
    public static void addOneLineChartData(Context context, LineChart mChart, String userid, List<MultiDayDateBean> multiDayDateBeanList,
                                           int clickPosition, List<BloodGlucoseEntity> entities) {
        MultiDayDateBean multiDayDateBean = multiDayDateBeanList.get(clickPosition);
        LineData lineData = mChart.getLineData();
        if (lineData == null) {
            lineData = new LineData();
        }
        //新增一条数据
        int color = multiDayDateBean.getBgColor();
        //从第一个日期开始需要减去的毫秒数(一天的毫秒数x天数)
        long minusMills = 24 * 60 * 60 * 1000l * clickPosition;
        LineDataSet lineDataSet = ChartUtils.createLineChartSet( getEntryValues(minusMills, entities), color, color, false);

        lineData.addDataSet(lineDataSet);
        mChart.setData(lineData);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    public static void addLineChartData(LineChart mChart, List<BloodGlucoseEntity> entities) {
        LineData lineData = mChart.getLineData();
        if (lineData == null) {
            lineData = new LineData();
        }
        int color = Color.parseColor("#486CA4");
        LineDataSet lineDataSet = ChartUtils.createLineChartSet( getEntryValues(0, UserInfoUtils.getUserId(), entities), color, color, false);

        lineData.addDataSet(lineDataSet);
        mChart.setData(lineData);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    public static void addLineChartData( LineChart mChart, List<BloodGlucoseEntity> entities, int num, long max, long min) {
        LineData lineData = mChart.getLineData();
        if (lineData == null) {
            lineData = new LineData();
        }
        int color = Color.parseColor("#486CA4");
        LineDataSet lineDataSet = ChartUtils.createLineChartSet( getPointEntryValues(entities, num, max, min, false), color, color, false);

        lineData.addDataSet(lineDataSet);
        mChart.setData(lineData);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    /**
     * 取消一条血糖数据
     *
     * @param mChart         图表
     * @param cancelPosition 取消的位置
     */
    public static void cancelOneLineChartData(LineChart mChart, int cancelPosition) {
        LineData lineData = mChart.getLineData();
        if (lineData == null)
            return;
        lineData.removeDataSet(cancelPosition);
        mChart.setData(lineData);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    /**
     * 添加所有日期数据
     *
     * @param context
     * @param mChart               图表
     * @param userId               设备名
     * @param multiDayDateBeanList 日期数据集合
     * @param entities             查询到的所有有效血糖数据
     */
    public static void addAllLineChartData(final Context context, final LineChart mChart, final String userId, final List<MultiDayDateBean> multiDayDateBeanList,
                                           final List<List<BloodGlucoseEntity>> entities) {

        LineData lineData = new LineData();
        for (int i = 0; i < multiDayDateBeanList.size(); i++) {
            MultiDayDateBean multiDayDateBean = multiDayDateBeanList.get(i);
            List<BloodGlucoseEntity> bloodGlucoseEntities = entities.get(i);
            //新增一条数据
            int color = multiDayDateBean.getBgColor();
            //从第一个日期开始需要减去的毫秒数(一天的毫秒数x天数)
            long minusMills = 24 * 60 * 60 * 1000l * i;
            LineDataSet lineDataSet = ChartUtils.createLineChartSet(getEntryValues(minusMills, bloodGlucoseEntities), color, color, false);
            lineData.addDataSet(lineDataSet);
        }

        mChart.setData(lineData);
        mChart.notifyDataSetChanged();
        mChart.invalidate();

    }

    /**
     * 清除所有数据
     *
     * @param mChart
     */
    public static void cancelAllLineChartData(LineChart mChart) {
        LineData lineData = mChart.getLineData();
        if (lineData!=null){
            List<ILineDataSet> dataSets = lineData.getDataSets();
            if (ObjectUtils.isNotEmpty(dataSets)){
                dataSets.clear();
                lineData.notifyDataChanged();
            }
        }
        mChart.setData(lineData);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
       // mChart.clear();
    }


    public static void initAGPLineChart(Context context, LineChart chart, boolean isShowMarkView, float max, float min) {
        Description description = new Description();
        description.setText(ConfigUtils.getInstance().getUnit(UserInfoUtils.getUserId()));
        description.setPosition(120, 20);
        chart.setDescription(description);
        chart.getDescription().setEnabled(true);
        chart.setBackgroundColor(Color.WHITE);
        chart.setTouchEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDrawBorders(true);
        chart.setBorderColor(Color.parseColor("#dddddd"));
        chart.setBorderWidth(0.8f);
        chart.setNoDataText(StringUtils.getString(R.string.data_is_insufficient_to_analyse));

        if (isShowMarkView) {
            CustomChartMarkView chartMarkView = new CustomChartMarkView(context, false);
            chartMarkView.setChartView(chart);
            chart.setMarker(chartMarkView);
        }


        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setForm(Legend.LegendForm.LINE);
        l.setFormLineWidth(14f);
        //l.setFormSize(30f);
        l.setDrawInside(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(7, true);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.parseColor("#dddddd"));
        xAxis.setGridColor(Color.parseColor("#dddddd"));
        xAxis.setValueFormatter(new TimeValueFormatter());

        YAxis leftAxis = chart.getAxisLeft();

        float maxValue = GlucoseUtil.getTransferValue(max);
        float minValue = GlucoseUtil.getTransferValue(min);

        leftAxis.setAxisMaximum(maxValue);
        leftAxis.setAxisMinimum(minValue);
        leftAxis.setLabelCount(6, true);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setAxisLineColor(Color.parseColor("#dddddd"));
        leftAxis.setGridColor(Color.parseColor("#dddddd"));

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setAxisMaximum(maxValue);
        rightAxis.setAxisMinimum(minValue);
        rightAxis.setLabelCount(6, true);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setAxisLineColor(Color.parseColor("#dddddd"));
        rightAxis.setGridColor(Color.parseColor("#dddddd"));

        float upper = GlucoseUtil.getTransferMgValue(UserInfoUtils.getBsRangeUpper());
        String upperString = GlucoseUtils.getUserConfigValue(UserInfoUtils.getBsRangeUpper(), false);
        float low = GlucoseUtil.getTransferMgValue(UserInfoUtils.getBsRangeLower());
        String lowString = GlucoseUtils.getUserConfigValue(UserInfoUtils.getBsRangeLower(), false);

        LimitLine ll1 = new LimitLine(upper, upperString);
        ll1.enableDashedLine(12f, 12f, 0);
        ll1.setLineColor(Color.parseColor("#0C4995"));
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(6.2f);

        LimitLine ll2 = new LimitLine(low, lowString);
        ll2.enableDashedLine(12f, 12f, 0);
        ll2.setLineColor(Color.parseColor("#0C4995"));
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll2.setTextSize(6.2f);

        leftAxis.setDrawLimitLinesBehindData(false);
        xAxis.setDrawLimitLinesBehindData(false);

        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
    }

    public interface ChartType {
        int UNKNOWN = -1;
        int AGP5 = 0x01;
        int AGP25 = 0x02;
        int AGP50 = 0x03;
        int AGP75 = 0x04;
        int AGP95 = 0x05;
    }

    public static LineDataSet createMultiLineChartSet(Context context, final LineChart chart, ArrayList<Entry> values, String label, int color, boolean filled, int chartType) {
        LineDataSet set = new LineDataSet(values, label);
        set.setDrawIcons(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setLineWidth(1.2f);
        set.setCircleRadius(1.5f);
        set.setDrawCircles(false);
        set.setDrawCircleHole(false);
        set.setFormLineWidth(1f);
        set.setFormSize(15.f);
        set.setDrawValues(false);
        set.enableDashedHighlightLine(10f, 5f, 0f);
        set.setColor(color);
        set.setFillColor(color);

        set.setFillFormatter(new IFillFormatter() {
            @Override
            public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                return chart.getAxisLeft().getAxisMaximum();
            }
        });
        if (chartType == ChartType.UNKNOWN) {
            set.setDrawFilled(filled);
        } else if (chartType == ChartType.AGP5) {
            set.setFillAlpha(255);
            set.setDrawFilled(true);
        } else if (chartType == ChartType.AGP25) {
            set.setFillAlpha(255);
            set.setDrawFilled(true);
        } else if (chartType == ChartType.AGP50) {
            set.setDrawFilled(false);
        } else if (chartType == ChartType.AGP75) {
            set.setFillAlpha(255);
            set.setDrawFilled(true);
        } else if (chartType == ChartType.AGP95) {
            set.setFillAlpha(255);
            set.setDrawFilled(true);
        }

        return set;
    }

    /**
     * 重新设置图表Y轴最大值
     *
     * @param lineChart
     */
    public static void againSetLineChartYMaxValue(LineChart lineChart, boolean isAgp) {
        if (lineChart.getData() == null) return;
        float maxValue = lineChart.getYMax();
        if (Float.isNaN(maxValue) || maxValue == 0) return;
        float yMaxValue;
        if (maxValue < GlucoseUtil.getTransferValue(10)) {
            yMaxValue = GlucoseUtil.getTransferValue(15);
        } else if (maxValue >= GlucoseUtil.getTransferValue(10) && maxValue < GlucoseUtil.getTransferValue(15)) {
            yMaxValue = GlucoseUtil.getTransferValue(20);
        }  else {
            yMaxValue = GlucoseUtil.getTransferValue(25);
        }
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setAxisMaximum(yMaxValue);
        if (isAgp) {
            YAxis yAxisRight = lineChart.getAxisRight();
            yAxisRight.setAxisMaximum(yMaxValue);
        }
        lineChart.postInvalidate();
    }

}
