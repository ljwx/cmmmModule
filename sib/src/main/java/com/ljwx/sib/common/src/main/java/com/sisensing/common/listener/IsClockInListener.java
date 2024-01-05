package com.sisensing.common.listener;

import com.sisensing.common.entity.actionRecord.ActionRecordEntity;

import java.util.List;

/**
 * @author y.xie
 * @date 2021/6/15 11:16
 * @desc 是否打卡监听
 */
public interface IsClockInListener {
    void isClockIn(boolean isClockIn, List<ActionRecordEntity> entities);
}
