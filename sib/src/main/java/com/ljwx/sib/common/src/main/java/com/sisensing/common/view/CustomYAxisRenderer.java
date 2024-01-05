package com.sisensing.common.view;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;

import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.sisensing.common.utils.BgUnitUtils;
import com.sisensing.common.utils.ConfigUtils;
import com.sisensing.common.utils.GlucoseUtils;
import com.sisensing.common.utils.Log;

import java.util.List;

/**
 * @author y.xie
 * @date 2021/4/17 15:29
 * @desc
 */
@SuppressLint("ViewConstructor")
public class CustomYAxisRenderer extends YAxisRenderer {
    private float minValue;
    private float maxValue;
    protected YAxis mYAxis;
    protected ViewPortHandler viewPortHandler;
    protected float[] pts1 = new float[2];
    protected float[] pts2 = new float[2];

    private Paint mLimitRectPaint;
    private Paint mbsRangeTextPaint;//血糖高低范围文字画笔
    private int bsUpperTextColor;//高血糖颜色
    private int bsLowerTextColor;//低血糖颜色


    public CustomYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans,int color,
                               int bsRangeUpperTextColor,int bsRangeLowerTextColor) {
        super(viewPortHandler, yAxis, trans);
        this.mYAxis = yAxis;
        this.viewPortHandler = viewPortHandler;
        this.bsUpperTextColor = bsRangeUpperTextColor;
        this.bsLowerTextColor = bsRangeLowerTextColor;

        mLimitRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLimitRectPaint.setColor(color);
        mLimitRectPaint.setStyle(Paint.Style.FILL);

        mbsRangeTextPaint = new Paint();
        mbsRangeTextPaint.setTextSize(32f);
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public void renderLimitLines(Canvas c) {
        pts1[0] = 0;
        pts1[1] = maxValue;

        pts2[0] = 0;
        pts2[1] = minValue;

        mTrans.pointValuesToPixel(pts1);
        mTrans.pointValuesToPixel(pts2);
        //绘制最小值和最大值区间背景
        c.drawRect(new RectF(mViewPortHandler.contentLeft(),pts2[1],mViewPortHandler.contentRight(),pts1[1]),mLimitRectPaint);

        Path path1 = new Path();
        path1.moveTo(mViewPortHandler.contentLeft(),pts1[1]);
        path1.lineTo(mViewPortHandler.contentRight(),pts1[1]);

        Path path2 = new Path();
        path2.moveTo(mViewPortHandler.contentLeft(),pts2[1]);
        path2.lineTo(mViewPortHandler.contentRight(),pts2[1]);


//        String max = ConfigUtils.getInstance().unitIsMmol() ? maxValue+"" : ((int)maxValue)+"";
        String max = GlucoseUtils.getUserConfigValue(maxValue, BgUnitUtils.isUserMol());
        Rect maxRect = new Rect();
        mbsRangeTextPaint.getTextBounds(max, 0, max.length(), maxRect);
        int maxWidth = maxRect.width();
        int maxHeight = maxRect.height();

//        String min = ConfigUtils.getInstance().unitIsMmol() ? minValue+"" : ((int)minValue)+"";
        String min = GlucoseUtils.getUserConfigValue(minValue, BgUnitUtils.isUserMol());
        Rect minRect = new Rect();
        mbsRangeTextPaint.getTextBounds(min,0,min.length(),minRect);
        int minWidth = minRect.width();
        int minHeight = minRect.height();

        mbsRangeTextPaint.setColor(bsUpperTextColor);
        c.drawText(max,mViewPortHandler.contentRight()-maxWidth,pts1[1]-maxHeight/2,mbsRangeTextPaint);

        mbsRangeTextPaint.setColor(bsLowerTextColor);
        c.drawText(min,mViewPortHandler.contentRight()-minWidth,pts2[1]+minHeight,mbsRangeTextPaint);
    }
}
