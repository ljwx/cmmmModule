package com.ljwx.baseapp.infochange

abstract class BaseUserInfo : IBaseUserInfo {

    private var changeType: Int? = null

    override fun setInfoChangeType(type: Int?) {
        changeType = type
    }

    override fun getInfoChangeType(): Int {
        return changeType ?: 0
    }

}