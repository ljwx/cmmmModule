package com.ljwx.baseapp.user


interface IBaseUserInfo {

    fun isLoggedIn(): Boolean

    fun setInfoChangeType(type: Int? = 0)

    fun getInfoChangeType(): Int

}