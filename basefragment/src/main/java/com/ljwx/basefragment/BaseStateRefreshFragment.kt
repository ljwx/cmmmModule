package com.ljwx.basefragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.ljwx.baseapp.R
import com.ljwx.baseapp.BasePopupLoading
import com.ljwx.baseapp.view.IViewRefreshLayout
import com.ljwx.baseapp.view.IViewStateLayout
import com.ljwx.baseapp.constant.LayoutStatus
import com.ljwx.baseapp.extensions.isMainThread
import com.ljwx.baseapp.page.IPagePopLoading
import com.ljwx.baseapp.page.IPageRefreshLayout
import com.ljwx.baseapp.page.IPageStateLayout

abstract class BaseStateRefreshFragment(@LayoutRes layoutResID: Int) :
    BaseFragment(layoutResID), IPagePopLoading, IPageStateLayout, IPageRefreshLayout {


    private val mPopupLoading by lazy {
        BasePopupLoading(requireContext())
    }

    /**
     * 多状态
     */
    private var mStateLayout: IViewStateLayout? = null

    /**
     * 安全线程切换
     */
    private var mStateRunnable: Runnable? = null

    /**
     * 下拉刷新
     */
    private var mRefreshLayout: IViewRefreshLayout? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useCommonStateLayout()
        useCommonRefreshLayout()

    }

    override fun showPopLoading(
        show: Boolean,
        cancelable: Boolean,
        transparent: Boolean,
        level: Int
    ) {
        if (!show || (isPopupLoadingShowing())) {
            return
        }
        activity?.runOnUiThread {
//            mPopupLoading.setCancelable(cancelable)
//            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
            mPopupLoading.showPopup(show, cancelable, backgroundTransparent = transparent)
        }
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
        if (stateLayout != null && stateLayout is IViewStateLayout) {
            initStateLayout(stateLayout)
        }
    }

    /**
     * 快速刷新布局
     */
    open fun useCommonRefreshLayout() {
        val refreshLayout = view?.findViewById<View>(R.id.base_app_quick_refresh_layout)
        if (refreshLayout != null && refreshLayout is IViewRefreshLayout) {
            initRefreshLayout(refreshLayout)
        }
    }

    /*================================================================*/

    /**
     * 初始化多状态
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
     * 设置布局样式
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
     * 显示布局状态
     *
     * @param state 哪种状态
     * @param show 是否需要显示
     * @param tag 携带数据
     */
    override fun showStateLayout(state: Int, show: Boolean, tag: Any?) {
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
        refreshLayout?.setOnRefreshListener(object : IViewRefreshLayout.RefreshListener {
            override fun onRefresh(refreshLayout: IViewRefreshLayout) {
                onRefreshData()
            }
        })
    }

    /**
     * 下拉刷新
     */
    override fun onRefreshData() {

    }

    /**
     * 刷新结束
     */
    override fun pullRefreshFinish() {
        requireActivity().runOnUiThread {
            mRefreshLayout?.refreshFinish()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mPopupLoading.dismiss()
        mStateLayout = null
        mStateRunnable = null
        mRefreshLayout = null
    }

}