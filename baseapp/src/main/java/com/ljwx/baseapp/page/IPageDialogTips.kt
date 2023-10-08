package com.ljwx.baseapp.page

import android.view.View

interface IPageDialogTips {

    fun showDialogTips(
        title: String? = null,
        content: String? = null,
        tag: String? = null,
        positiveText: String? = null,
        negativeText: String? = null,
        showClose: Boolean? = null,
        positiveListener: View.OnClickListener? = null
    )

}