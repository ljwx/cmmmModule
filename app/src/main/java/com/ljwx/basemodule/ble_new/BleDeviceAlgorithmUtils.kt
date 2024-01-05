package com.sisensing.common.ble_new

import com.sisensing.common.algorithom.AlgorithmFactory
import com.sisensing.common.algorithom.IAlgorithm
import com.sisensing.common.ble.BleLog
import com.sisensing.common.entity.Device.DeviceEntity

object BleDeviceAlgorithmUtils {

    private var mAlgorithm: IAlgorithm? = null

    /**
     * 获取当前算法
     */
    @JvmStatic
    fun getCurrentAlgorithm(): IAlgorithm {
        if (mAlgorithm == null) {
            throw NullPointerException("血糖算法未创建")
        }
        return mAlgorithm!!
    }

    /**
     * 通过版本获取算法
     */
    @JvmStatic
    fun createAlgorithm(algorithmVersion: String): IAlgorithm? {
        mAlgorithm = AlgorithmFactory.createAlgorithm(algorithmVersion)
        return mAlgorithm
    }

    /**
     * 校验连接码
     */
    @JvmStatic
    fun verifyLinkCodePass(linkCode: String): Boolean {
        if (mAlgorithm?.verifyLinkCode(linkCode) == 1) {
            return true
        }
        BleLog.dInterrupt("算法校验连接码不通过");
        return false
    }

    /**
     * 初始化上下文
     */
    @JvmStatic
    fun initContext(deviceEntity: DeviceEntity) {
        mAlgorithm?.initAlgorithmContext(deviceEntity)
    }

}