package com.sisensing.common.entity.rxbusbean

import com.sisensing.common.entity.guardianship.GuardianshipDeviceAndGlucoseEntity
import com.sisensing.common.entity.guardianship.RelativesAlarmDetailResponseEntity
import com.sisensing.common.entity.guardianship.RelativesFollowInfoEntity

/**
 * @ClassName RxBusGuardianshipHomeBean
 * @Description 监护模式首页的事件
 * @Author xieyang
 * @Date 2023/5/30 14:46
 */
class RxBusGuardianshipHomeBean (
    var rxBusGuardianshipHomeType:RxBusGuardianshipHomeType,
    var info:String?=null,
    var guardianshipDeviceAndGlucoseEntity: GuardianshipDeviceAndGlucoseEntity?=null,
    var relativesAlarmDetailResponseEntity: RelativesAlarmDetailResponseEntity? = null,
    var relativesFollowInfoEntity: RelativesFollowInfoEntity? = null
)
