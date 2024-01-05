package com.sisensing.common.utils;

import com.blankj.utilcode.util.StringUtils;
import com.sisensing.common.R;

public class SGUtils {

    /**
     * 剩余时间
     * 14*24*60=20160；
     *
     * @return
     */
    public static String getRemainingTime(int index) {
        //距离14天剩余待发送index
        int remainIndex = 20160 - index;
        if (remainIndex >= 0 && remainIndex <= 60) {
            return remainIndex + " " + StringUtils.getString(R.string.min);
        } else {
            String syDay;
            int day = remainIndex / (1440);
            if (day == 0) {
                syDay = (remainIndex / 60) + " " + StringUtils.getString(R.string.hour);
            } else {
                String suffixDay = " " + StringUtils.getString(R.string.day);
                String suffixDays = " " + StringUtils.getString(R.string.days);
                if (remainIndex % 1440 == 0) {
                    syDay = day > 1 ? day + suffixDays : day + suffixDay;
                } else {
                    syDay = (day + 1) > 1 ? (day + 1) + suffixDays : (day + 1) + suffixDays;
                }
            }
            return syDay;
        }
    }

    public static String getRemainingTimeUnit(int index) {
        //距离14天剩余待发送index
        int remainIndex = 20160 - index;
        if (remainIndex >= 0 && remainIndex <= 60) {
            return remainIndex + " " + StringUtils.getString(R.string.minutes);
        } else {
            String syDay;
            int day = remainIndex / (1440);
            if (day == 0) {
                syDay = (remainIndex / 60) + " " + StringUtils.getString(R.string.hour);
            } else {
                String suffixDay = " " + StringUtils.getString(R.string.day);
                String suffixDays = " " + StringUtils.getString(R.string.days);
                if (remainIndex % 1440 == 0) {
                    syDay = day > 1 ? day + suffixDays : day + suffixDay;
                } else {
                    syDay = (day + 1) > 1 ? (day + 1) + suffixDays : (day + 1) + suffixDays;
                }
            }
            return syDay;
        }
    }

    public static int getUsedMinute(long endTime) {
        int remainingMinute = (int) (endTime - System.currentTimeMillis()) / 60000;
        int usedMinute = 20160 - Math.max(remainingMinute, 0);
        return usedMinute;
    }

    /**
     * 获取血糖单位
     */
    public static String getUnit(boolean isMmol) {
        return StringUtils.getString(isMmol ? R.string.mol_unit : R.string.mg_dl);
    }

}
