package com.ljwx.recyclerview.quick

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ljwx.recyclerview.BR
import com.ljwx.recyclerview.BaseRecyclerLog
import com.ljwx.recyclerview.diff.ItemDiffCallback
import com.ljwx.recyclerview.holder.ItemHolder
import com.ljwx.recyclerview.itemtype.ItemBindClick
import com.ljwx.recyclerview.itemtype.ItemTypeBinding
import com.ljwx.recyclerview.itemtype.ItemTypeLayout

open class QuickSingleAdapter<Item : Any> @JvmOverloads constructor(
    itemClass: Class<Item>,
    @LayoutRes
    private val layoutResId: Int,
    brId: Int? = BR.item,
    diff: DiffUtil.ItemCallback<Item> = ItemDiffCallback(),
    itemClick: ((ItemHolder, Item) -> Unit)? = null,
) : ListAdapter<Item, ItemHolder>(AsyncDifferConfig.Builder(diff).build()),
    ItemBindClick<Item> {

    private val TAG = this.javaClass.simpleName

    private val mItemType: ItemTypeLayout<Item> =
        ItemTypeBinding(itemClass, layoutResId, brId = brId)

    protected var newList: List<Item>? = null

    init {
        itemClick?.let { setOnItemClick(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        BaseRecyclerLog.d(TAG, "onCreateViewHolder")
        return mItemType.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        BaseRecyclerLog.d(TAG, "onBindViewHolder:$position,holder address:$holder")
        mItemType.bind(holder, getItem(position))
    }

    open fun addItem(position: Int, item: Item) {
        val list: List<Item> =
            newList?.toMutableList()?.apply { add(position, item) } ?: listOf(item)
        submitList(list)
    }

    open fun deletePosition(position: Int) {
        if (position < (newList?.size ?: 0)) {
            val list = newList?.toMutableList()?.apply { removeAt(position) }
            submitList(list)
        }
    }

    open fun deleteItem(item: Item) {
        val list = newList?.toMutableList()?.apply { remove(item) }
        submitList(list)
    }

    open fun addList(list: List<Item>) {
        submitList((newList ?: emptyList()) + list)
    }

    override fun setOnItemBind(binder: (ItemHolder, Item) -> Unit) {
        mItemType.setOnItemBind(binder)
    }

    override fun setOnItemClick(itemClick: ((ItemHolder, Item) -> Unit)) {
        BaseRecyclerLog.d(TAG, "设置item点击")
        mItemType.setOnItemClick(itemClick)
    }

    override fun setOnItemChildClick(
        vararg ids: Int,
        itemClick: ((ItemHolder, Item, Int) -> Unit)
    ) {
        BaseRecyclerLog.d(TAG, "设置item子view点击")
        mItemType.setOnItemChildClick(*ids, itemClick = itemClick)
    }

    open fun getDataItem(position: Int): Item? {
        if (newList == null || newList!!.size <= position) {
            return null
        }
        return newList?.get(position)
    }

    open fun getDataSize(): Int {
        return newList?.size ?: 0
    }

    override fun submitList(list: List<Item>?) {
        newList = list
        currentList
        super.submitList(list)
        BaseRecyclerLog.d(TAG, "提交数据")
    }

    override fun submitList(list: List<Item>?, commitCallback: Runnable?) {
        newList = list
        super.submitList(list, commitCallback)
        BaseRecyclerLog.d(TAG, "提交数据")
    }

}