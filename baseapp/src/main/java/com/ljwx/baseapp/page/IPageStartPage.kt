package com.ljwx.baseapp.page

import android.os.Bundle

interface IPageStartPage {

    fun startActivityRouter(path: String, bundle: Bundle)

    fun startActivity(clazz: Class<*>, bundle: Bundle)

    fun startActivityRouter(
        path: String,
        type: String? = null,
        id: String? = null,
        params: String? = null
    )

    fun startActivityRouter(
        path: String,
        type: Int? = null,
        id: String? = null,
        params: String? = null
    )

    fun getCommonBundleParams()

}