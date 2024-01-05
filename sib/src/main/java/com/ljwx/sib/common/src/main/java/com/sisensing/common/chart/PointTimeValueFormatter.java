package com.sisensing.common.chart;

import com.blankj.utilcode.util.TimeUtils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.chart
 * @Author: f.deng
 * @CreateDate: 2021/3/11 16:47
 * @Description:
 */
public class PointTimeValueFormatter implements IAxisValueFormatter {

    private long maxMill;
    private int hour;

    public PointTimeValueFormatter(int hour,long mill) {
        this.maxMill = mill;
        this.hour = hour;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        //需要减去的对应的小时
        String xTime;
        long minusMill;
        if (hour == 3) {
            float v = value / 12 ;
            minusMill = (long) ((hour - v) * 60 * 60 * 1000l);
        }else {
            int xValue = (int) value;
            int v = xValue/12;
            minusMill = (hour-v)*60*60*1000l;
        }
        xTime = TimeUtils.millis2String(maxMill - minusMill, "HH:mm");
        return xTime;

    }
}
