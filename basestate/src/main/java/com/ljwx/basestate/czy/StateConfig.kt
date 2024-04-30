package com.ljwx.basestate.czy

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes


/**
 * 全局的缺省页布局[StateLayout]配置
 */
object StateConfig {

    internal var retryIds: IntArray? = null
//    internal var onEmpty: (View.(Any?) -> Unit)? = null
//    internal var onError: (View.(Any?) -> Unit)? = null
//    internal var onLoading: (View.(Any?) -> Unit)? = null
//    internal var onContent: (View.(Any?) -> Unit)? = null

    /** 错误页布局的layoutRes, 如果[StateLayout.errorLayout]设置则该属性无效 */
    @LayoutRes
    @JvmStatic
    var errorLayout = View.NO_ID

    /** 空页布局的layoutRes, 如果[StateLayout.emptyLayout]设置则该属性无效 */
    @LayoutRes
    @JvmStatic
    var emptyLayout = View.NO_ID

    /** 加载页布局的layoutRes, 如果[StateLayout.loadingLayout]设置则该属性无效 */
    @LayoutRes
    @JvmStatic
    var loadingLayout = View.NO_ID

    /** 加载页布局的layoutRes, 如果[StateLayout.loadingLayout]设置则该属性无效 */
    @LayoutRes
    @JvmStatic
    var offlineLayout = View.NO_ID

    /** 加载页布局的layoutRes, 如果[StateLayout.loadingLayout]设置则该属性无效 */
    @LayoutRes
    @JvmStatic
    var extendLayout = View.NO_ID

    /** 处理缺省页状态变更 */
//    @JvmStatic
//    var stateChangedHandler: StateChangedHandler = StateChangedHandler

    /** 防抖动点击事件的间隔时间, 单位毫秒 */
    @JvmStatic
    var clickThrottle: Long = 500


    /**
     * 会为所有[StateLayout.emptyLayout]/[StateLayout.errorLayout]中的指定Id的视图对象添加一个点击事件
     * 该点击事件会触发[StateLayout.showLoading], 同时500ms内防抖动
     */
    @JvmStatic
    fun setRetryIds(@IdRes vararg ids: Int) {
        retryIds = ids
    }
}