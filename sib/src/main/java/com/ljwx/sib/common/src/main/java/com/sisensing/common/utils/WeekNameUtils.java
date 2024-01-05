package com.sisensing.common.utils;

import android.content.Context;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeekNameUtils {

    /**
     * 根据app当前语言返回星期
     *
     * @param millis 对应时间戳
     * @return app语言对应的星期
     */
    public static String getLocalWeek(long millis) {
        final Date date = new Date(millis);
        Locale local = AppLanguageUtils.getSelectLanguageLocale();
        String result = StringUtils.upperFirstLetter(new SimpleDateFormat("EEEE", local).format(date));
        return result;
    }

    /**
     * 根据app当前语言返回星期
     *
     * @param millis 对应时间戳
     * @return app语言对应的星期的简称
     */
    public static String getLocalWeekShort(long millis) {
        String weekStr = getLocalWeek(millis);
        if (ObjectUtils.isNotEmpty(weekStr) && weekStr.length() > 3) {
            weekStr = weekStr.substring(0, 3);
        } else {
            weekStr = "";
        }
        return weekStr;
    }

    public static String getWeekShort(Context context, int weekDay) {
        if (context == null) {
            return "";
        }
        return context.getResources().getStringArray(com.haibin.calendarview.R.array.week_string_array)[weekDay];
    }

}
