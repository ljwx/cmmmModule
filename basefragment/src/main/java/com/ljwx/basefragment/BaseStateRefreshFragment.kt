package com.ljwx.basefragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.ljwx.baseapp.R
import com.ljwx.baseapp.BasePopupLoading
import com.ljwx.baseapp.view.IViewRefreshLayout
import com.ljwx.baseapp.view.IViewStateLayout
import com.ljwx.baseapp.constant.BaseLayoutStatus
import com.ljwx.baseapp.extensions.isMainThread
import com.ljwx.baseapp.page.IPagePopLoading
import com.ljwx.baseapp.page.IPageRefreshLayout
import com.ljwx.baseapp.page.IPageStateLayout
import com.ljwx.baseapp.util.BaseModuleLog

abstract class BaseStateRefreshFragment(@LayoutRes layoutResID: Int = R.layout.baseapp_state_layout_empty) :
    BaseFragment(layoutResID), IPagePopLoading, IPageStateLayout, IPageRefreshLayout {


    private var mPopupLoading: BasePopupLoading? = null

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

    /**
     * 多状态的数据是否成功获取过 成功过
     */
    protected var stateLoadingDataSucceeded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        useCommonStateLayout()
        useCommonRefreshLayout()

    }

    override fun showPopLoading(
        show: Boolean,
        message: CharSequence?,
        cancelable: Boolean,
        transparent: Boolean,
        level: Int
    ) {
        if (!show || (isPopupLoadingShowing())) {
            return
        }
        activity?.runOnUiThread {
            mPopupLoading = mPopupLoading ?: BasePopupLoading(requireContext())
//            mPopupLoading.setCancelable(cancelable)
//            dialog.setCanceledOnTouchOutside(canceledOnTouchOutside)
            mPopupLoading?.showPopup(show, cancelable, message, backgroundTransparent = transparent)
        }
    }

    override fun dismissPopLoading(dismiss: Boolean) {
        activity?.runOnUiThread {
            mPopupLoading?.dismiss()
        }
    }

    override fun isPopupLoadingShowing(): Boolean = mPopupLoading?.isShowing() == true

    override fun setPopupLoadingLayout(@LayoutRes layout: Int) {
        mPopupLoading?.setLayout(layout)
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
        BaseModuleLog.d(TAG, "初始化stateLayout")
        this.mStateLayout = stateLayout
    }


    override fun addStateLayoutClick(
        @BaseLayoutStatus.LayoutStatus state: Int,
        id: Int,
        listener: View.OnClickListener,
    ) {
        mStateLayout?.addClickListener(state, id, listener)
    }

    /**
     * 显示布局状态
     *
     * @param state 哪种状态
     * @param show 是否需要显示
     * @param tag 携带数据
     */
    override fun showStateLayout(state: Int, show: Boolean, view: View?, tag: Any?) {
        if (!show || requireActivity().isFinishing) {
            return
        }
        if (isMainThread) {
            mStateLayout?.showStateView(state, view, tag)
        } else {
            mStateRunnable = mStateRunnable ?: Runnable {
                mStateLayout?.showStateView(state, view, tag)
            }
            requireActivity().runOnUiThread(mStateRunnable)
        }
    }

    open fun showStateContent() = showStateLayout(BaseLayoutStatus.CONTENT)
    open fun showStateEmpty() = showStateLayout(BaseLayoutStatus.EMPTY)
    open fun showStateLoading() = showStateLayout(BaseLayoutStatus.LOADING)
    open fun showStateError() = showStateLayout(BaseLayoutStatus.ERROR)
    open fun showStateOffline() = showStateLayout(BaseLayoutStatus.OFFLINE)
    open fun showStateExtend() = showStateLayout(BaseLayoutStatus.EXTEND)

    /*================================================================*/

    override fun enableRefresh(): Boolean = true

    override fun initRefreshLayout(refreshLayout: IViewRefreshLayout?) {
        if (enableRefresh()) {
            BaseModuleLog.d(TAG, "启用下拉刷新")
            refreshLayout?.enableRefresh(true)
            this.mRefreshLayout = refreshLayout
            refreshLayout?.setRefreshPage(this)
        } else {
            refreshLayout?.enableRefresh(false)
        }
    }

    override fun initRefreshLayout(refreshId: Int) {
        this.mRefreshLayout = view?.findViewById<View>(refreshId) as? IViewRefreshLayout
        if (enableRefresh()) {
            BaseModuleLog.d(TAG, "启用下拉刷新")
            this.mRefreshLayout?.enableRefresh(true)
            this.mRefreshLayout?.setRefreshPage(this)
        } else {
            this.mRefreshLayout?.enableRefresh(false)
        }
    }

    /**
     * 下拉刷新
     */
    override fun onRefreshData(type: Long) {
        onLoadData(type)
    }

    override fun onLoadData(type: Long) {

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
        mPopupLoading?.dismiss()
        mStateLayout = null
        mStateRunnable = null
        mRefreshLayout = null
        super.onDestroy()
    }

}