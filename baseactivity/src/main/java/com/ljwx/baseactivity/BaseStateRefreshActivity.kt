package com.ljwx.baseactivity

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.ljwx.baseapp.BasePopupLoading
import com.ljwx.baseapp.R
import com.ljwx.baseapp.view.IViewRefreshLayout
import com.ljwx.baseapp.view.IViewStateLayout
import com.ljwx.baseapp.constant.BaseLayoutStatus
import com.ljwx.baseapp.extensions.isMainThread
import com.ljwx.baseapp.page.IPagePopLoading
import com.ljwx.baseapp.page.IPageRefreshLayout
import com.ljwx.baseapp.page.IPageStateLayout

open class BaseStateRefreshActivity : BaseActivity(), IPagePopLoading, IPageStateLayout,
    IPageRefreshLayout {

    private val mPopupLoading by lazy {
        BasePopupLoading(this)
    }

    /**
     * 多状态控件
     */
    private var mStateLayout: IViewStateLayout? = null

    /**
     * 子线程切换多状态布局
     */
    private var mStateRunnable: Runnable? = null

    /**
     * 下拉刷新控件
     */
    private var mRefreshLayout: IViewRefreshLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun showPopLoading(
        show: Boolean,
        message: CharSequence?,
        cancelable: Boolean,
        transparent: Boolean,
        level: Int
    ) {
        runOnUiThread {
//            mPopupLoading.setCancelable(cancelable)
//            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
            mPopupLoading.showPopup(show, cancelable, message, backgroundTransparent = transparent)
        }
    }

    override fun dismissPopLoading(dismiss: Boolean) {
        mPopupLoading.dismiss()
    }

    override fun isPopupLoadingShowing(): Boolean = mPopupLoading.isShowing()

    override fun setPopupLoadingLayout(@LayoutRes layout: Int) {
        mPopupLoading.setLayout(layout)
    }

    /*================================================================*/

    /**
     * 快速使用状态布局
     */
    open fun useCommonStateLayout() {
        val stateLayout = findViewById<View>(R.id.base_app_quick_state_layout)
        if (stateLayout != null && stateLayout is IViewStateLayout) {
            initStateLayout(stateLayout)
        }
    }

    /**
     * 快速使用刷新布局
     */
    open fun useCommonRefreshLayout() {
        var refreshLayout = findViewById<View>(R.id.base_app_quick_refresh_layout)
        if (refreshLayout == null) {
            refreshLayout = findViewById(R.id.base_app_page_refresh_layout)
        }
        if (refreshLayout != null && refreshLayout is IViewRefreshLayout) {
            initRefreshLayout(refreshLayout)
        }
    }

    /*================================================================*/

    /**
     * 初始化多状态布局
     *
     * @param stateLayout 多状态布局容器
     */
    override fun initStateLayout(stateLayout: IViewStateLayout?) {
        this.mStateLayout = stateLayout
    }

    override fun setStateLayoutClick(
        id: Int,
        listener: View.OnClickListener,
        @BaseLayoutStatus.LayoutStatus vararg stateLayout: Int
    ) {
        this.mStateLayout?.setClickListener(id, listener)
//        if (retryId != null) {
//            stateLayout?.setRetryIds(retryId)
//        }
//        // 重试按钮触发
//        stateLayout?.onRefresh {
//            onStateLayoutRetry(this.tag)
//        }
    }

    /**
     * 重新设置布局样式
     *
     * @param state 哪种状态
     * @param layout 对应的布局
     */
    fun setStateLayoutRes(@BaseLayoutStatus.LayoutStatus state: Int, @LayoutRes layout: Int) {
        when (state) {
            BaseLayoutStatus.LOADING -> {
                mStateLayout?.setLayoutLoading(layout)
//                mStateLayout?.emptyLayout = layout
            }

            BaseLayoutStatus.EMPTY -> {
                mStateLayout?.setLayoutEmpty(layout)
//                mStateLayout?.emptyLayout = layout
            }

            BaseLayoutStatus.ERROR -> {
                mStateLayout?.setLayoutError(layout)
//                mStateLayout?.errorLayout = layout
            }

            BaseLayoutStatus.OFFLINE -> {

            }

            else -> {}
        }
    }

    /**
     * 显示多种状态
     *
     * @param state 哪种状态
     * @param show 是否需要显示
     * @param tag 携带数据
     */
    override fun showStateLayout(state: Int, show: Boolean, tag: Any?) {
        if (!show || isFinishing) {
            return
        }
        if (isMainThread) {
            showState(state, tag)
        } else {
            mStateRunnable = mStateRunnable ?: Runnable {
                showState(state, tag)
            }
            runOnUiThread(mStateRunnable)
        }
    }

    private fun showState(@BaseLayoutStatus.LayoutStatus state: Int, tag: Any?) {
        when (state) {
            BaseLayoutStatus.LOADING -> {
                mStateLayout?.showLoading(tag)
            }

            BaseLayoutStatus.CONTENT -> {
                mStateLayout?.showContent()
            }

            BaseLayoutStatus.EMPTY -> {
                mStateLayout?.showEmpty()
            }

            BaseLayoutStatus.ERROR -> {
                mStateLayout?.showError(tag)
            }

            BaseLayoutStatus.OFFLINE -> {

            }
        }
    }

    /**
     * 重试回调
     *
     * @param tag 携带数据
     */
    override fun onStateLayoutRetry(tag: Any?) {

    }

    /*================================================================*/

    override fun enableRefresh(): Boolean = true

    override fun initRefreshLayout(refreshLayout: IViewRefreshLayout?) {
        if (enableRefresh()) {
            refreshLayout?.enableRefresh(true)
            this.mRefreshLayout = refreshLayout
            refreshLayout?.setRefreshPage(this)
        } else {
            refreshLayout?.enableRefresh(false)
        }
    }

    override fun initRefreshLayout(refreshId: Int) {
        this.mRefreshLayout = findViewById<View>(refreshId) as? IViewRefreshLayout
        if (enableRefresh()) {
            this.mRefreshLayout?.enableRefresh(true)
            this.mRefreshLayout?.setRefreshPage(this)
        } else {
            this.mRefreshLayout?.enableRefresh(false)
        }
    }

    /**
     * 下拉刷新逻辑
     */
    override fun onRefreshData(manual: Boolean) {

    }

    /**
     * 刷新结束
     */
    override fun pullRefreshFinish() {
        runOnUiThread {
            mRefreshLayout?.refreshFinish()
        }
    }


    override fun onDestroy() {
        mPopupLoading.dismiss()
        mStateLayout = null
        mStateRunnable = null
        mRefreshLayout = null
        super.onDestroy()
    }

}