package com.ljwx.recyclerview.loadmore.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ljwx.recyclerview.BaseRecyclerLog
import com.ljwx.recyclerview.R
import com.ljwx.recyclerview.loadmore.LoadMoreAdapter
import com.ljwx.recyclerview.loadmore.LoadMoreStatus
import com.ljwx.recyclerview.quick.QuickLoadMoreAdapter
import com.ljwx.recyclerview.singleClick

class LoadMoreViewPresenter {

    private val TAG = this.javaClass.simpleName

    var loadMoreLoadingLayout: Int? = null
    var loadMoreErrorLayout: Pair<Int, Int?>? = null
    var loadMoreCompleteLayout: Int? = null

    fun showState(holderView: View, @LoadMoreStatus.LoadMoreStatus state: String) {
        BaseRecyclerLog.d(TAG, "showState:$state")
        if (state == LoadMoreStatus.STATE_HAS_MORE) {
            (holderView as? LoadMoreView)?.showStateLoading(loadMoreLoadingLayout)
        }
        if (state == LoadMoreStatus.STATE_COMPLETE) {
            (holderView as? LoadMoreView)?.showStateComplete(loadMoreCompleteLayout)
        }
        if (state == LoadMoreStatus.STATE_ERROR) {
            var retryId = (loadMoreErrorLayout?.second) ?: R.id.rv_load_more_retry
            (holderView as? LoadMoreView)?.showStateError(loadMoreErrorLayout?.first)?.apply {
                findViewById<View>(retryId)?.singleClick {
                    ((holderView.parent as? RecyclerView)?.adapter as? LoadMoreAdapter)?.startLoadMore()
                    ((holderView.parent as? RecyclerView)?.adapter as? QuickLoadMoreAdapter<*>)?.startLoadMore()
                }
            }
        }
    }

}