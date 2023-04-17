package com.ljwx.recyclerview.quick.adapter

import android.util.Log
import android.view.ViewGroup
import android.widget.Space
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ljwx.recyclerview.ItemTypeRegistry
import com.ljwx.recyclerview.quick.diff.ItemDiffCallback
import com.ljwx.recyclerview.quick.holder.ItemHolder
import com.ljwx.recyclerview.quick.type.ItemType

/**
 * 支持多类型ItemType
 */
open class MultipleTypeAdapter<Item : Any>(
    config: AsyncDifferConfig<Item> = AsyncDifferConfig.Builder<Item>(ItemDiffCallback()).build(),
) : ListAdapter<Item, ItemHolder>(config) {

    private val TAG = "rv-" + this.javaClass.simpleName

    /**
     * ItemType集合
     */
    private lateinit var itemTypes: Array<out ItemType<Any, ItemHolder>>

    /**
     * ItemType赋值
     */
    open fun setup(vararg types: ItemType<Any, ItemHolder>): MultipleTypeAdapter<Item> {
        itemTypes = types
        return this
    }

    /**
     * 添加ItemType
     */
    fun setup(
        registry: ItemTypeRegistry? = null,
        append: ItemTypeRegistry.() -> Unit = {},
    ): MultipleTypeAdapter<Item> {
        // 登记类
        val r = ItemTypeRegistry()
        registry?.toArray()?.forEach {
            r.add(it)
        }
        r.append()
        setup(*r.toArray())
        return this
    }

    /**
     * 获取ItemType类型
     */
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        for (viewType in itemTypes.indices) {
            if (itemTypes[viewType].matches(item)) {
                return viewType
            }
        }
        return RecyclerView.INVALID_TYPE
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        if (viewType == RecyclerView.INVALID_TYPE) {
            return ItemHolder(Space(parent.context))
        }
        return itemTypes[viewType].create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder:$position")
        if (holder.itemViewType != RecyclerView.INVALID_TYPE) {
            val item = getItem(position)
            itemTypes[holder.itemViewType].bind(holder, item)
        }
    }
}