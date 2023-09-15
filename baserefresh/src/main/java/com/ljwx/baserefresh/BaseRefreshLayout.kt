package com.ljwx.baserefresh

import android.content.Context
import android.util.AttributeSet
import com.ljwx.baseapp.business.IBaseRefreshHeader
import com.ljwx.baseapp.business.IBaseRefreshLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshHeader

open class BaseRefreshLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SmartRefreshLayout(context, attrs), IBaseRefreshLayout {

    companion object {
        fun setDefaultRefreshHeaderCreator(creator: (context: Context, refreshLayout: IBaseRefreshLayout) -> IBaseRefreshHeader) {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                creator.invoke(context, layout as IBaseRefreshLayout) as RefreshHeader
            }
        }
    }

    override fun setRefreshHeader(header: IBaseRefreshHeader) {
        if (header is RefreshHeader) {
            super.setRefreshHeader(header)
        }
    }

    override fun setOnRefreshListener(refreshListener: IBaseRefreshLayout.RefreshListener) {
        super.setOnRefreshListener {
            refreshListener.onRefresh(this)
        }
    }

    override fun refreshFinish() {
        super.finishRefresh()
    }


}