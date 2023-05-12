package com.ljwx.baseapp

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.ljwx.baseapp.R

class PopupLoading(val context: Context) {

    private val dialog by lazy {
        AlertDialog.Builder(context).setView(R.layout.baseapp_popup_loading_layout).create()
    }


    private var mLoadingRunnable: Runnable? = null

    /**
     * 显示loading弹窗
     *
     * @param show 是否显示
     * @param cancelable 是否可以取消
     * @param focusable 是否获取焦点
     * @param canceledOnTouchOutside 点击外部是否消失
     */
    fun showPopup(
        show: Boolean,
        cancelable: Boolean = true,
        focusable: Boolean = true,
        canceledOnTouchOutside: Boolean = false,
    ) {
        dialog.show()
    }


    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun dismiss() {
        dialog.ownerActivity?.runOnUiThread {
            dialog.dismiss()
        }
    }

}


