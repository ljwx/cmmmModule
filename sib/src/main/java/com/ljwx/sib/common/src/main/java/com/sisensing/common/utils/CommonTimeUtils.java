package com.sisensing.common.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.StringUtils;
import com.sisensing.common.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author y.xie
 * @date 2021/3/8 17:43
 * @desc
 */
public class CommonTimeUtils {
    /**
     * 年月日时分时间选择
     * @param context
     * @param showColumns 年月日时分秒列显示与隐藏
     * @param listener
     */
    public static void showTimePicker(Context context,boolean[] showColumns,final OnTimeSelectListener listener){
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy ");
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);


        SimpleDateFormat formatter_mouth = new SimpleDateFormat("MM ");
        String mouth_str = formatter_mouth.format(curDate);
        int mouth_int = (int) Double.parseDouble(mouth_str);

        SimpleDateFormat formatter_day = new SimpleDateFormat("dd ");
        String day_str = formatter_day.format(curDate);
        int day_int = (int) Double.parseDouble(day_str);


        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(year_int, mouth_int - 1, day_int);

        //时间选择器
        TimePickerView pvTime1 = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (listener!=null){
                    listener.onTimeSelect(date,v);
                }
            }
        }) .setType(showColumns) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setCancelColor(Color.parseColor("#999999"))
                .setSubmitColor(Color.parseColor("#00D5B8"))
                .setTitleBgColor(Color.parseColor("#FFFFFF"))
                .setLabel(context.getString(R.string.year_y), context.getString(R.string.month_m), context.getString(R.string.day_d), StringUtils.getString(R.string.dailybs_hour_h), StringUtils.getString(R.string.min), "")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setTextColorOut(Color.GRAY)//设置没有被选中项的颜色
                .setDate(selectedDate)
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(-10, 0, 10, 0, 0, 0)//设置X轴倾斜角度[ -90 , 90°]
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .setCancelText(context.getString(R.string.common_cancel))
                .setSubmitText(context.getString(R.string.common_know))
                .build();
        pvTime1.show();
    }

    public static void showBirthdayDatePicker(Context context,final OnTimeSelectListener listener){
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy ");
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);


        SimpleDateFormat formatter_mouth = new SimpleDateFormat("MM ");
        String mouth_str = formatter_mouth.format(curDate);
        int mouth_int = (int) Double.parseDouble(mouth_str);

        SimpleDateFormat formatter_day = new SimpleDateFormat("dd ");
        String day_str = formatter_day.format(curDate);
        int day_int = (int) Double.parseDouble(day_str);


        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(year_int-150, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(year_int-18, mouth_int - 1, day_int);


        //时间选择器
        TimePickerView pvTime1 = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (listener!=null){
                    listener.onTimeSelect(date,v);
                }
            }
        }) .setType(new boolean[]{true, true, true, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setCancelColor(Color.parseColor("#999999"))
                .setSubmitColor(Color.parseColor("#00D5B8"))
                .setTitleBgColor(Color.parseColor("#FFFFFF"))
                .setLabel(context.getString(R.string.year_y), context.getString(R.string.month_m), context.getString(R.string.day_d), StringUtils.getString(R.string.dailybs_hour_h), StringUtils.getString(R.string.min), "")//默认设置为年月日时分秒
                .isCenterLabel(false)
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setTextColorOut(Color.GRAY)//设置没有被选中项的颜色
                .setDate(endDate)
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(-10, 0, 10, 0, 0, 0)//设置X轴倾斜角度[ -90 , 90°]
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .setCancelText(context.getString(R.string.common_cancel))
                .setSubmitText(context.getString(R.string.done))
                .build();
        pvTime1.show();
    }

    /**
     * 获取某个日前的前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static Date getOldDate(long mills,int distanceDay) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = new Date(mills);
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) + distanceDay);
        Date endDate = null;
        try {
            endDate = dft.parse(dft.format(date.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return endDate;
    }

    /**
     * 获取每天的开始时间 00:00:00:00
     *
     * @param date
     * @return
     */
    public static Date getStartTime(Date date) {
        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(date);
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        return dateStart.getTime();
    }

    /**
     * 获取每天的结束时间 23:59:59:999
     *
     * @param date
     * @return
     */
    public static Date getEndTime(Date date) {
        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(date);
        dateEnd.set(Calendar.HOUR_OF_DAY, 23);
        dateEnd.set(Calendar.MINUTE, 59);
        dateEnd.set(Calendar.SECOND, 59);
        return dateEnd.getTime();
    }

    /**
     * 获取每天的起床时间 06:00:00
     *
     * @param date
     * @return
     */
    public static Date getUPTime(Date date) {
        Calendar dateEnd = Calendar.getInstance();
        dateEnd.setTime(date);
        dateEnd.set(Calendar.HOUR_OF_DAY, 6);
        dateEnd.set(Calendar.MINUTE, 00);
        dateEnd.set(Calendar.SECOND, 00);
        return dateEnd.getTime();
    }


    /**
     * 获取系统时间戳
     * @return
     */
    public long getCurTimeLong(){
        long time=System.currentTimeMillis();
        return time;
    }
    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getCurDate(String pattern){
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new java.util.Date());
    }

    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 将字符串转为时间戳
     * @param dateString
     * @param pattern
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * 获取上一月
     * @param mill
     * @return
     */
    public static String getPreMonth(long mill){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(mill));
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("M");
        String time = format.format(c.getTime());
        return time;
    }

    /**
     * 获取下一月
     * @param mill
     * @return
     */
    public static String getNextMonth(long mill){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(mill));
        c.add(Calendar.MONTH, +1);
        SimpleDateFormat format = new SimpleDateFormat("M");
        String time = format.format(c.getTime());
        return time;
    }

    /**
     * 分开获取年月日
     * @param mill
     * @return
     */
    public static int[] getYearMonthDay(long mill){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mill);
        return new int[]{calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+ 1,calendar.get(Calendar.DAY_OF_MONTH)};
    }

    /**
     * 获取指定年月的第一天
     * @param year
     * @param month
     * @return
     */
    public static long getFirstDayOfMonth1(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        cal.set(Calendar.HOUR_OF_DAY,00);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,00);
        return cal.getTimeInMillis();
    }

    /**
     * 获取指定年月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static long getLastDayOfMonth1(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.HOUR_OF_DAY,23);
        cal.set(Calendar.MINUTE,59);
        cal.set(Calendar.SECOND,59);
        return cal.getTimeInMillis();
    }

    /**
     * 根据时间戳获取多少小时多少分 eg:1h35min
     * @param time
     * @return
     */
    public static String getHourAndMin(long time){

        long min = (long) (time/(1000*60f)+0.5);
        long hour = (long) (min/60f);
        return hour+ StringUtils.getString(R.string.dailybs_hour_h) + min % 60+StringUtils.getString(R.string.min);
    }

    public static String getWeek(int index) {
        String week;
        if (index == 1) {
            week = "Mon";
        } else if (index == 2) {
            week = "Tue";
        } else if (index == 3) {
            week = "Wed";
        } else if (index == 4) {
            week = "Thu";
        } else if (index == 5) {
            week = "Fri";
        } else if (index == 6) {
            week = "Sat";
        } else {
            week = "Sun";
        }
        return week;
    }
}
