package com.sisensing.common.entity.BloodGlucoseEntity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author y.xie
 * @date 2021/3/20 11:22
 * @desc 多日趋势图 - 多日叠加 - 多日血糖曲线 - 点击可选中或取消相应日期
 */
public class MultiDayDateBean implements Parcelable {
    //当前日期起始时间戳
    private long startMills;
    private long endMills;
    private String date;
    private int bgColor;
    private boolean isSelect;

    public MultiDayDateBean() {

    }

    protected MultiDayDateBean(Parcel in) {
        startMills = in.readLong();
        endMills = in.readLong();
        date = in.readString();
        bgColor = in.readInt();
        isSelect = in.readByte() != 0;
    }

    public static final Creator<MultiDayDateBean> CREATOR = new Creator<MultiDayDateBean>() {
        @Override
        public MultiDayDateBean createFromParcel(Parcel in) {
            return new MultiDayDateBean(in);
        }

        @Override
        public MultiDayDateBean[] newArray(int size) {
            return new MultiDayDateBean[size];
        }
    };

    public long getStartMills() {
        return startMills;
    }

    public void setStartMills(long startMills) {
        this.startMills = startMills;
    }

    public long getEndMills() {
        return endMills;
    }

    public void setEndMills(long endMills) {
        this.endMills = endMills;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(startMills);
        dest.writeLong(endMills);
        dest.writeString(date);
        dest.writeInt(bgColor);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }
}
