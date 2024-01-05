package com.sisensing.common.entity.clcok;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

/**
 * @author y.xie
 * @date 2021/5/19 16:31
 * @desc
 */
@Dao
public interface ClockEntityDao {
    //插入或替换
    @Insert(onConflict = REPLACE)
    Completable insert(ClockEntity entity);

    @Query("select * from clockentity where userId= :userId")
    Single<List<ClockEntity>> findAllClock(String userId);
}
