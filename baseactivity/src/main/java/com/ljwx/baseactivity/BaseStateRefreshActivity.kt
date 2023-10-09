package com.ljwx.baseactivity

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.ljwx.baseapp.BasePopupLoading
import com.ljwx.baseapp.R
import com.ljwx.baseapp.view.IViewRefreshLayout
import com.ljwx.baseapp.view.IViewStateLayout
import com.ljwx.baseapp.constant.LayoutStatus
import com.ljwx.baseapp.extensions.isMainThread
import com.ljwx.baseapp.page.IPagePopLoading
import com.ljwx.baseapp.page.IPageRefreshLayout
import com.ljwx.baseapp.page.IPageStateLayout

open class BaseStateRefreshActivity : BaseActivity(), IPagePopLoading, IPageStateLayout,
    IPageRefreshLayout {

    private val mPopupLoading by lazy {
        BasePopupLoading(this)
    }

    private var mLoadingRunnable: Runnable? = null

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

    /**
     * 子线程执行刷新
     */
    private var mRefreshRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun showPopLoading(show: Boolean, cancelable: Boolean, transparent:Boolean, level: Int) {
        mLoadingRunnable = mLoadingRunnable ?: Runnable {
//            mPopupLoading.setCancelable(cancelable)
//            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
            mPopupLoading.showPopup(show, cancelable, backgroundTransparent = transparent)
        }
        runOnUiThread(mLoadingRunnable)
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
        val refreshLayout = findViewById<View>(R.id.base_app_quick_refresh_layout)
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
        @LayoutStatus.LayoutStatus vararg stateLayout: Int
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
    fun setStateLayoutRes(@LayoutStatus.LayoutStatus state: Int, @LayoutRes layout: Int) {
        when (state) {
            LayoutStatus.LOADING -> {
                mStateLayout?.setLayoutLoading(layout)
//                mStateLayout?.emptyLayout = layout
            }

            LayoutStatus.EMPTY -> {
                mStateLayout?.setLayoutEmpty(layout)
//                mStateLayout?.emptyLayout = layout
            }

            LayoutStatus.ERROR -> {
                mStateLayout?.setLayoutError(layout)
//                mStateLayout?.errorLayout = layout
            }

            LayoutStatus.OFFLINE -> {

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

    private fun showState(@LayoutStatus.LayoutStatus state: Int, tag: Any?) {
        when (state) {
            LayoutStatus.LOADING -> {
                mStateLayout?.showLoading(tag)
            }

            LayoutStatus.CONTENT -> {
                mStateLayout?.showContent()
            }

            LayoutStatus.EMPTY -> {
                mStateLayout?.showEmpty()
            }

            LayoutStatus.ERROR -> {
                mStateLayout?.showError(tag)
            }

            LayoutStatus.OFFLINE -> {

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

    override fun initRefreshLayout(refreshLayout: IViewRefreshLayout?) {
        this.mRefreshLayout = refreshLayout
        // 下拉刷新触发
        refreshLayout?.setOnRefreshListener(object :IViewRefreshLayout.RefreshListener{
            override fun onRefresh(refreshLayout: IViewRefreshLayout) {
                onPullRefresh()
            }
        })
    }

    /**
     * 下拉刷新逻辑
     */
    override fun onPullRefresh() {

    }

    /**
     * 刷新结束
     */
    override fun pullRefreshFinish() {
        if (isMainThread) {
            mRefreshLayout?.refreshFinish()
        } else {
            mRefreshRunnable = mRefreshRunnable ?: Runnable {
                mRefreshLayout?.refreshFinish()
            }
            runOnUiThread(mRefreshRunnable)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mPopupLoading.dismiss()
        mLoadingRunnable = null
        mStateLayout = null
        mStateRunnable = null
        mRefreshLayout = null
        mRefreshRunnable = null
    }

}