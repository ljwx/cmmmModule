package com.ljwx.recyclerview.adapter

import android.view.ViewGroup
import android.widget.Space
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ljwx.recyclerview.BaseRecyclerLog
import com.ljwx.recyclerview.diff.ItemDiffCallback
import com.ljwx.recyclerview.holder.ItemHolder
import com.ljwx.recyclerview.itemtype.ItemType

/**
 * 支持多类型ItemType
 */
open class MultipleTypeAdapter(
    vararg itemTypes: ItemType<*, *>,
    config: AsyncDifferConfig<Any> = AsyncDifferConfig.Builder(ItemDiffCallback()).build(),
) : ListAdapter<Any, ItemHolder>(config) {

    private val TAG = this.javaClass.simpleName + "-[rv"

    /**
     * ItemType集合
     */
    protected lateinit var mItemTypes: Array<out ItemType<Any, ItemHolder>>

    init {
        mItemTypes = itemTypes as Array<out ItemType<Any, ItemHolder>>
    }

    protected var newList: List<Any?>? = null

//    /**
//     * ItemType赋值
//     */
//    open fun setup(vararg types: ItemType<Any, ItemHolder>): MultipleTypeAdapter {
//        itemTypes = types
//        return this
//    }
//
//    /**
//     * 添加ItemType
//     */
//    fun setup(
//        registry: ItemTypeRegistry? = null,
//        append: ItemTypeRegistry.() -> Unit = {},
//    ): MultipleTypeAdapter {
//        // 登记类
//        val r = ItemTypeRegistry()
//        registry?.toArray()?.forEach {
//            r.add(it)
//        }
//        r.append()
//        setup(*r.toArray())
//        return this
//    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        for (viewType in mItemTypes.indices) {
            if (mItemTypes[viewType].matches(item)) {
                return viewType
            }
        }
        return RecyclerView.INVALID_TYPE
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        BaseRecyclerLog.d(TAG, "onCreateViewHolder,viewType:$viewType")
        if (viewType == RecyclerView.INVALID_TYPE) {
            return ItemHolder(Space(parent.context))
        }
        return mItemTypes[viewType].create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        BaseRecyclerLog.d(TAG, "onBindViewHolder:$position,holder address:$holder")
        if (holder.itemViewType != RecyclerView.INVALID_TYPE) {
            val item = getItem(position)
            mItemTypes[holder.itemViewType].bind(holder, item)
        }
    }

    open fun addItem(position: Int, item: Any?) {
        val list: List<Any?> =
            newList?.toMutableList()?.apply { add(position, item) } ?: listOf(item)
        submitList(list)
    }

    open fun deletePosition(position: Int) {
        if (position < (newList?.size ?: 0)) {
            val list = newList?.toMutableList()?.apply { removeAt(position) }
            submitList(list)
        }
    }

    open fun deleteItem(item: Any?) {
        val list = newList?.toMutableList()?.apply { remove(item) }
        submitList(list)
    }

    fun addList(list: List<Any?>) {
        submitList(listOf<Any?>() + currentList + list)
    }

    open fun getDataItem(position: Int): Any? {
        if (newList == null || newList!!.size <= position) {
            return null
        }
        return newList?.get(position)
    }

    open fun getData(): List<Any?>? {
        return newList
    }

    open fun getDataSize(): Int {
        return newList?.size ?: 0
    }

    override fun submitList(list: List<Any?>?) {
        newList = list
        super.submitList(list)
        BaseRecyclerLog.d(TAG, "提交数据")
    }

    override fun submitList(list: List<Any?>?, commitCallback: Runnable?) {
        newList = list
        super.submitList(list, commitCallback)
        BaseRecyclerLog.d(TAG, "提交数据")
    }

}