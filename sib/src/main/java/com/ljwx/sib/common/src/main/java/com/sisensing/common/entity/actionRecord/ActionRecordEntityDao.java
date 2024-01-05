package com.sisensing.common.entity.actionRecord;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.entity.actionRecord
 * Author: f.deng
 * CreateDate: 2021/3/23 18:20
 * Description:
 */
@Dao
public interface ActionRecordEntityDao {

    @Insert(onConflict = REPLACE)
    Completable insert(ActionRecordEntity actionRecordEntity);

    @Delete
    Completable delete(ActionRecordEntity actionRecordEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable update(ActionRecordEntity actionRecordEntity);


    @Query("select * from ActionRecordEntity where userId like :userId and startTime between :startTimeMill and :endTimeMil")
    Single<List<ActionRecordEntity>> getActionRecord(String userId, long startTimeMill, long endTimeMil);

    @Query("select * from ActionRecordEntity where userId like :userId and startTime between :startTimeMill and :endTimeMil order by startTime desc")
    Single<List<ActionRecordEntity>> getActionRecordSort(String userId, long startTimeMill, long endTimeMil);

    /**
     * 根据类型查询对应的行为事件
     * @param userId
     * @param startTimeMill
     * @param endTimeMil
     * @param tp 事件类型
     * @return
     */
    @Query("select * from ActionRecordEntity where userId like :userId and startTime between :startTimeMill and :endTimeMil and type like :tp")
    Single<List<ActionRecordEntity>> getActionRecord(String userId, long startTimeMill, long endTimeMil,int tp);

    /**
     * 获取 uploadService = -1的打卡事件
     * @param userId
     * @return
     */
    @Query("select * from ActionRecordEntity where userId like :userId and uploadService = -1")
    Single<List<ActionRecordEntity>> getActionRecordListByUs(String userId);

    @Query("select * from ActionRecordEntity where userId like :userId and uploadService < 1")
    Single<List<ActionRecordEntity>> getActionRecordListByUs2(String userId);

    /**
     * 根据用户id删除所有打卡数据
     * @param userid
     */
    @Query("delete from ActionRecordEntity where userId = :userid")
    void deleteAllClockInData(String userid);

    @Query("select * from ActionRecordEntity where userId like :userId and dataId = :dataId limit 1")
    Single<List<ActionRecordEntity>> findDataByDataId(String userId, String dataId);

}
