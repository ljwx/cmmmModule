package com.sisensing.common.entity.clcok;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * @author y.xie
 * @date 2021/5/19 16:26
 * @desc 闹钟数据库实体类
 * 复合主键 frequency与time确定唯一性
 */
@Entity(primaryKeys = {"frequency","time"})
public class ClockEntity implements Parcelable {
    private String userId;
    private String name;          //闹钟名称
    @NotNull
    private String frequency;          //频率
    @NotNull
    private String time;          //时间点 HH:mm
    private boolean isActivated;  //是否已激活
    private int notifyStyle;//提醒方式 1.声音 2.震动

    public ClockEntity() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public int getNotifyStyle() {
        return notifyStyle;
    }

    public void setNotifyStyle(int notifyStyle) {
        this.notifyStyle = notifyStyle;
    }

    protected ClockEntity(Parcel in) {
        userId = in.readString();
        name = in.readString();
        frequency = in.readString();
        time = in.readString();
        isActivated = in.readByte() != 0;
        notifyStyle = in.readInt();
    }

    public static final Creator<ClockEntity> CREATOR = new Creator<ClockEntity>() {
        @Override
        public ClockEntity createFromParcel(Parcel in) {
            return new ClockEntity(in);
        }

        @Override
        public ClockEntity[] newArray(int size) {
            return new ClockEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(frequency);
        dest.writeString(time);
        dest.writeByte((byte) (isActivated ? 1 : 0));
        dest.writeInt(notifyStyle);
    }
}
