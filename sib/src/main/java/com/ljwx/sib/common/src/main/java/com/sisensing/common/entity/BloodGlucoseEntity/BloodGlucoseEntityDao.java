package com.sisensing.common.entity.BloodGlucoseEntity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.entity.BloodGlucoseEntity
 * @Author: f.deng
 * @CreateDate: 2021/3/6 15:59
 * @Description:
 */
@Dao
public interface BloodGlucoseEntityDao {

    @Insert
    void insert(BloodGlucoseEntity bloodGlucoseEntity);

    @Delete
    void delete(BloodGlucoseEntity bloodGlucoseEntity);

    /**
     * 通过设备名删除所有血糖数据
     *
     * @param deviceName
     */
    @Query("delete from BloodGlucoseEntity where bleName = :deviceName")
    void deleteDeviceAllBsDataByDeviceName(String deviceName);

    @Query("select * from BloodGlucoseEntity where  bleName = :deviceName and (userId=:userId or userId is null) order by `index` desc limit 1")
    Single<BloodGlucoseEntity> getLastBloodGlucose(String userId, String deviceName);


    @Query("select * from BloodGlucoseEntity where `index`>=60 and `index`<=20160  and validIndex>0 and (userId=:userId or userId is null) and  bleName like :deviceName order by `index` desc limit 1")
    Single<BloodGlucoseEntity> getLastValidBloodGlucose(String userId, String deviceName);


    @Query("select * from BloodGlucoseEntity where `index`>=60 and `index`<=20160 and validIndex>0 and " +
            "processedTimeMill between:startTimeMill and :endTimeMill and bleName like :bleName and (userId=:userId or userId is null)")
    Single<List<BloodGlucoseEntity>> getPeriodBloodGlucose(String userId, String bleName, long startTimeMill, long endTimeMill);

    /**
     * @param
     * @return 获取超过当前index的血糖数据
     */
    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index` between :index+1 and 20160 and bleName like :bleName")
    Single<List<BloodGlucoseEntity>> getMoreThanIndexBloodGlucose(String userId, String bleName, int index);

    /**
     * @param
     * @return 获取超过当前index的血糖数据
     */
    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index` between :index+1 and :maxIndex and bleName like :bleName")
    Single<List<BloodGlucoseEntity>> getMoreThanIndexBloodGlucose(String userId, String bleName, int index,int maxIndex);

    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index` between :startIndex and 20160 and validIndex>0 and bleName like :bleName")
    Single<List<BloodGlucoseEntity>> getValidBloodGlucose(String userId, String bleName, int startIndex);


    /**
     * 获取60条之后的所有有效血糖数据
     *
     * @param deviceName
     * @return
     */
    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index`>=60 and `index`<=20160 and validIndex>0 and  bleName like :deviceName")
    Single<List<BloodGlucoseEntity>> getAllValidBloodGlucose(String userId, String deviceName);

    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index`>=60 and `index`<=20160 and validIndex>0 and  bleName like :deviceName order by `processedTimeMill` asc")
    Observable<List<BloodGlucoseEntity>> getAllValidBloodGlucose2(String userId, String deviceName);

    /**
     * 获取时间范围血糖数据
     *
     * @param deviceName
     * @return
     */
    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `processedTimeMill` between :startMill and :endMill and validIndex>0 and  bleName like :deviceName order by `processedTimeMill` asc")
    Observable<List<BloodGlucoseEntity>> getRangeValidBloodGlucose(String userId, String deviceName,long startMill, long endMill);

    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index`>=60 and `index`<=20160 and `processedTimeMill` between :startMill and :endMill and validIndex>0 and  bleName like :deviceName order by `processedTimeMill` asc")
    List<BloodGlucoseEntity> getRangeValidBloodGlucoseSuspend(String userId, String deviceName,long startMill, long endMill);

    /**
     * 获取所有血糖数据
     *
     * @param deviceName
     * @return
     */
    @Query("select * from BloodGlucoseEntity where  (userId=:userId or userId is null) and `index`>0 and`index`<=20160  and  bleName = :deviceName")
    Single<List<BloodGlucoseEntity>> getAllBloodGlucose(String userId, String deviceName);

    /**
     * 查询index=1的血糖数据
     *
     * @param bleName
     * @return
     */
    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index` =1 and bleName like :bleName")
    Single<BloodGlucoseEntity> getFirstBloodGlucose(String userId, String bleName);

    /**
     * 获取指定时间内的所有设备的血糖数据
     *
     * @param startTimeMill
     * @param endTimeMill
     * @return
     */
    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index`>=60 and`index`<=20160 and validIndex>0 and processedTimeMill between:startTimeMill and :endTimeMill")
    Single<List<BloodGlucoseEntity>> getAllDeviceValidBloodGlucose(String userId, long startTimeMill, long endTimeMill);

    /**
     * 根据蓝牙名称删除数据
     *
     * @param deviceName
     * @return
     */
    @Query("delete from BloodGlucoseEntity where bleName = :deviceName and (userId=:userId or userId is null)")
    Single<Integer> deleteBloodGlucoseByBle(String userId, String deviceName);


    /**
     * 查询index=1的血糖数据
     *
     * @param bleName
     * @return
     */
    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index` =1 and bleName like :bleName")
    Observable<List<BloodGlucoseEntity>> getFirstBloodGlucose2(String userId, String bleName);

    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index` =1 and bleName like :bleName")
    BloodGlucoseEntity getFirstBloodGlucoseSuspend(String userId, String bleName);

    @Query("select * from BloodGlucoseEntity where `index`>=60 and `index`<=20160  and validIndex>0 and (userId=:userId or userId is null) and  bleName like :deviceName order by `index` desc limit 1")
    Observable<List<BloodGlucoseEntity>> getLastValidBloodGlucose2(String userId, String deviceName);

    @Query("select * from BloodGlucoseEntity where `index`>=60 and `index`<=20160  and validIndex>0 and (userId=:userId or userId is null) and  bleName like :deviceName order by `index` desc limit 1")
    BloodGlucoseEntity getLastValidBloodGlucoseSuspend(String userId, String deviceName);
}
