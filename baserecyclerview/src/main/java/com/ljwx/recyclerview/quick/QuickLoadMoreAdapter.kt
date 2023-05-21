package com.ljwx.recyclerview.quick

import android.annotation.SuppressLint
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.*
import com.ljwx.recyclerview.loadmore.view.LoadMoreView
import com.ljwx.recyclerview.loadmore.view.LoadMoreViewPresenter
import com.ljwx.recyclerview.adapter.MultipleTypeAdapter
import com.ljwx.recyclerview.diff.ItemDiffCallback
import com.ljwx.recyclerview.holder.ItemHolder
import com.ljwx.recyclerview.itemtype.*
import com.ljwx.recyclerview.loadmore.LoadMoreTrigger
import com.ljwx.recyclerview.loadmore.view.LoadMoreItem

@Suppress("UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class QuickLoadMoreAdapter<Item : Any>(
    itemClass: Class<Item>,
    @LayoutRes
    private val layoutResId: Int,
    itemClick: ((ItemHolder, Item) -> Unit)? = null,
) : MultipleTypeAdapter(config = AsyncDifferConfig.Builder(ItemDiffCallback()).build()),
    ItemBindClick<Item> {

    companion object {
        const val STATE_LOADING = 0
        const val STATE_OFFLINE = 1
        const val STATE_EMPTY = 2

        const val STATE_HAS_MORE = 3
        const val STATE_COMPLETE = 4
        const val STATE_ERROR = 5
    }

    private var mItemType: ItemTypeLayout<Item>

    private val mLoadMoreItem = LoadMoreItem()
    private val mLoadMoreItemType =
        ItemTypeViewClass(LoadMoreItem::class.java, LoadMoreView::class.java) { holder, item ->
            mLoadMorePresenter.showState(holder.itemView, item.state)
        }
    private val mLoadMoreTrigger = LoadMoreTrigger()
    private val mLoadMorePresenter = LoadMoreViewPresenter()

    init {
        val loadMoreItem = mLoadMoreItemType as ItemType<Any, ItemHolder>
        mItemType = ItemTypeBinding(
            itemClass,
            layoutResId,
            itemClick = itemClick
        )
        mItemTypes = arrayOf(loadMoreItem, (mItemType as ItemType<Any, ItemHolder>))
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

    fun setLoadMoreErrorView(@LayoutRes layout: Int, @IdRes retryId: Int) {
        mLoadMorePresenter.loadMoreError = Pair(layout, retryId)
    }

    fun setLoadMoreComplete(@LayoutRes layout: Int) {
        mLoadMorePresenter.loadMoreCompleteLayout = layout
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
        if (!mLoadMoreTrigger.isLoading) {
            setStatus(STATE_HAS_MORE)
            mLoadMoreTrigger.loadMore()
        }
    }

    /**
     * 加载错误
     */
    fun showError() {
        setStatus(STATE_ERROR)
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addList(list: List<*>, hasMore: Boolean = false, isRefresh: Boolean = false) {
        val newList = if (isRefresh) list else (listOf<Any>() + currentList + list)
        submitList(newList)
        when {
            newList.isEmpty() -> setStatus(STATE_EMPTY)
            hasMore -> setStatus(STATE_HAS_MORE)
            else -> setStatus(STATE_COMPLETE)
        }
    }

    private fun setStatus(status: Int) {
        if (mLoadMoreVisible && mLoadMoreItem.state != status) {
            mLoadMoreItem.state = status
        }
        notifyItemChanged(itemCount - 1)
        mLoadMoreTrigger.hasMore = status == STATE_HAS_MORE
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

    override fun setOnItemBind(binder: (ItemHolder, Item) -> Unit) {
        mItemType.setOnItemBind(binder)
    }

    override fun setOnItemClick(itemClick: ((ItemHolder, Item) -> Unit)) {
        mItemType.setOnItemClick(itemClick)
    }

    override fun setOnItemChildClick(
        vararg ids: Int,
        itemClick: ((ItemHolder, Item, Int) -> Unit)
    ) {
        mItemType.setOnItemChildClick(*ids, itemClick = itemClick)
    }

}