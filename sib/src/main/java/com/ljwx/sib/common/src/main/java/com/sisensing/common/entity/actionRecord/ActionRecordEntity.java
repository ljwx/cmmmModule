package com.sisensing.common.entity.actionRecord;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.sisensing.common.utils.ConfigUtils;
import com.sisensing.common.utils.StringUtilsKt;

import java.io.File;
import java.util.List;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.entity.actionRecord
 * Author: f.deng
 * CreateDate: 2021/3/23 17:25
 * Description:
 */
@Entity(indices = {@Index(value = {"startTime", "dataId"},
        unique = true)})
public class ActionRecordEntity implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;     //事件名称
    private int type;        //事件标识
    private long startTime;//开始时间
    private long endTime;  //结束时间
    private String userId;
    private String dataId;


    private String eventType;//事件类型
    private String eventDetail;//事件详情
    private String eventConsume;//事件消耗
    private int uploadService = 0;//上传服务器是否成功(默认0，成功1)
    private int oneHoursRepeatClockIn = 0;//一小时是否重复打卡(默认0，重复打卡1)
    private int wearClockIn = 0;//穿戴设备打卡(0:手机打卡,1:穿戴设备打卡)
    private String batchActionData;//批量打卡数据
    private String sgUnit;//血糖单位

    @Ignore
    private List<String> actionImgs;//打卡图片

    private int editModel;//是否编辑

    public ActionRecordEntity() {
    }

    protected ActionRecordEntity(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        type = in.readInt();
        startTime = in.readLong();
        endTime = in.readLong();
        userId = in.readString();
        eventType = in.readString();
        eventDetail = in.readString();
        eventConsume = in.readString();
        uploadService = in.readInt();
        oneHoursRepeatClockIn = in.readInt();
        wearClockIn = in.readInt();
        batchActionData = in.readString();
        sgUnit = in.readString();
        dataId = in.readString();
        editModel = in.readInt();
    }

    public static final Creator<ActionRecordEntity> CREATOR = new Creator<ActionRecordEntity>() {
        @Override
        public ActionRecordEntity createFromParcel(Parcel in) {
            return new ActionRecordEntity(in);
        }

        @Override
        public ActionRecordEntity[] newArray(int size) {
            return new ActionRecordEntity[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventConsume() {
        return eventConsume;
    }

    public void setEventConsume(String eventConsume) {
        this.eventConsume = eventConsume;
    }

    public String getEventDetail() {
        if (type == ActionRecordEnum.FINGER_BLOOD.getType() && StringUtilsKt.isDigit(eventDetail)
                && !ConfigUtils.getInstance().unitIsMmol()
        ) {
            return StringUtilsKt.floatString2Int(eventDetail)+"";
        }
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public int getUploadService() {
        return uploadService;
    }

    public void setUploadService(int uploadService) {
        this.uploadService = uploadService;
    }

    public int getOneHoursRepeatClockIn() {
        return oneHoursRepeatClockIn;
    }

    public void setOneHoursRepeatClockIn(int oneHoursRepeatClockIn) {
        this.oneHoursRepeatClockIn = oneHoursRepeatClockIn;
    }

    public int getWearClockIn() {
        return wearClockIn;
    }

    public void setWearClockIn(int wearClockIn) {
        this.wearClockIn = wearClockIn;
    }

    public String getBatchActionData() {
        return batchActionData;
    }

    public void setBatchActionData(String batchActionData) {
        this.batchActionData = batchActionData;
    }

    public String getSgUnit() {
        return sgUnit;
    }

    public void setSgUnit(String sgUnit) {
        this.sgUnit = sgUnit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<String> getActionImgs() {
        return actionImgs;
    }

    public void setActionImgs(List<String> actionImgs) {
        this.actionImgs = actionImgs;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeInt(type);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(userId);
        dest.writeString(eventType);
        dest.writeString(eventDetail);
        dest.writeString(eventConsume);
        dest.writeInt(uploadService);
        dest.writeInt(oneHoursRepeatClockIn);
        dest.writeInt(wearClockIn);
        dest.writeString(batchActionData);
        dest.writeString(sgUnit);
        dest.writeString(dataId);
        dest.writeInt(editModel);
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public boolean isEditModel() {
        return editModel == 1;
    }

    public int getEditModel() {
        return editModel;
    }

    public void setEditModel(int editModel) {
        this.editModel = editModel;
    }
}
