package com.sisensing.common.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.utils
 * Author: f.deng
 * CreateDate: 2021/3/22 15:38
 * Description:
 */
public class MathUtil {
    // 通过 -?[0-9]+(\\\\.[0-9]+)? 进行匹配是否为数字
    private static Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");

    public static float getTransferPointData(float value, int point) {
        return BigDecimal.valueOf(value).setScale(point, BigDecimal.ROUND_DOWN).floatValue();
    }

    public static double getTransferPointData(double value, int point) {
        return BigDecimal.valueOf(value).setScale(point, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        // 通过Matcher进行字符串匹配
        Matcher m = pattern.matcher(str);
        // 如果正则匹配通过 m.matches() 方法返回 true ，反之 false
        return m.matches();
    }

}
