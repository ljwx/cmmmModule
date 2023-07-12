package com.ljwx.baseapp

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog

class BasePopupLoading(val context: Context) {

    companion object {

        private var loadingLayout = R.layout.baseapp_popup_loading_layout
        fun setLayout(@LayoutRes layout: Int) {
            loadingLayout = layout
        }
    }

    private var mLoadingLayout: Int? = null

    private val dialog by lazy {
        AlertDialog.Builder(context).setView(mLoadingLayout ?: loadingLayout).create()
    }

    fun setLayout(@LayoutRes layoutRes: Int) {
        mLoadingLayout = layoutRes
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


