package com.ljwx.recyclerview.loadmore.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ljwx.recyclerview.R
import com.ljwx.recyclerview.loadmore.LoadMoreAdapter
import com.ljwx.recyclerview.singleClick

class LoadMoreViewPresenter {

    var loadMoreError: Pair<Int, Int>? = null
    var loadMoreCompleteLayout: Int? = null

    fun showState(holderView: View, state: Int) {
        if (state == LoadMoreAdapter.STATE_HAS_MORE) {
            (holderView as LoadMoreView)?.showStateLoading(null)
        }
        if (state == LoadMoreAdapter.STATE_COMPLETE) {
            (holderView as LoadMoreView)?.showStateComplete(loadMoreCompleteLayout)
        }
        if (state == LoadMoreAdapter.STATE_ERROR) {
            var retryId = (loadMoreError?.second) ?: R.id.rv_load_more_retry
            (holderView as LoadMoreView)?.showStateError(loadMoreError?.first)?.apply {
                findViewById<View>(retryId).singleClick {
                    ((holderView.parent as? RecyclerView)?.adapter as? LoadMoreAdapter)?.startLoadMore()
                }
            }
        }
    }

}