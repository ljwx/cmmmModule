package com.sisensing.common.entity.Device;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.sisensing.common.database.RoomResponseListener;
import com.sisensing.common.user.UserInfoUtils;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.entity.Device
 * @Author: f.deng
 * @CreateDate: 2021/3/18 9:09
 * @Description:
 */
public class DeviceManager {


    private DeviceManager() {

    }

    private static class Inner {
        private static DeviceManager INSTANCE = new DeviceManager();
    }

    public static DeviceManager getInstance() {
        return Inner.INSTANCE;
    }


    private DeviceEntity mDeviceEntity;

    /**
     * 初始化一个全局device，避免频繁查询数据库
     * <p>
     * 1. 启动应用时判断是否登录 若登录初始化
     * 2. 重新登录时登录成功初始化
     *
     * @param userId
     */
    public void init(final String userId, final DeviceInitListener listener) {

        DeviceRepository.getInstance().queryMaxConnectMill(userId, new RoomResponseListener<DeviceEntity>() {
            @Override
            public void response(@Nullable DeviceEntity deviceEntity) {
                if (deviceEntity == null) {
                    mDeviceEntity = new DeviceEntity();
                    mDeviceEntity.setUserId(userId);
                    //DeviceRepository.getInstance().insert(mDeviceEntity);
                } else {
                    mDeviceEntity = deviceEntity;
                }

                listener.initComplete(mDeviceEntity);

            }
        });
    }


    /**
     * 获取全局设备实例
     *
     * @return
     */
    public DeviceEntity getDeviceEntity() {

        if (mDeviceEntity == null) {
            mDeviceEntity = DeviceRepository.getInstance().queryDeviceAllowMainThread(UserInfoUtils.getUserId());
        }

        return mDeviceEntity;
    }

    public void setDeviceEntity(DeviceEntity deviceEntity) {
        this.mDeviceEntity = deviceEntity;
    }


    /**
     * 更新连接码
     *
     * @param linkCode
     */
    public void updateLinkCode(String linkCode) {
        mDeviceEntity.setBlueToothNum(linkCode);
        DeviceRepository.getInstance().update(mDeviceEntity);
    }


    /**
     * 更新设备
     *
     * @param deviceEntity
     */
    public void updateDevice(DeviceEntity deviceEntity) {
        mDeviceEntity = deviceEntity;
        DeviceRepository.getInstance().update(deviceEntity);
    }

    /**
     * 新增一个设备
     *
     * @param deviceEntity
     */
    public void insertDevice(DeviceEntity deviceEntity) {
        mDeviceEntity = deviceEntity;
        DeviceRepository.getInstance().insert(deviceEntity);
    }

    /**
     * 设置传感器状态，即异常状态
     */
    public void setDeviceStatus(int deviceStatus) {
        mDeviceEntity.setDeviceStatus(deviceStatus);
    }

    public interface DeviceInitListener {

        void initComplete(DeviceEntity deviceEntity);
    }

}
