package com.ljwx.baseapp.infochange


interface IBaseUserInfo {

    fun isLoggedIn(): Boolean

    fun setInfoChangeType(type: Int? = 0)

    fun getInfoChangeType(): Int

}