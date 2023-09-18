package com.ljwx.baserefresh

import android.content.Context
import android.util.AttributeSet
import com.ljwx.baseapp.view.IViewRefreshHeader

class BaseRefreshHeader @JvmOverloads constructor(
    private val context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) {

    open fun getHeader(): IViewRefreshHeader {
        return SmartRefreshHeader(context, attrs)
    }

}