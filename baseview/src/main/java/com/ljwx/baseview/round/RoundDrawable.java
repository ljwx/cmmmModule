package com.ljwx.baseview.round;

import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.Nullable;

/**
 *
 */

public class RoundDrawable extends GradientDrawable {
    private ColorStateList mSolidColor;
    private int mFillColor;
    private float mPressedRatio;
    @Nullable
    private int[] mFillGradientColors;
    @Nullable
    private ColorStateList[] mGradientColorLists;
    private Rect mBoundsRect;

    public RoundDrawable(ColorStateList solidColor, String gradientColors, float pressedRatio) {
        mSolidColor = solidColor;
        mPressedRatio = pressedRatio;

        if (mSolidColor == null) {
            mSolidColor = ColorStateList.valueOf(0);
        }
        if (mSolidColor.isStateful()) {
            setSolidColors(mSolidColor);
        } else if (mPressedRatio > 0.0001f) {
            setSolidColors(RoundUtil.csl(mSolidColor.getDefaultColor(), mPressedRatio));
        } else {
            setColor(mSolidColor.getDefaultColor());
        }
        setGradientColors(RoundUtil.parseGradientColors(gradientColors));
    }

    public void setGradientColors(@Nullable int[] gradientColors) {
        mFillGradientColors = gradientColors;
        if (gradientColors != null && gradientColors.length > 1) {
            mGradientColorLists = new ColorStateList[gradientColors.length];
            for (int i = 0; i < mGradientColorLists.length; i++) {
                ColorStateList colorStateList = RoundUtil.csl(gradientColors[i], mPressedRatio);
                mGradientColorLists[i] = colorStateList;
            }
            setColors(gradientColors);
        }
    }

    public void setBoundsRect(Rect boundsRect) {
        mBoundsRect = boundsRect;
    }


    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        if (mBoundsRect != null)
            super.setBounds(left + mBoundsRect.left, top + mBoundsRect.top,
                    right - mBoundsRect.right, bottom - mBoundsRect.bottom);
        else
            super.setBounds(left, top, right, bottom);
    }

    private void setSolidColors(ColorStateList colors) {
        mSolidColor = colors;
        setColor(colors.getDefaultColor());
    }

    @Override
    public void setColor(int argb) {
        mFillColor = argb;
        super.setColor(argb);
    }

    @Override
    protected boolean onStateChange(int[] stateSet) {
        if (mGradientColorLists != null && mFillGradientColors != null) {
            for (int i = 0; i < mGradientColorLists.length; i++) {
                int newColor = mGradientColorLists[i].getColorForState(stateSet, 0);
                mFillGradientColors[i] = newColor;
            }
            setColors(mFillGradientColors);
            return true;
        }
        if (mSolidColor != null) {
            final int newColor = mSolidColor.getColorForState(stateSet, 0);
            if (mFillColor != newColor) {
                setColor(newColor);
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean isStateful() {
        return super.isStateful() || (mSolidColor != null && mSolidColor.isStateful());
    }
}
