package com.ljwx.baseapp.page

import com.ljwx.baseapp.router.IPostcard

interface IPageStartPage {

    fun startActivity(clazz: Class<*>)

    fun routerTo(path: String): IPostcard

}