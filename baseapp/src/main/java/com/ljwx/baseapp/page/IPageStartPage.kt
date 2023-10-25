package com.ljwx.baseapp.page

import com.ljwx.baseapp.router.IPostcard

interface IPageStartPage {

    fun startActivity(clazz: Class<*>, requestCode: Int? = null)

    fun routerTo(path: String): IPostcard

}