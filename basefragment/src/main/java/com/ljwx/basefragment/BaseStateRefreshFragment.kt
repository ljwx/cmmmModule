package com.ljwx.basefragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.drake.statelayout.StateLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ljwx.baseapp.R
import com.ljwx.baseapp.LayoutStatus
import com.ljwx.baseapp.BasePopupLoading
import com.ljwx.baseapp.extensions.isMainThread
import com.ljwx.baseapp.page.IPagePopLoading
import com.ljwx.baseapp.page.IPageRefreshLayout
import com.ljwx.baseapp.page.IPageStateLayout

open abstract class BaseStateRefreshFragment(@LayoutRes layoutResID: Int) :
    BaseFragment(layoutResID), IPagePopLoading, IPageStateLayout, IPageRefreshLayout {


    private val mPopupLoading by lazy {
        BasePopupLoading(mActivity)
    }

    private var mLoadingRunnable: Runnable? = null

    /**
     * 多状态
     */
    private var mStateLayout: StateLayout? = null

    /**
     * 安全线程切换
     */
    private var mStateRunnable: Runnable? = null

    /**
     * 下拉刷新
     */
    private var mRefreshLayout: SmartRefreshLayout? = null

    /**
     * 安全线程切换
     */
    private var mRefreshRunnable: Runnable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useCommonStateLayout()
        useCommonRefreshLayout()

    }

    override fun showPopLoading(show: Boolean, cancelable: Boolean, level: Int) {
        if (!show || (isPopupLoadingShowing())) {
            return
        }
        mLoadingRunnable = mLoadingRunnable ?: Runnable {
//            mPopupLoading.setCancelable(cancelable)
//            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
            mPopupLoading.showPopup(show)
        }
        activity?.runOnUiThread(mLoadingRunnable)
    }

    override fun dismissPopLoading(dismiss: Boolean) {
        activity?.runOnUiThread {
            mPopupLoading.dismiss()
        }
    }

    override fun isPopupLoadingShowing(): Boolean = mPopupLoading.isShowing()

    override fun setPopupLoadingLayout(@LayoutRes layout: Int) {
        mPopupLoading.setLayout(layout)
    }

    /*================================================================*/

    /**
     * 快速状态布局
     */
    open fun useCommonStateLayout() {
        val stateLayout = view?.findViewById<View>(R.id.base_app_quick_state_layout)
        if (stateLayout != null && stateLayout is StateLayout) {
            initStateLayout(stateLayout)
        }
    }

    /**
     * 快速刷新布局
     */
    open fun useCommonRefreshLayout() {
        val refreshLayout = view?.findViewById<View>(R.id.base_app_quick_refresh_layout)
        if (refreshLayout != null && refreshLayout is SmartRefreshLayout) {
            initRefreshLayout(refreshLayout)
        }
    }

    /*================================================================*/

    /**
     * 初始化多状态
     *
     * @param stateLayout 多状态布局容器
     * @param retryId 重试id
     */
    override fun initStateLayout(stateLayout: StateLayout?, retryId: Int?) {
        this.mStateLayout = stateLayout
        if (retryId != null) {
            stateLayout?.setRetryIds(retryId)
        }
        // 重试按钮触发
        stateLayout?.onRefresh {
            onStateLayoutRetry(this.tag)
        }
    }

    /**
     * 设置布局样式
     *
     * @param state 哪种状态
     * @param layout 对应的布局
     */
    fun setStateLayoutRes(state: LayoutStatus, @LayoutRes layout: Int) {
        when (state) {
            LayoutStatus.LOADING -> {
                mStateLayout?.emptyLayout = layout
            }

            LayoutStatus.EMPTY -> {
                mStateLayout?.emptyLayout = layout
            }

            LayoutStatus.ERROR -> {
                mStateLayout?.errorLayout = layout
            }

            LayoutStatus.OFFLINE -> {

            }

            else -> {}
        }
    }

    /**
     * 显示布局状态
     *
     * @param state 哪种状态
     * @param show 是否需要显示
     * @param tag 携带数据
     */
    override fun showStateLayout(state: LayoutStatus, show: Boolean, tag: Any?) {
        if (!show || requireActivity().isFinishing) {
            return
        }
        if (isMainThread) {
            showState(state, tag)
        } else {
            mStateRunnable = mStateRunnable ?: Runnable {
                showState(state, tag)
            }
            requireActivity().runOnUiThread(mStateRunnable)
        }
    }

    private fun showState(state: LayoutStatus, tag: Any?) {
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

    override fun initRefreshLayout(refreshLayout: SmartRefreshLayout?) {
        this.mRefreshLayout = refreshLayout
        refreshLayout?.setOnRefreshListener {
            onPullRefresh()
        }
    }

    /**
     * 下拉刷新
     */
    override fun onPullRefresh() {

    }

    /**
     * 刷新结束
     */
    override fun pullRefreshFinish() {
        if (isMainThread) {
            mRefreshLayout?.finishRefresh()
        } else {
            mRefreshRunnable = mRefreshRunnable ?: Runnable {
                mRefreshLayout?.finishRefresh()
            }
            requireActivity().runOnUiThread(mRefreshRunnable)
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