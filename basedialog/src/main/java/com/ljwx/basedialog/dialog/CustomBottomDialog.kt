package com.ljwx.basedialog.dialog

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager

open class CustomBottomDialog : CustomDialog {

    constructor(context: Context) : this(context, 0, true)

    constructor(context: Context?, themeResId: Int, isFillHorizontal: Boolean)
            : super(context, themeResId) {

        setAnimation(translate(1f, 0f), translate(0f, 1f))
        window?.setGravity(Gravity.BOTTOM or if (isFillHorizontal) Gravity.FILL_HORIZONTAL else Gravity.CENTER_HORIZONTAL)
        window?.decorView?.layoutParams = WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        setCanceledOnTouchOutside(true)

    }
}