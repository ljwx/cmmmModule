package com.ljwx.baseapp.user

open abstract class BaseUserInfo : IBaseUserInfo {

    private var infoChangeType: Int? = 0

    override fun setInfoChangeType(type: Int?) {
        infoChangeType = type
    }

    override fun getInfoChangeType(): Int {
        return infoChangeType ?: -1000
    }

}