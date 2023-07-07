package com.ljwx.baseview.round;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.AppCompatTextView;

import com.ljwx.baseview.R;


public final class RoundTextView extends AppCompatTextView {
    private static final int DEFAULT_SHADOW_COLOR = -1250068;
    public Paint mShadowPaint;
    private int mCornerRadius;
    private RectF mShadowRect;

    private RoundDrawable rd;
    private int gradientCenterColor;


    public RoundTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundTextView);
        int shadowColor = a.getColor(R.styleable.RoundTextView_rndShadowColor, DEFAULT_SHADOW_COLOR);
        final int shadowLen = a.getLayoutDimension(R.styleable.RoundTextView_rndShadowLen, 0);
        final int leftShadowLen = a.getLayoutDimension(R.styleable.RoundTextView_rndLeftShadowLen, shadowLen);
        final int topShadowLen = a.getLayoutDimension(R.styleable.RoundTextView_rndTopShadowLen, shadowLen);
        final int rightShadowLen = a.getLayoutDimension(R.styleable.RoundTextView_rndRightShadowLen, shadowLen);
        final int bottomShadowLen = a.getLayoutDimension(R.styleable.RoundTextView_rndBottomShadowLen, shadowLen);
        String gradientColors = a.getString(R.styleable.RoundTextView_rndGradientColors);
        int gradientType = a.getLayoutDimension(R.styleable.RoundTextView_rndGradientType, 0);
        float gradientCenterX = a.getFloat(R.styleable.RoundTextView_rndGradientCenterX, 0.5f);
        float gradientCenterY = a.getFloat(R.styleable.RoundTextView_rndGradientCenterY, 0.5f);
        int gradientRadius = a.getDimensionPixelSize(R.styleable.RoundTextView_rndGradientRadius, 0);
        int gradientStartColor = a.getColor(R.styleable.RoundTextView_rndGradientStartColor, -1);
        gradientCenterColor = a.getColor(R.styleable.RoundTextView_rndGradientCenterColor, -1);
        int gradientEndColor = a.getColor(R.styleable.RoundTextView_rndGradientEndColor, -1);
        int gradientOrientation = a.getColor(R.styleable.RoundTextView_rndGradientOrientation, 6);

        float pressedRatio = a.getFloat(R.styleable.RoundTextView_rndPressedRatio, 0.80f);
        mCornerRadius = a.getLayoutDimension(R.styleable.RoundTextView_rndCornerRadius, 0);

        ColorStateList solidColor = a.getColorStateList(R.styleable.RoundTextView_rndSolidColor);
        final int strokeColor = a.getColor(R.styleable.RoundTextView_rndStrokeColor, 0x0);
        final int strokeWidth = a.getDimensionPixelSize(R.styleable.RoundTextView_rndStrokeWidth, 0);
        final int strokeDashWidth = a.getDimensionPixelSize(R.styleable.RoundTextView_rndStrokeDashWidth, 0);
        final int strokeDashGap = a.getDimensionPixelSize(R.styleable.RoundTextView_rndStrokeDashGap, 0);
        final int shadowOffsetX = a.getDimensionPixelSize(R.styleable.RoundTextView_rndShadowOffsetX, 0);
        final int shadowOffsetY = a.getDimensionPixelSize(R.styleable.RoundTextView_rndShadowOffsetY, 0);
        a.recycle();
        setMaxLines(1);
        setGravity(Gravity.CENTER);
        rd = new RoundDrawable(solidColor, gradientColors, pressedRatio);
        if (mCornerRadius != -1) {
            rd.setCornerRadius(mCornerRadius);
        }
        rd.setStroke(strokeWidth, strokeColor, strokeDashWidth, strokeDashGap);
        rd.setOrientation(RoundUtil.parseGradientOrientation(gradientOrientation));
        int[] gradientColorArray = RoundUtil.parseGradientColors(gradientStartColor, gradientCenterColor, gradientEndColor);
        if (gradientColorArray != null && gradientColorArray.length > 1) {
            rd.setGradientColors(gradientColorArray);
        }
        rd.setGradientType(gradientType);
        rd.setGradientRadius(gradientRadius);
        rd.setGradientCenter(gradientCenterX, gradientCenterY);
        Rect rect = new Rect(leftShadowLen, topShadowLen, rightShadowLen, bottomShadowLen);
        rd.setBoundsRect(rect);

        final boolean isHasShadow = leftShadowLen > 0 || topShadowLen > 0 || rightShadowLen > 0 || bottomShadowLen > 0;
        setBackground(rd);
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
                    if (mCornerRadius == -1) {
                        mCornerRadius = Math.min(getMeasuredWidth() - leftShadowLen - rightShadowLen, getMeasuredHeight() - topShadowLen - bottomShadowLen) / 2;
                        rd.setCornerRadius(mCornerRadius);
                    }
                    if (isHasShadow && mShadowRect == null)
                        mShadowRect = new RectF(leftShadowLen + strokeWidth / 2.0f,
                                topShadowLen + strokeWidth / 2.0f,
                                getMeasuredWidth() - rightShadowLen - strokeWidth / 2.0f,
                                getMeasuredHeight() - bottomShadowLen - strokeWidth / 2.0f);
                    getViewTreeObserver().removeOnPreDrawListener(this);
                }
                return true;
            }
        });

        if (isHasShadow) {
            int shadowRadius = Math.max(Math.max(leftShadowLen, topShadowLen), Math.max(rightShadowLen, bottomShadowLen));
            mShadowPaint = RoundUtil.initShadowPaint(shadowColor, shadowRadius, shadowOffsetX, shadowOffsetY);
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        setPadding(leftShadowLen + getPaddingLeft(), topShadowLen + getPaddingTop(),
                rightShadowLen + getPaddingRight(), bottomShadowLen + getPaddingBottom());
    }

    public void setBGColor(int gradientStartColor, int gradientEndColor) {
        int[] gradientColorArray = RoundUtil.parseGradientColors(gradientStartColor, gradientCenterColor, gradientEndColor);
        if (gradientColorArray != null && gradientColorArray.length > 1) {
            rd.setGradientColors(gradientColorArray);
        }
        setBackground(rd);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mShadowRect != null)
            canvas.drawRoundRect(mShadowRect, mCornerRadius, mCornerRadius, mShadowPaint);
        super.draw(canvas);
    }
}