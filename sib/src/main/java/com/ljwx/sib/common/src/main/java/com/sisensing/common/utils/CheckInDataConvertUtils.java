package com.sisensing.common.utils;

import com.sisensing.common.entity.actionRecord.ActionRecordEntity;
import com.sisensing.common.entity.personalcenter.LifeEventEntity;

public class CheckInDataConvertUtils {

    public static LifeEventEntity.RecordsDTO db2ServerData(ActionRecordEntity entity) {
        LifeEventEntity.RecordsDTO dto = new LifeEventEntity.RecordsDTO();
        dto.setType(entity.getType());
        LifeEventEntity.RecordsDTO.ActionDataDTO a = new LifeEventEntity.RecordsDTO.ActionDataDTO();
        a.setEventDetail(entity.getEventDetail());
        a.setEventConsume(entity.getEventConsume());
        a.setEventType(entity.getEventType());
        a.setStartTime(entity.getStartTime());
        a.setEndTime(entity.getEndTime());
        a.setUserId(entity.getUserId());
        a.setUnit(entity.getEventConsume());
        dto.setActionData(a);
        dto.setActionStartTime(entity.getStartTime());
        dto.setActionEndTime(entity.getEndTime());
        dto.setDataId(entity.getDataId());
        dto.setId(entity.getId().toString());
        return dto;
    }

}
