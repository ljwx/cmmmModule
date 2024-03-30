package com.ljwx.recyclerview.adapter

import android.util.Log
import android.view.ViewGroup
import android.widget.Space
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
        if (viewType == RecyclerView.INVALID_TYPE) {
            return ItemHolder(Space(parent.context))
        }
        return mItemTypes[viewType].create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder:$position")
        if (holder.itemViewType != RecyclerView.INVALID_TYPE) {
            val item = getItem(position)
            mItemTypes[holder.itemViewType].bind(holder, item)
        }
    }

    fun addList(list: List<Any>) {
        submitList(listOf<Any>() + currentList + list)
    }
}