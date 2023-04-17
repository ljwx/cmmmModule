package com.ljwx.recyclerview.loadmore

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*
import com.ljwx.recyclerview.loadmore.view.LoadMoreView
import com.ljwx.recyclerview.loadmore.view.LoadMoreViewPresenter
import com.ljwx.recyclerview.quick.adapter.MultipleTypeAdapter
import com.ljwx.recyclerview.quick.diff.ItemDiffCallback
import com.ljwx.recyclerview.quick.holder.ItemHolder
import com.ljwx.recyclerview.quick.type.ItemType
import com.ljwx.recyclerview.quick.type.ViewClassItemType

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class LoadMoreAdapter(
    config: AsyncDifferConfig<Any> = AsyncDifferConfig.Builder(ItemDiffCallback()).build(),
) : MultipleTypeAdapter<Any>(config) {

    companion object {
        const val STATE_LOADING = 0
        const val STATE_OFFLINE = 1
        const val STATE_EMPTY = 2

        const val STATE_HAS_MORE = 3
        const val STATE_COMPLETE = 4
        const val STATE_ERROR = 5
    }

    internal class LoadMoreItem {
        var state: Int = STATE_LOADING
    }

    private val loadMoreTrigger = LoadMoreTrigger()
    private val loadMorePresenter = LoadMoreViewPresenter()

    private var loadMoreItem = LoadMoreItem()
    private var loadMoreItemType =
        ViewClassItemType(LoadMoreItem::class.java, LoadMoreView::class.java) { holder, item ->
            loadMorePresenter.showState(holder.itemView, item.state)
        }


    private var loadMoreVisible: Boolean = true
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
        loadMoreTrigger.onLoadMore = listener
    }

    fun setLoadMoreErrorView(@LayoutRes layout: Int, @IdRes retryId: Int) {
        loadMorePresenter.loadMoreError = Pair(layout, retryId)
    }

    fun setLoadMoreComplete(@LayoutRes layout: Int){
        loadMorePresenter.loadMoreCompleteLayout = layout
    }

    /**
     * 设置ItemType
     */
    override fun setup(vararg types: ItemType<Any, ItemHolder>): MultipleTypeAdapter<Any> {
        return super.setup(loadMoreItemType as ItemType<Any, ItemHolder>, *types)
    }


    fun startLoading(online: Boolean = true) {
        if (currentList.isEmpty()) {
            setStatus(if (online) STATE_LOADING else STATE_OFFLINE)
        }
    }

    /**
     * 触发加载更多
     */
    fun startLoadMore() {
        if (!loadMoreTrigger.isLoading) {
            setStatus(STATE_HAS_MORE)
            loadMoreTrigger.loadMore()
        }
    }

    /**
     * 加载错误
     */
    fun showError() {
        setStatus(STATE_ERROR)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: List<Any>, hasMore: Boolean = false, isRefresh: Boolean = false) {

        val newList = if (isRefresh) list else (listOf<Any>() + currentList + list)

        submitList(newList)

        when {
            newList.isEmpty() -> setStatus(STATE_EMPTY)
            hasMore -> setStatus(STATE_HAS_MORE)
            else -> setStatus(STATE_COMPLETE)
        }
    }

    private fun setStatus(status: Int) {
        if (loadMoreVisible && loadMoreItem.state != status) {
            loadMoreItem.state = status
        }
        notifyItemChanged(itemCount - 1)
        loadMoreTrigger.hasMore = status == STATE_HAS_MORE
        loadMoreTrigger.isLoading = false
    }


    override fun getItemCount(): Int {
        return super.getItemCount() + if (loadMoreVisible && !currentList.isEmpty()) 1 else 0
    }


    override fun getItem(position: Int): Any {
        return if (currentList.size == position) loadMoreItem else currentList[position]
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
        loadMoreTrigger.attach(rv)
    }

    override fun onDetachedFromRecyclerView(rv: RecyclerView) {
        super.onDetachedFromRecyclerView(rv)
        loadMoreTrigger.detach()
    }


    override fun onViewAttachedToWindow(holder: ItemHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder.itemView.layoutParams
        if (lp is StaggeredGridLayoutManager.LayoutParams && holder.itemView is LoadMoreView) {
            lp.isFullSpan = true
        }
    }
}