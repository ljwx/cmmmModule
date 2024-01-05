package com.sisensing.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.sisensing.common.R;
import com.sisensing.common.entity.actionRecord.ActionRecordEntity;
import com.sisensing.common.entity.actionRecord.ActionRecordEnum;
import com.sisensing.common.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * projectName: sibionicsCGM
 * packageName: com.sisensing.hospital_v1.widget
 * author: tanLiang
 * Date: 2019/8/14  09:56
 * Description:
 **/
public class EventRecordProgressView extends View {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<ActionRecordEntity> recordEntityList = new ArrayList<>();
    private long startTimeMills;
    private long endTimeMills;
    private float denominator;
    private OnClickEventListener listener;
    private Point xPos[];
    private float mBitmapWidth;

    private float mTop;
    private float mLastLeft;
    private float mSpace;
    private int mHour;

    public void setOnClickEventListener(OnClickEventListener listener) {
        this.listener = listener;
    }

    public EventRecordProgressView(Context context) {
        super(context);
    }

    public EventRecordProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mSpace = SizeUtils.dp2px(2);
    }

    public EventRecordProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setTimeMills(long startTimeMills, long endTimeMills, int hour) {
        this.startTimeMills = startTimeMills;
        this.endTimeMills = endTimeMills;
        this.denominator = (endTimeMills - startTimeMills) * 1.0f;
        this.mHour = hour;
    }

    public void setValues(List<ActionRecordEntity> values, int hour) {
        this.mHour = hour;
        recordEntityList.clear();
        if (ObjectUtils.isNotEmpty(values)) {
            recordEntityList.addAll(values);
            xPos = new Point[recordEntityList.size()];
        }
        invalidate();
    }

    public void addValue(ActionRecordEntity value, int hour) {
        this.mHour = hour;
        if (value != null) {
            recordEntityList.add(value);
            xPos = new Point[recordEntityList.size()];
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.clipRect(0, 0, getWidth(), getHeight());
        mPaint.setStrokeWidth(2.5f);
        mPaint.setColor(Color.TRANSPARENT);
        textPaint.setTextSize(22.0f);
        textPaint.setColor(getContext().getResources().getColor(R.color.black));
        mPaint.setColor(Color.parseColor("#EBEBEB"));

        mTop = 0;

        if (ObjectUtils.isNotEmpty(recordEntityList)) {
            Log.d("打卡", "drawBitmap,数据:"+recordEntityList.size());
            for (int i = 0; i < recordEntityList.size(); i++) {
                ActionRecordEntity entity = recordEntityList.get(i);
                drawBitmap(canvas, entity, i);
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawBitmap(Canvas canvas, ActionRecordEntity entity, int index) {

        int width = getWidth();
        if (entity != null) {
            Bitmap bitmap;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 1;
            long timeMills = entity.getStartTime();
            long dTime = timeMills - startTimeMills;

            if (entity.getType() == ActionRecordEnum.FOOD.getType()) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bs_food_small, options);
            } else if (entity.getType() == ActionRecordEnum.SPORTS.getType()) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bs_sport_small, options);
            } else if (entity.getType() == ActionRecordEnum.MEDICATIONS.getType()) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bs_medication_small, options);
            } else if (entity.getType() == ActionRecordEnum.INSULIN.getType()) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bs_insulin_small, options);
            } else if (entity.getType() == ActionRecordEnum.SLEEP.getType()) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bs_sleep_small, options);
            } else if (entity.getType() == ActionRecordEnum.FINGER_BLOOD.getType()) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bs_finger_blood_small, options);
            } else if (entity.getType() == ActionRecordEnum.PHYSICAL_STATE.getType()) {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bs_body_small, options);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_bs_food_small, options);
            }
            bitmap = scaleImage(bitmap, SizeUtils.dp2px(15), SizeUtils.dp2px(15));
            mPaint.setColor(Color.WHITE);
            Log.d("打卡icon", "当前事件跟开始时间时差:"+(dTime/(60*1000))+"分钟,分母:"+denominator+",控件总宽:"+width);
            float left = dTime / denominator * width;


            mBitmapWidth = bitmap.getWidth();
            if (left > (width - mBitmapWidth)) {
                left = width - mBitmapWidth;
            }

            Log.d("打卡icon", "lastLeft:"+mLastLeft+",left:"+left+",bitmapWidth:"+bitmap.getWidth()+",bitmapHigh:"+bitmap.getHeight());
            if (mLastLeft + bitmap.getWidth() < left + 5) {
                mTop = 0;
            }

            Point point = new Point((int) left, (int) mTop);
            xPos[index] = point;

            try {
                canvas.drawBitmap(bitmap, left, mTop, mPaint);
            } catch (Exception e) {
                Log.d("打卡", "图标绘画异常:"+e);
            }

            mLastLeft = left;
            mTop += bitmap.getHeight() + mSpace;
        }
    }

    private static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);

        return newBm;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if ((x + getLeft() < getRight()) && (y + getTop() < getBottom()) && !recordEntityList.isEmpty()) {
                    for (int i = 0; i < xPos.length; i++) {
                        int posX = xPos[i].x;
                        int posY = xPos[i].y;
                        float bitmapW = mBitmapWidth;

                        LogUtils.i(x + "---------------------" + y + "\n" + posX + "---------------------" + posY + "\n" + "-------------" + mBitmapWidth);
                        if (x >= posX && x <= posX + bitmapW && y >= posY && y <= posY + mBitmapWidth) {
                            ActionRecordEntity entity = recordEntityList.get(i);
                            if (listener != null) {
                                listener.onClickEvent(entity);
                            }
                            break;
                        }
                    }
                }
                break;
        }
        return true;
    }


    public interface OnClickEventListener {
        void onClickEvent(ActionRecordEntity actionRecordEntity);
    }
}
