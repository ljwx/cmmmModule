package com.ljwx.baseview.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.ljwx.baseview.R;

public class DrawableTextView extends AppCompatTextView {

    private Drawable drawableLeft = null, drawableTop = null, drawableRight = null,
            drawableBottom = null;
    private int drawableWidth, drawableHeight, drawableSize;

    private int drawablePadding;

    public DrawableTextView(Context context) {
        this(context, null);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.DrawableTextView_drawableRight) {
                drawableRight = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.DrawableTextView_drawableLeft) {
                drawableLeft = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.DrawableTextView_drawableTop) {
                drawableTop = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.DrawableTextView_drawableBottom) {
                drawableBottom = typedArray.getDrawable(attr);
            } else if (attr == R.styleable.DrawableTextView_drawableWidth) {
                drawableWidth = typedArray.getDimensionPixelSize(attr, 0);
            } else if (attr == R.styleable.DrawableTextView_drawableHeight) {
                drawableHeight = typedArray.getDimensionPixelSize(attr, 0);
            }
        }
        drawableSize = typedArray.getDimensionPixelSize(R.styleable.DrawableTextView_drawableSize, 0);
        if (drawableSize > 0) {
            drawableWidth = drawableSize;
            drawableHeight = drawableSize;
        }
        if (null != drawableLeft) {
            drawableLeft.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        if (null != drawableRight) {
            drawableRight.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        if (null != drawableTop) {
            drawableTop.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        if (null != drawableBottom) {
            drawableBottom.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        drawablePadding = getCompoundDrawablePadding();
        boolean show = typedArray.getBoolean(R.styleable.DrawableTextView_drawableShow, true);
        if (show) {
            setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
        }
        typedArray.recycle();
    }

    public void setDrawableShow(boolean show) {
        if (show) {
            setCompoundDrawablePadding(drawablePadding);
            setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
        } else {
            setCompoundDrawablePadding(0);
            setCompoundDrawables(null, null, null, null);
        }
    }

    public void setDrawableTop(Drawable drawableTop) {
        drawableTop.setBounds(0, 0, drawableWidth, drawableHeight);
        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void setDrawableBottom(Drawable drawableBottom) {
        drawableBottom.setBounds(0, 0, drawableWidth, drawableHeight);
        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void setDrawableRight(Drawable drawableRight) {
        if (drawableRight != null) {
            drawableRight.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void setDrawableLeft(Drawable drawableLeft) {
        if (drawableLeft != null) {
            drawableLeft.setBounds(0, 0, drawableWidth, drawableHeight);
        }
        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    public void setDrawableWidth(int drawableWidth) {
        this.drawableWidth = drawableWidth;
    }

    public void setDrawableHeight(int drawableHeight) {
        this.drawableHeight = drawableHeight;
    }
}