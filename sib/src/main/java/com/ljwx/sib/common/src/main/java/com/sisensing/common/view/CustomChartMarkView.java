package com.sisensing.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.sisensing.common.R;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.utils.BgUnitUtils;
import com.sisensing.common.utils.BsDataUtils;
import com.sisensing.common.utils.ConfigUtils;
import com.sisensing.common.utils.GlucoseUtils;

/**
 * @author y.xie
 * @date 2021/4/17 16:41
 * @desc 折线图点击的弹窗
 */
@SuppressLint("ViewConstructor")
public class CustomChartMarkView extends MarkerView {
    private TextView mTvBsValue;
    private TextView mTvUnit;
    private TextView mTvBsStatus;
    private TextView mTvBsTime;

    public CustomChartMarkView(Context context,boolean isShowTime) {
        super(context, R.layout.common_custom_chart_markview);
        mTvBsValue = findViewById(R.id.tv_bs_value);
        mTvUnit = findViewById(R.id.tv_bs_unit);
        mTvBsStatus = findViewById(R.id.tv_bs_status);
        mTvBsTime = findViewById(R.id.tv_bs_time);
        if (isShowTime){
            mTvBsTime.setVisibility(VISIBLE);
        }else {
            mTvBsTime.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e.getData()!=null){
            float y = e.getY();
//            String yS = ConfigUtils.getInstance().unitIsMmol() ? y+"" : ((int)y)+"";
            String yS = GlucoseUtils.getUserConfigValue(y, BgUnitUtils.isUserMol());
            BloodGlucoseEntity entity = (BloodGlucoseEntity)e.getData() ;
            mTvBsTime.setText(TimeUtils.millis2String(entity.getProcessedTimeMill(),"MM/dd HH:mm"));
            mTvBsValue.setText(yS);
            mTvBsStatus.setText(BsDataUtils.getBsDesc(getContext(),y));
            mTvUnit.setText(BgUnitUtils.getUserUnit());
        }
        super.refreshContent(e, highlight);
    }


    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
