package com.ljwx.basefragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.drake.statelayout.StateLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.ljwx.baseapp.R
import com.ljwx.baseapp.isMainThread
import com.ljwx.baseapp.page.IBasePage
import com.ljwx.baseapp.LayoutStatus
import com.ljwx.baseapp.PopupLoading

open abstract class BaseStateRefreshFragment : BaseFragment(), IBasePage {


    private val mPopupLoading by lazy {
        PopupLoading(requireContext())
    }

    /**
     * 多状态控件
     */
    private var mStateLayout: StateLayout? = null

    /**
     * 子线程切换多状态布局
     */
    private var mStateRunnable: Runnable? = null

    /**
     * 下拉刷新控件
     */
    private var mRefreshLayout: SmartRefreshLayout? = null

    /**
     * 子线程执行刷新
     */
    private var mRefreshRunnable: Runnable? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useCommonStateLayout()
        useCommonRefreshLayout()

    }

    /**
     * 显示悬浮弹窗
     */
    override fun showPopupLoading(show: Boolean, type: Int) {
        mPopupLoading.showPopup(show)
    }

    override fun isPopupLoadingShowing(): Boolean = mPopupLoading.isShowing()

    /*================================================================*/

    /**
     * 快速使用状态布局
     */
    open fun useCommonStateLayout() {
        val stateLayout = view?.findViewById<View>(R.id.base_state_layout_container)
        if (stateLayout != null && stateLayout is StateLayout) {
            initStateLayout(stateLayout)
        }
    }

    /**
     * 快速使用刷新布局
     */
    open fun useCommonRefreshLayout() {
        val refreshLayout = view?.findViewById<View>(R.id.base_refresh_layout_container)
        if (refreshLayout != null && refreshLayout is SmartRefreshLayout) {
            initRefreshLayout(refreshLayout)
        }
    }

    /*================================================================*/

    /**
     * 初始化多状态布局
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
     * 重新设置布局样式
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
     * 显示多种状态
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
        // 下拉刷新触发
        refreshLayout?.setOnRefreshListener {
            onPullRefresh()
        }
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
            mRefreshLayout?.finishRefresh()
        } else {
            mRefreshRunnable = mRefreshRunnable ?: Runnable {
                mRefreshLayout?.finishRefresh()
            }
            requireActivity().runOnUiThread(mRefreshRunnable)
        }
    }


    override fun networkException(exception: Exception) {

    }

    override fun onDestroy() {
        super.onDestroy()
        mStateLayout = null
        mStateRunnable = null
        mRefreshLayout = null
        mRefreshRunnable = null
        mPopupLoading.dismiss()
    }

}