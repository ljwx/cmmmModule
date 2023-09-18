package com.ljwx.baserefresh

import android.content.Context
import android.util.AttributeSet
import com.ljwx.baseapp.view.IViewRefreshHeader
import com.ljwx.baseapp.view.IViewRefreshLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader

open class BaseRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SmartRefreshLayout(context, attrs), IViewRefreshLayout {

    companion object {
        fun setDefaultRefreshHeaderCreator(creator: (context: Context, refreshLayout: IViewRefreshLayout) -> IViewRefreshHeader) {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                creator.invoke(context, layout as IViewRefreshLayout) as RefreshHeader
            }
        }
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


}