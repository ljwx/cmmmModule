package com.sisensing.common.entity.actionRecord;

import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.database.RoomTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.entity.actionRecord
 * Author: f.deng
 * CreateDate: 2021/3/23 18:24
 * Description:
 */
public class ActionRecordRepository {


    private ActionRecordEntityDao mActionRecordEntityDao;

    private ActionRecordRepository() {
        mActionRecordEntityDao = AppDatabase.getInstance().getActionRecordEntityDao();
    }

    private static class Inner {
        private static ActionRecordRepository INSTANCE = new ActionRecordRepository();
    }

    public static ActionRecordRepository getInstance() {
        return Inner.INSTANCE;
    }

    public void insert(ActionRecordEntity actionRecordEntity) {
        RoomTask.CompletableTask(mActionRecordEntityDao.insert(actionRecordEntity));
    }

    public void update(ActionRecordEntity actionRecordEntity) {
        RoomTask.CompletableTask(mActionRecordEntityDao.update(actionRecordEntity));
    }

    public void delete(ActionRecordEntity actionRecordEntity) {
        RoomTask.CompletableTask(mActionRecordEntityDao.delete(actionRecordEntity));
    }
}
