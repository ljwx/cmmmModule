package com.ljwx.baseview.round;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;


public class RoundUtil {

    private RoundUtil() {
    }

    // 明度
    public static int darker(int color, float ratio) {
        color = (color >> 24) == 0 ? 0x22808080 : color;
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= ratio;
        return Color.HSVToColor(color >> 24, hsv);
    }

    public static ColorStateList csl(int normal, float ratio) {
        //        int disabled = greyer(normal);
        int pressed = darker(normal, ratio);
        int[][] states = new int[][]{{android.R.attr.state_pressed}, {}};
        int[] colors = new int[]{pressed, normal};
        return new ColorStateList(states, colors);
    }

    public static int[] parseGradientColors(int... colors) {
        int length = 0;
        for (int color : colors) {
            if (color != -1) {
                length++;
            }
        }
        if (length == 0) return null;
        int[] arr = new int[length];
        int index=0;
        for (int color : colors) {
            if (color != -1) {
                arr[index] = color;
                index++;
            }
        }
        return arr;
    }

    public static int[] parseGradientColors(String colors) {
        int[] array = null;
        if (!TextUtils.isEmpty(colors) && colors.contains(",")) {
            String[] colorArr = colors.split(",");
            array = new int[colorArr.length];
            for (int i = 0; i < colorArr.length; i++) {
                array[i] = Color.parseColor(colorArr[i]);
            }
        }
        return array;
    }

    public static GradientDrawable.Orientation parseGradientOrientation(int orientation) {
        switch (orientation) {
            case 0:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case 1:
                return GradientDrawable.Orientation.TR_BL;
            case 2:
                return GradientDrawable.Orientation.RIGHT_LEFT;
            case 3:
                return GradientDrawable.Orientation.BR_TL;
            case 4:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case 5:
                return GradientDrawable.Orientation.BL_TR;
            case 6:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case 7:
                return GradientDrawable.Orientation.TL_BR;
            default:
                return GradientDrawable.Orientation.TOP_BOTTOM;
        }
    }

    public static Paint initShadowPaint(int shadowColor, int shadowRadius, int offsetX, int offsetY) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(-1);
        paint.setStyle(Paint.Style.FILL);
        paint.setShadowLayer(shadowRadius, offsetX, offsetY, shadowColor);
        return paint;
    }

}
