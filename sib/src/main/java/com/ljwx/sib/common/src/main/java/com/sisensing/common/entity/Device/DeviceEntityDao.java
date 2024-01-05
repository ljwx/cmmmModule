package com.sisensing.common.entity.Device;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.entity.Device
 * @Author: f.deng
 * @CreateDate: 2021/3/6 15:50
 * @Description:
 */
@Dao
public interface DeviceEntityDao {

    @Insert(onConflict = REPLACE)
    Completable insert(DeviceEntity device);

    @Delete
    Completable delete(DeviceEntity device);


    @Update
    Completable update(DeviceEntity device);


    @Query("select * from DeviceEntity")
    Single<List<DeviceEntity>> getAll();


    @Query("select * from DeviceEntity where userId = :userId")
    Single<DeviceEntity> findByUserId(String userId);


    @Query("select * from DeviceEntity where blueToothNum = :linkCode")
    Single<DeviceEntity> findByLinkCode(String linkCode);


    @Query("update DeviceEntity set algorithmContext=:algorithmContext , algorithmContextIndex=:index where deviceName = :deviceName")
    void updateAlgorithm(String deviceName, String algorithmContext, int index);

    @Query("update DeviceEntity set algorithmContext=:algorithmContext , algorithmContextIndex=:index where deviceName = :deviceName and userId=:userId")
    void updateAlgorithm(String userId,String deviceName, String algorithmContext, int index);

    @Query("update DeviceEntity set connectMill=:connectMill where deviceName = :deviceName")
    void updateConnectMill(String deviceName, long connectMill);

    @Query("update DeviceEntity set connectMill=:connectMill,characteristic=:characteristic where deviceName = :deviceName and userId=:userId")
    void updateConnectMillAndcharacteristic(String userId,String deviceName, long connectMill,String characteristic);

    @Query("update DeviceEntity set connectMill=:connectMill where deviceName = :deviceName and userId=:userId")
    void updateUserDeviceConnectMill(String deviceName,String userId, long connectMill);

    @Query("update DeviceEntity set deviceId=:deviceId where deviceName = :deviceName")
    void updateDeviceId(String deviceName, String deviceId);

    @Query("update DeviceEntity set deviceId=:deviceId where deviceName = :deviceName and userId=:userId")
    void updateDeviceId(String userId,String deviceName, String deviceId);

    @Query("update DeviceEntity set deviceStatus=:deviceStatus,uploadDeviceStatus=:uploadDeviceStatus where deviceName =:deviceName and userId=:userId")
    void updateDeviceStatus(String userId,String deviceName, int deviceStatus, int uploadDeviceStatus);


    @Query("update DeviceEntity set uploadDeviceStatus=:uploadDeviceStatus where deviceName = :deviceName")
    void updateDeviceUploadStatus(String deviceName,  int uploadDeviceStatus);

    @Query("update DeviceEntity set uploadDeviceStatus=:uploadDeviceStatus where deviceName = :deviceName and userId=:userId")
    void updateDeviceUploadStatus(String userId,String deviceName,  int uploadDeviceStatus);

    @Query("update DeviceEntity set deviceStatus=:deviceStatus,uploadDeviceStatus=:uploadDeviceStatus, firstBsMill=:firstBsMill where deviceName = :deviceName and userId=:userId")
    void updateDeviceStatusAndFirstMill(String userId,String deviceName, int deviceStatus, int uploadDeviceStatus, long firstBsMill);

    /**
     * 查询所有设备
     *
     * @param userId
     * @return
     */
    @Query("select * from DeviceEntity where userId = :userId and connectMill!=0")
    Single<List<DeviceEntity>> findAllDeviceByUserId(String userId);

    @Query("select * from DeviceEntity where userId = :userId")
    Single<List<DeviceEntity>> findAllDeviceByUserIdIncludeServer(String userId);

    /**
     * 通过连接码和用户id查询设备
     *
     * @param linkCode
     * @param userId
     * @return
     */
    @Query("select * from DeviceEntity where blueToothNum = :linkCode and userId= :userId")
    Single<DeviceEntity> findByLinkCodeAndUserId(String linkCode, String userId);

    /**
     * 通过设备名和用户id查询设备
     *
     * @param deviceName
     * @param userId
     * @return
     */
    @Query("select * from DeviceEntity where deviceName = :deviceName and userId= :userId")
    Single<DeviceEntity> findByDeviceNameAndUserId(String deviceName, String userId);

    /**
     * 查询connectMills最大的设备(最后连接的设备)
     *
     * @param userId
     * @return
     */
    @Query("select * from DeviceEntity where userId = :userId and connectMill!=0 order by connectMill desc limit 1")
    Single<DeviceEntity> findByUserIdAndMillMax(String userId);


    /**
     * 查询connectMills最大的设备(最后连接的设备)
     *
     * @param userId
     * @return
     */
    @Query("select * from DeviceEntity where userId = :userId and connectMill!=0 order by connectMill desc limit 1")
    LiveData<DeviceEntity> findLatestUseDevice(String userId);

    /**
     * 查询connectMills最大的设备(最后连接的设备)
     *
     * @param userId
     * @return
     */
    @Query("select * from DeviceEntity where userId = :userId order by connectMill desc limit 1")
    DeviceEntity queryDeviceByUserId(String userId);

    /**
     * 根据连接码查询出所有相同名字的设备
     *
     * @param linkCode
     * @return
     */
    @Query("select * from DeviceEntity where blueToothNum = :linkCode")
    Single<List<DeviceEntity>> findAllDeviceByLinkCode(String linkCode);

    /**
     * 根据
     * @param userId
     * @param deviceId
     * @return
     */
    @Query("select * from DeviceEntity where userId = :userId and deviceId = :deviceId")
    DeviceEntity queryDeviceByUserIdAndDeviceId(String userId,String deviceId);

    /**
     * 根据设备名删除设备
     * @param deviceName
     */
    @Query("delete from DeviceEntity where deviceName = :deviceName")
    void deleteDeviceByDeviceName(String deviceName);

    @Query("select * from DeviceEntity where userId = :userId and deviceId = :deviceId")
    Single<List<DeviceEntity>> findDeviceByDeviceId(String userId, String deviceId);
}
