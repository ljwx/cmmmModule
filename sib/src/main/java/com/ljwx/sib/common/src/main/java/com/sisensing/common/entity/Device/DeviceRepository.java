package com.sisensing.common.entity.Device;

import android.os.AsyncTask;

import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.database.RoomResponseListener;
import com.sisensing.common.database.RoomTask;
import com.sisensing.common.user.UserInfoUtils;

import java.util.List;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.entity.Device
 * @Author: f.deng
 * @CreateDate: 2021/3/11 11:42
 * @Description:
 */
public class DeviceRepository {


    private DeviceEntityDao mDeviceEntityDao;

    private DeviceRepository() {
        mDeviceEntityDao = AppDatabase.getInstance().getDeviceDao();
    }

    private static class Inner {
        private static DeviceRepository INSTANCE = new DeviceRepository();
    }

    public static DeviceRepository getInstance() {
        return Inner.INSTANCE;
    }


    public void insert(DeviceEntity deviceEntity) {
        RoomTask.CompletableTask(mDeviceEntityDao.insert(deviceEntity));
    }


    public void update(DeviceEntity deviceEntity) {
        RoomTask.CompletableTask(mDeviceEntityDao.update(deviceEntity));
    }


    public void queryDeviceByUser(String userId, RoomResponseListener<DeviceEntity> listener) {
        RoomTask.singleTask(mDeviceEntityDao.findByUserId(userId), listener);
    }

    /**
     * 获取当前用户下的所有设备
     *
     * @param userId
     * @param listener
     */
    public void queryUserAllDevice(String userId, RoomResponseListener<List<DeviceEntity>> listener) {
        RoomTask.singleTask(AppDatabase.getInstance().getDeviceDao().findAllDeviceByUserId(userId), listener);
    }

    /**
     * 查询最后连接的设备
     *
     * @param userId
     * @param listener
     */
    public void queryMaxConnectMill(String userId, RoomResponseListener<DeviceEntity> listener) {
        RoomTask.singleTask(mDeviceEntityDao.findByUserIdAndMillMax(userId), listener);
    }


    public void updateConnectMill(String deviceName, long connectMills) {
        mDeviceEntityDao.updateUserDeviceConnectMill(deviceName, UserInfoUtils.getUserId(), connectMills);
    }

    public void updateConnectMillAndCharacteristic(String userId,String deviceName, long connectMills,String characteristic){
        mDeviceEntityDao.updateConnectMillAndcharacteristic(userId,deviceName,connectMills,characteristic);
    }

    public void updateDeviceStatusAndFirstMill(String userId,String deviceName, int deviceStatus, int uploadStatus, long connectMills) {
        mDeviceEntityDao.updateDeviceStatusAndFirstMill(userId,deviceName, deviceStatus, uploadStatus, connectMills);
    }

    public void updateDeviceStatus(String userid,String deviceName, int deviceStatus, int uploadStatus) {
        mDeviceEntityDao.updateDeviceStatus(userid,deviceName, deviceStatus, uploadStatus);
    }

    /**
     * 通过设备连接码查找设备
     *
     * @param blueToothNum
     * @param listener
     */
    public void queryDeviceByBlueToothNum(String blueToothNum, RoomResponseListener<DeviceEntity> listener) {
        RoomTask.singleTask(mDeviceEntityDao.findByLinkCode(blueToothNum), listener);
    }

    public void queryDeviceByLinkCodeAndUserId(String linkCode, String userId, RoomResponseListener<DeviceEntity> listener) {
        RoomTask.singleTask(mDeviceEntityDao.findByLinkCodeAndUserId(linkCode, userId), listener);
    }

    private static class InsertAsyncTask extends AsyncTask<DeviceEntity, Void, Void> {
        private DeviceEntityDao mDeviceEntityDao;

        InsertAsyncTask(DeviceEntityDao deviceEntityDao) {
            this.mDeviceEntityDao = deviceEntityDao;
        }

        @Override
        protected Void doInBackground(DeviceEntity... deviceEntities) {
            mDeviceEntityDao.insert(deviceEntities[0]);
            return null;
        }
    }


    public DeviceEntity queryDeviceAllowMainThread(String userId) {
        return AppDatabase.getInstance().getDeviceDao().queryDeviceByUserId(userId);
    }

    public DeviceEntity queryDevice(String userid,String deviceId){
        return mDeviceEntityDao.queryDeviceByUserIdAndDeviceId(userid,deviceId);
    }
}
