package com.sisensing.common.entity.rxbusbean

import com.sisensing.common.entity.BsShareContentEntity
import com.sisensing.common.entity.RedDotBean

/**
 * @ClassName RxBusHomeBean
 * @Description
 * @Author xieyang
 * @Date 2023/5/26 14:47
 */
class RxBusHomeBean {
    var rxBusType: RxBusHomeType = RxBusHomeType.RED_DOT
    var redDotBean: RedDotBean? = null
    var bsShareContentEntity: BsShareContentEntity? = null
}