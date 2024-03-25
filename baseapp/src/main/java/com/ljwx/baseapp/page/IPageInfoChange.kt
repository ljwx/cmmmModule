package com.ljwx.baseapp.page

interface IPageInfoChange {

    fun <T> userInfoChange(data: T, type: Int = 0)

}