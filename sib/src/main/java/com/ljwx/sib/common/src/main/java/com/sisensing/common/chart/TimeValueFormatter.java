package com.sisensing.common.chart;


import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.sisensing.common.utils.Log;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.chart
 * @Author: f.deng
 * @CreateDate: 2021/3/11 16:47
 * @Description:
 */
public class TimeValueFormatter implements IAxisValueFormatter {


    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        BigDecimal timeMills = BigDecimal.valueOf(value).multiply(BigDecimal.valueOf(60000));


        //LogUtils.e(timeMills.longValue());

        SimpleDateFormat format = new SimpleDateFormat("HH:mm");

        Log.d("血糖曲线", "value:"+format.format(timeMills));
        return format.format(timeMills);

    }
}
