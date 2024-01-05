package com.sisensing.common.listener;

import com.sisensing.common.entity.actionRecord.ActionRecordEntity;

/**
 * @author y.xie
 * @date 2021/6/30 16:59
 * @desc
 */
public interface WearClockInListener {
    void clockInSuccess(ActionRecordEntity entity);
}
