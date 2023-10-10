package com.ljwx.baseapp.page

import android.app.Dialog
import android.view.View

interface IPageDialogTips {

    fun showDialogTips(
        title: String? = null,
        content: String? = null,
        positiveText: String? = null,
    ): Dialog?

    fun showDialogTips(
        title: String? = null,
        content: String? = null,
        positiveText: String? = null,
        negativeText: String? = null,
        showClose: Boolean? = null,
        tag: String? = null,
        reversalButtons: Boolean = false,
        negativeListener: View.OnClickListener? = null,
        positiveListener: View.OnClickListener? = null
    ): Dialog?

}