package com.sisensing.common.view.popup;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.lxj.xpopup.core.PositionPopupView;
import com.sisensing.common.R;
import com.sisensing.common.utils.CommonTimeUtils;
import com.sisensing.common.utils.DateTimeUtils;
import com.sisensing.common.utils.WeekNameUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author y.xie
 * @date 2021/4/22 20:15
 * @desc
 */
public class BsCalendarPop extends PositionPopupView implements View.OnClickListener, CalendarView.OnCalendarSelectListener, CalendarView.OnCalendarInterceptListener, CalendarView.OnMonthChangeListener {
    //一个占位View
    private View mViewPlaceHolder;
    private TextView mTvBeforeDay;
    private TextView mTvDate1;
    private TextView mTvWeek;
    private TextView mTvNextDay;
    private TextView mTvBeforeMonth;
    private TextView mTvDate2;
    private TextView mTvNextMonth;
    private CalendarView calendarView;
    //有数据的日期
    private Map<String, Calendar> mSchemeDates = new HashMap<>();
    private CalendarListener calendarListener;
    //当前日期
    private Calendar mCalendar;

    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    public BsCalendarPop(@NonNull Context context) {
        super(context);
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.common_bs_calendar;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mViewPlaceHolder = findViewById(R.id.view_placeholder);
        mTvBeforeDay = findViewById(R.id.tv_day_before);
        mTvDate1 = findViewById(R.id.tv_date1);
        mTvWeek = findViewById(R.id.tv_week);
        mTvNextDay = findViewById(R.id.tv_day_next);
        mTvBeforeMonth = findViewById(R.id.tv_month_before);
        mTvDate2 = findViewById(R.id.tv_date2);
        mTvNextMonth = findViewById(R.id.tv_month_next);
        calendarView = findViewById(R.id.calendar);

        if (mCalendar == null) {
            //选中当天
            mCalendar = getSchemeCalendar(calendarView.getCurYear(), calendarView.getCurMonth(), calendarView.getCurDay());
        }
        showCalendar(mCalendar);
        calendarView.scrollToCalendar(mCalendar.getYear(), mCalendar.getMonth(), mCalendar.getDay());

        BarUtils.addMarginTopEqualStatusBarHeight(mViewPlaceHolder);

        calendarView.setMonthViewScrollable(true);
        calendarView.setOnCalendarSelectListener(this);
        calendarView.setOnCalendarInterceptListener(this);
        calendarView.setOnMonthChangeListener(this);

        calendarView.setSchemeDate(mSchemeDates);


        mTvBeforeDay.setOnClickListener(this);
        mTvNextDay.setOnClickListener(this);
        mTvBeforeMonth.setOnClickListener(this);
        mTvNextMonth.setOnClickListener(this);
    }

    public void setCalendar(Calendar calendar) {
        this.mCalendar = calendar;
    }

    /**
     * @param dates 所有包含数据的日期集合
     */
    public void setData(Map<String, Calendar> dates) {
        mSchemeDates.putAll(dates);

        if (calendarView != null) {
            calendarView.setSchemeDate(mSchemeDates);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_day_before) {
            //前一天
            preDay();
        } else if (id == R.id.tv_day_next) {
            //后一天
            nextDay();
        } else if (id == R.id.tv_month_before) {
            //上一月
            calendarView.scrollToPre();
        } else if (id == R.id.tv_month_next) {
            //下一月
            calendarView.scrollToNext();
        }
    }

    public void preDay() {
        if (calendarView == null) return;
        Calendar selectCalendar = calendarView.getSelectedCalendar();

        //前一天时间戳
        long mill = selectCalendar.getTimeInMillis() - 86400000;
        int[] d = CommonTimeUtils.getYearMonthDay(mill);
        //不在标记的日期
        if (mSchemeDates.get(getSchemeCalendar(d[0], d[1], d[2]).toString()) == null) {
            ToastUtils.showShort(getContext().getString(R.string.chart_noData));
        } else {
            calendarView.scrollToCalendar(d[0], d[1], d[2]);
        }
    }

    public void nextDay() {
        if (calendarView == null) return;
        Calendar selectCalendar = calendarView.getSelectedCalendar();
        //后一天时间戳
        long mill = selectCalendar.getTimeInMillis() + 86400000;
        int[] d = CommonTimeUtils.getYearMonthDay(mill);
        if (mSchemeDates.get(getSchemeCalendar(d[0], d[1], d[2]).toString()) == null) {
            ToastUtils.showShort(getContext().getString(R.string.chart_noData));
        } else {
            calendarView.scrollToCalendar(d[0], d[1], d[2]);
        }
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        if (calendar.hasScheme()) {
            dismiss();
            showCalendar(calendar);
        }
    }


    private void showCalendar(Calendar calendar) {
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        long mill = calendar.getTimeInMillis();
        LogUtils.d("年月日", year, month, day);
        mTvDate1.setText(DateTimeUtils.numMonth2Short(getContext(), month) + " " + DateTimeUtils.INSTANCE.limit2Bit(day));
        mTvWeek.setText(WeekNameUtils.getWeekShort(getContext(), calendar.getWeek()));
        mTvDate2.setText(DateTimeUtils.numMonth2Short(getContext(), month) + " " + year);
        String[] month_string_array = getContext()
                .getResources()
                .getStringArray(R.array.month_array);
        String beforeMonth = month_string_array[(month - 1 == 0 ? 12 : month - 1)-1];
        String nextMonth = month_string_array[(month + 1 == 13 ? 1 : month + 1)-1];
        mTvBeforeMonth.setText(beforeMonth);
        mTvNextMonth.setText(nextMonth);

        if (calendarListener != null) {
            calendarListener.getDate(year, month, day, mill);
        }
    }

//    /**
//     * 小于10的月或者日前面补0
//     *
//     * @param value
//     * @return
//     */
//    private String getMonthOrDayStr(int value) {
//        String str;
//        if (value < 10) {
//            str = "0" + value;
//        } else {
//            str = String.valueOf(value);
//        }
//        return str;
//    }

    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        return !calendar.hasScheme();
    }

    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {
        ToastUtils.showShort(calendar.getDay());
    }

    public Calendar getSchemeCalendar(int year, int month, int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        //设置星期
        calendar.setWeek(getSystemWeekIndex(year, month, day));
        return calendar;
    }

    public static java.util.Calendar getSystemCalendar(int year, int month, int day) {
        java.util.Calendar calendar1 = java.util.Calendar.getInstance();
        calendar1.set(java.util.Calendar.YEAR, year);
        calendar1.set(java.util.Calendar.MONTH, month-1);
        calendar1.set(java.util.Calendar.DAY_OF_MONTH, day);
        return calendar1;
    }

    public static int getSystemWeekIndex(int year, int month, int day) {
        return getSystemCalendar(year, month, day).get(java.util.Calendar.DAY_OF_WEEK) - 1;
    }

    @Override
    public void onMonthChange(int year, int month) {
        //切换月份默认是当月的第一天
        showCalendar(getSchemeCalendar(year, month, 1));
    }

    public interface CalendarListener {
        void getDate(int year, int month, int day, long mill);
    }
}
