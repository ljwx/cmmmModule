package com.ljwx.baseapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ThreadUtils

class BasePopupLoading(private val context: Context) {

    companion object {

        private var loadingLayout = R.layout.baseapp_popup_loading_layout
        fun setLayout(@LayoutRes layout: Int) {
            loadingLayout = layout
        }
    }

    private var mLoadingLayout: Int? = null

    private val dialog by lazy {
        val view = LayoutInflater.from(context).inflate(mLoadingLayout ?: loadingLayout, null)
        val dialog = AlertDialog.Builder(context, R.style.dialogNoBg).setView(view).create()
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog
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
        backgroundTransparent: Boolean = false
    ) {
        if (!show) {
            return
        }
        dialog.setCancelable(cancelable)
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
        dialog.show()
        val lp = dialog.window?.attributes
        lp?.width = 300
        lp?.height = 300
        lp?.dimAmount = if (backgroundTransparent) 0f else 0.4f
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes = lp
    }


    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    fun dismiss() {
        if (!dialog.isShowing) {
            return
        }
        ThreadUtils.runOnUiThread {
            dialog.dismiss()
        }
    }

}


