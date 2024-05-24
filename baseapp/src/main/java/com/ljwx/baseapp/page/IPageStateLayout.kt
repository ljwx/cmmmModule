package com.ljwx.baseapp.page

import android.view.View
import androidx.annotation.IdRes
import com.ljwx.baseapp.view.IViewStateLayout
import com.ljwx.baseapp.constant.BaseLayoutStatus

interface IPageStateLayout {
    /**
     * 初始化多状态布局
     */
    fun initStateLayout(stateLayout: IViewStateLayout?)

    /**
     * 显示多状态
     */
    fun showStateLayout(
        @BaseLayoutStatus.LayoutStatus state: Int,
        show: Boolean = true,
        view: View? = null,
        tag: Any? = null
    )

    /**
     * 设置点击事件
     */
    fun addStateLayoutClick(
        @BaseLayoutStatus.LayoutStatus stateLayout: Int,
        @IdRes id: Int,
        listener: View.OnClickListener
    )

}