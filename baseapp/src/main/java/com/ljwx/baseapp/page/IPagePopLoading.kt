package com.ljwx.baseapp.page

interface IPagePopLoading {

    /**
     * 显示加载悬浮弹窗
     */
    fun showPopLoading(show: Boolean = true, cancelable: Boolean = true, level: Int = 3)

    /**
     * 取消加载悬浮窗
     */
    fun dismissPopLoading(dismiss: Boolean = true)

    /**
     * 悬浮弹窗是否显示中
     */
    fun isPopupLoadingShowing(): Boolean

}