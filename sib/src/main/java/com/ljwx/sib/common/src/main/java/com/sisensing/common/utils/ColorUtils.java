package com.sisensing.common.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * @author y.xie
 * @date 2021/3/20 14:24
 * @desc
 */
public class ColorUtils {
    /**
     * 生成随机颜色
     * @return
     */
    public static int randomColor(){
        Random random = new Random();
        int red = random.nextInt(200);
        int green = random.nextInt(200);
        int blue = random.nextInt(200);
        return Color.rgb(red,green,blue);
    }
}
