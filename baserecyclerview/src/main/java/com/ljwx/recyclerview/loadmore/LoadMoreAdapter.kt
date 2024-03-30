package com.ljwx.recyclerview.loadmore

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*
import com.ljwx.recyclerview.loadmore.view.LoadMoreView
import com.ljwx.recyclerview.loadmore.view.LoadMoreViewPresenter
import com.ljwx.recyclerview.adapter.MultipleTypeAdapter
import com.ljwx.recyclerview.diff.ItemDiffCallback
import com.ljwx.recyclerview.holder.ItemHolder
import com.ljwx.recyclerview.itemtype.ItemType
import com.ljwx.recyclerview.itemtype.ItemTypeViewClass
import com.ljwx.recyclerview.loadmore.view.LoadMoreItem

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class LoadMoreAdapter(
    vararg itemTypes: ItemType<*, *>,
    config: AsyncDifferConfig<Any> = AsyncDifferConfig.Builder(ItemDiffCallback()).build(),
) : MultipleTypeAdapter(*itemTypes, config = config) {

    private val mLoadMoreItem = LoadMoreItem()
    private val mLoadMoreItemType =
        ItemTypeViewClass(LoadMoreItem::class.java, LoadMoreView::class.java) { holder, item ->
            mLoadMorePresenter.showState(holder.itemView, item.state)
        }
    private val mLoadMoreTrigger = LoadMoreTrigger()
    private val mLoadMorePresenter = LoadMoreViewPresenter()

    init {
        val loadMoreItem = mLoadMoreItemType as ItemType<Any, ItemHolder>
        mItemTypes = arrayOf(loadMoreItem) + mItemTypes
    }

    private var mLoadMoreVisible: Boolean = true
        set(value) {
            if (field == value) return
            field = value
            if (value) {
                notifyItemInserted(itemCount)
            } else {
                notifyItemRemoved(itemCount - 1)
            }
        }

    fun setOnLoadMoreListener(listener: () -> Unit) {
        mLoadMoreTrigger.onLoadMore = listener
    }

    fun setLoadMoreLoadingView(@LayoutRes layout: Int) {
        mLoadMorePresenter.loadMoreLoadingLayout = layout
    }

    fun setLoadMoreErrorView(@LayoutRes layout: Int, @IdRes retryId: Int) {
        mLoadMorePresenter.loadMoreErrorLayout = Pair(layout, retryId)
    }

    fun setLoadMoreCompleteView(@LayoutRes layout: Int) {
        mLoadMorePresenter.loadMoreCompleteLayout = layout
    }

    /**
     * 设置ItemType
     */
//    override fun setup(vararg types: ItemType<Any, ItemHolder>): MultipleTypeAdapter {
//        return super.setup(loadMoreItemType as ItemType<Any, ItemHolder>, *types)
//    }


    fun startLoading(online: Boolean = true) {
        if (currentList.isEmpty()) {
            setStatus(if (online) LoadMoreStatus.STATE_LOADING else LoadMoreStatus.STATE_OFFLINE)
        }
    }

    /**
     * 触发加载更多
     */
    fun startLoadMore() {
        if (!mLoadMoreTrigger.isLoading) {
            setStatus(LoadMoreStatus.STATE_HAS_MORE)
            mLoadMoreTrigger.loadMore()
        }
    }

    /**
     * 加载错误
     */
    fun showError() {
        setStatus(LoadMoreStatus.STATE_ERROR)
    }

    fun showComplete() {
        setStatus(LoadMoreStatus.STATE_COMPLETE)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: List<*>, hasMore: Boolean = false, isRefresh: Boolean = false) {
        val newList = if (isRefresh) list else (listOf<Any>() + currentList + list)
        submitList(newList)
        when {
            newList.isEmpty() -> setStatus(LoadMoreStatus.STATE_EMPTY)
            hasMore -> setStatus(LoadMoreStatus.STATE_HAS_MORE)
            else -> setStatus(LoadMoreStatus.STATE_COMPLETE)
        }
    }

    private fun setStatus(@LoadMoreStatus.LoadMoreStatus status: String) {
        if (mLoadMoreVisible && mLoadMoreItem.state != status) {
            mLoadMoreItem.state = status
        }
        notifyItemChanged(itemCount - 1)
        mLoadMoreTrigger.hasMore = status == LoadMoreStatus.STATE_HAS_MORE
        mLoadMoreTrigger.isLoading = false
    }


    override fun getItemCount(): Int {
        return super.getItemCount() + if (mLoadMoreVisible && currentList.isNotEmpty()) 1 else 0
    }


    override fun getItem(position: Int): Any {
        return if (currentList.size == position) mLoadMoreItem else currentList[position]
    }

    override fun getItemId(position: Int): Long {
        return if (currentList.size == position) RecyclerView.NO_ID else super.getItemId(position)
    }

    override fun onAttachedToRecyclerView(rv: RecyclerView) {
        super.onAttachedToRecyclerView(rv)
        val manager = rv.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    if (currentList.size == position) return manager.spanCount
                    val type = getItemViewType(position)
                    return if (type == RecyclerView.INVALID_TYPE) manager.spanCount else 1
                }
            }
        }
        mLoadMoreTrigger.attach(rv)
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        super.onDetachedFromRecyclerView(rv)
        mLoadMoreTrigger.detach()
    }


    override fun onViewAttachedToWindow(holder: ItemHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams && holder.itemView is LoadMoreView) {
            lp.isFullSpan = true
        }
    }
}