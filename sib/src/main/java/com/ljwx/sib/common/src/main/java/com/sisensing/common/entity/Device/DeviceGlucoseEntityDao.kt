package com.sisensing.common.entity.Device

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface DeviceGlucoseEntityDao {

    @Insert
    fun insert(bloodGlucoseEntity: DeviceGlucoseEntity?)

    @Query("select * from RemoteGlucoseEntity")
    fun getDeviceGlucose(): Single<DeviceGlucoseEntity>

    /**
     * 获取60条之后的所有有效血糖数据
     *
     * @param userId
     * @return
     */
//    @Query("select * from BloodGlucoseEntity where (userId=:userId or userId is null) and `index`>=60 and `index`<=20160 and validIndex>0 and  bleName like :deviceName")
    @Query("select * from RemoteGlucoseEntity where (userId=:userId or userId is null) and `index`>=60 and `index`<=20160")
    fun getAllValidBloodGlucose(
        userId: String?,
    ): Observable<List<BloodGlucoseEntity>?>

    /**
     * 获取时间范围血糖数据
     *
     * @param deviceName
     * @return
     */
    @Query("select * from RemoteGlucoseEntity where (userId=:userId or userId is null) and `processedTimeMill` between :startMill and :endMill order by `processedTimeMill` asc")
    fun getRangeValidBloodGlucose(
        userId: String?,
        startMill: Long,
        endMill: Long
    ): Observable<List<BloodGlucoseEntity>>

    @Query("select * from RemoteGlucoseEntity where (userId=:userId or userId is null) and `index`>=60 and `index`<=20160 and `processedTimeMill` between :startMill and :endMill order by `processedTimeMill` asc")
    fun getRangeValidBloodGlucoseSuspend(
        userId: String?,
        startMill: Long,
        endMill: Long
    ): List<BloodGlucoseEntity>?

    @Query("select * from RemoteGlucoseEntity where `index`>=60 and `index`<=20160 and (userId=:userId or userId is null) order by `processedTimeMill` asc limit 1")
    fun getFirstBloodGlucose(
        userId: String?,
    ): Observable<List<BloodGlucoseEntity>>

    @Query("select * from RemoteGlucoseEntity where `index`>=60 and `index`<=20160 and (userId=:userId or userId is null) order by `processedTimeMill` asc limit 1")
    fun getFirstBloodGlucoseSuspend(
        userId: String?,
    ): BloodGlucoseEntity?

    @Query("select * from RemoteGlucoseEntity where `index`>=60 and `index`<=20160 and (userId=:userId or userId is null) order by `processedTimeMill` desc limit 1")
    fun getLastBloodGlucose(
        userId: String?,
    ): Observable<List<BloodGlucoseEntity>>

    @Query("select * from RemoteGlucoseEntity where `index`>=60 and `index`<=20160 and (userId=:userId or userId is null) order by `processedTimeMill` desc limit 1")
    fun getLastBloodGlucoseSuspend(
        userId: String?,
    ): BloodGlucoseEntity?

    @Query("select count(*) from RemoteGlucoseEntity where (userId=:userId or userId is null) and `processedTimeMill` between :startMill and :endMill")
    fun getRangeCount(userId: String?, startMill: Long, endMill: Long): Single<Int>

    @Query("select count(*) from RemoteGlucoseEntity where (userId=:userId or userId is null) ")
    fun getCount(userId: String?): Int

    @Query("delete from RemoteGlucoseEntity where (userId=:userId or userId is null)")
    fun deleteUserData(userId: String?)
}