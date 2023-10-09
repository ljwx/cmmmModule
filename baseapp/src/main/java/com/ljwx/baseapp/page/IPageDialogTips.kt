package com.ljwx.baseapp.page

import android.view.View

interface IPageDialogTips {

    fun showDialogTips(
        title: String? = null,
        content: String? = null,
        positiveText: String? = null,
    )

    fun showDialogTips(
        title: String? = null,
        content: String? = null,
        positiveText: String? = null,
        negativeText: String? = null,
        showClose: Boolean? = null,
        tag: String? = null,
        negativeListener: View.OnClickListener? = null,
        positiveListener: View.OnClickListener? = null
    )

}