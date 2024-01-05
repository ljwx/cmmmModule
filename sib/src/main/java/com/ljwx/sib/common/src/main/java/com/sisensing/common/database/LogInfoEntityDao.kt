package com.sisensing.common.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sisensing.common.entity.LogInfoEntity
import io.reactivex.Completable
import io.reactivex.Single


/**
 * @ClassName DeviceLogInfoDao
 * @Description
 * @Author xieyang
 * @Date 2023/8/4 15:35
 */
@Dao
interface LogInfoEntityDao {
    /**
     * 插入一条设备日志信息
     * @param deviceLog DeviceLogInfoEntity
     * @return Completable
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLog(deviceLog: LogInfoEntity): Single<Long>

    /**
     * 删除一条设备日志信息
     * @param timestamp Long
     * @return Completable
     */
    @Query("DELETE FROM LogInfoEntity WHERE timestamp = :timestamp")
    fun deleteLogByTimestamp(timestamp: Long): Completable

    @Delete
    fun deleteLogs(logs: List<LogInfoEntity>)

    @Query("SELECT * FROM LogInfoEntity ORDER BY timestamp ASC LIMIT 10")
    fun getFirst10Logs(): Single<List<LogInfoEntity>>
}