package com.ljwx.baseview.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.ljwx.baseview.R;

public class RoundImageView extends AppCompatImageView {

    private Path path = new Path();
    private Paint paint;
    private float mRadiuAll, mRadiuTopLeft, mRadiuTopRight, mRadiuBottomLeft, mRadiuBottomRight;
    private float mViewWidth, mViewHeight;
    private float mBorderWith;
    private int mBorderColor;

    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private float[] mRids = {0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 0.0f,};

    public RoundImageView(Context context) {
        this(context, null, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        mRadiuAll = array.getLayoutDimension(R.styleable.RoundImageView_riv_radiu_all, 0);
        mRadiuTopLeft = array.getDimension(R.styleable.RoundImageView_riv_radiu_top_left, 0);
        mRadiuTopRight = array.getDimension(R.styleable.RoundImageView_riv_radiu_top_right, 0);
        mRadiuBottomLeft = array.getDimension(R.styleable.RoundImageView_riv_radiu_bottom_left, 0);
        mRadiuBottomRight = array.getDimension(R.styleable.RoundImageView_riv_radiu_bottom_right, 0);
        mBorderColor = array.getColor(R.styleable.RoundImageView_riv_border_color, Color.parseColor("#ffffff"));
        mBorderWith = array.getDimension(R.styleable.RoundImageView_riv_border_width, 0);
        array.recycle();
        init();
    }

    public void init() {
        if (mRadiuAll > 0) {
            for (int i = 0; i < mRids.length; i++) {
                mRids[i] = mRadiuAll;
            }
        }
        if (mRadiuAll == 0) {
            mRids[0] = mRadiuTopLeft;
            mRids[1] = mRadiuTopLeft;
            mRids[2] = mRadiuTopRight;
            mRids[3] = mRadiuTopRight;
            mRids[4] = mRadiuBottomRight;
            mRids[5] = mRadiuBottomRight;
            mRids[6] = mRadiuBottomLeft;
            mRids[7] = mRadiuBottomLeft;
        }
        if (paint == null && mBorderWith > 0) {
            paint = new Paint();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mViewWidth = getWidth();
        mViewHeight = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        clipRadius(canvas);
        super.onDraw(canvas);
        drawBorders(canvas);
    }

    /**
     * 切圆角
     */
    private void clipRadius(Canvas canvas){
        if (mViewHeight < 1) {
            return;
        }
        path.reset();
        if (mRadiuAll == -1) {
            //圆形
            path.addCircle(mViewWidth / 2, mViewHeight / 2, mViewWidth / 2, Path.Direction.CW);
        } else {
            //圆角矩形
            path.addRoundRect(0, 0, mViewWidth, mViewHeight, mRids, Path.Direction.CW);
        }
        canvas.clipPath(path);
    }

    /**
     * 描边框
     */
    private void drawBorders(Canvas canvas) {
        if (mBorderWith > 0) {
            initBorderPaint();
            if (mRadiuAll == -1) {
                path.addCircle(mViewWidth / 2, mViewHeight / 2, mViewWidth / 2, Path.Direction.CCW);
            } else {
                path.addRoundRect(0, 0, mViewWidth, mViewHeight, mRids, Path.Direction.CCW);
            }
            canvas.drawPath(path, paint);
        }
    }

    /**
     * 设置画笔
     */
    private void initBorderPaint() {
        path.reset();
        // 设置画笔为描边模式
        paint.setStyle(Paint.Style.STROKE);
        // 描边宽度
        paint.setStrokeWidth(mBorderWith);
        //毛边
        paint.setAntiAlias(true);
        // 描边颜色
        paint.setColor(mBorderColor);
    }
}
