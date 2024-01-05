package com.sisensing.common.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author xieyang
 * @desc 实时监测分享内容实体类
 */
public class BsShareContentEntity implements Parcelable {
    private int type;
    private Bitmap bitmap;

    public BsShareContentEntity() {
    }

    protected BsShareContentEntity(Parcel in) {
        type = in.readInt();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<BsShareContentEntity> CREATOR = new Creator<BsShareContentEntity>() {
        @Override
        public BsShareContentEntity createFromParcel(Parcel in) {
            return new BsShareContentEntity(in);
        }

        @Override
        public BsShareContentEntity[] newArray(int size) {
            return new BsShareContentEntity[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeParcelable(bitmap, flags);
    }
}
