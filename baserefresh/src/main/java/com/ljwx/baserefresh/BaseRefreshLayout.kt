package com.ljwx.baserefresh

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.ljwx.baseapp.page.IPageRefreshLayout
import com.ljwx.baseapp.view.IViewRefreshHeader
import com.ljwx.baseapp.view.IViewRefreshLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader

open class BaseRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) {

    private val refreshLayout: SpecialRefreshLayout

    init {
        refreshLayout = SpecialRefreshLayout(context, attrs)
    }

    companion object {
        fun setDefaultRefreshHeaderCreator(creator: (context: Context, refreshLayout: IViewRefreshLayout) -> IViewRefreshHeader) {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                creator.invoke(context, layout as IViewRefreshLayout) as RefreshHeader
            }
        }
    }

    open fun getView(): ViewGroup {
        return refreshLayout.getView()
    }

    open fun onFinishInflate() {
        refreshLayout.onFinishInflate()
    }


    private inner class SpecialRefreshLayout : SmartRefreshLayout, IViewRefreshLayout {

        constructor(context: Context) : this(context, null)

        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

        private var refreshPage: IPageRefreshLayout? = null

        init {
            super.setOnRefreshListener {
                refreshPage?.onRefreshData()
            }
        }

        public override fun onFinishInflate() {
            super.onFinishInflate()
        }

        override fun enableRefresh(enable: Boolean) {
            super.setEnableRefresh(enable)
        }

        override fun setRefreshPage(refreshPage: IPageRefreshLayout) {
            this.refreshPage = refreshPage
        }

        override fun setRefreshHeader(header: IViewRefreshHeader) {
            if (header is RefreshHeader) {
                super.setRefreshHeader(header)
            }
        }

        override fun setOnRefreshListener(refreshListener: IViewRefreshLayout.RefreshListener) {
            super.setOnRefreshListener {
                refreshListener.onRefresh(this)
            }
        }

        override fun refreshFinish() {
            super.finishRefresh()
        }

        override fun getView(): ViewGroup {
            return this
        }
    }

}