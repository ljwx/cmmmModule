package com.ljwx.baseapp.page

import com.ljwx.baseapp.infochange.IBaseConfigInfo
import com.ljwx.baseapp.infochange.IBaseUserInfo

interface IPageInfoChange {

    fun userInfoChange(data: IBaseUserInfo?, type: Int = 0)

    fun configInfoChange(data: IBaseConfigInfo?, type: Int = 0)

}