package com.ljwx.recyclerview.itemtype

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.ljwx.recyclerview.holder.ItemHolder
import com.ljwx.recyclerview.singleClick

open class ItemTypeLayout<Item>@JvmOverloads constructor(
    private val itemClass: Class<Item>,
    @LayoutRes
    private val layoutResId: Int,
    private val subtype: Int = 0,
    private var itemClick: ((ItemHolder, Item) -> Unit)? = null,
) : ItemType<Item, ItemHolder> ,ItemBindClick<Item>{

    private var mBinder: ((ItemHolder, Item) -> Unit)? = null
    private var mItemChildClick: ((ItemHolder, Item, Int) -> Unit)? = null
    private var mIds: Array<Int>? = null

    override fun create(parent: ViewGroup): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false))
    }

    override fun matches(item: Any): Boolean {
        return itemClass.isInstance(item) && (subtype == 0 || (item is ItemSubtype && item.subtype == subtype))
    }

    override fun bind(holder: ItemHolder, item: Item) {
        mBinder?.invoke(holder, item)
        itemClick?.let {
            holder.itemView.singleClick {
                itemClick?.invoke(holder, item)
            }
        }
        mItemChildClick?.let {
            mIds?.forEach {
                holder.itemView.findViewById<View>(it)?.singleClick {
                    mItemChildClick?.invoke(holder, item, it)
                }
            }
        }
    }

    override fun setOnItemBind(binder: (ItemHolder, Item) -> Unit) {
        this.mBinder = binder
    }

    override fun setOnItemClick(itemClick: ((ItemHolder, Item) -> Unit)) {
        this.itemClick = itemClick
    }

    override fun setOnItemChildClick(
        vararg ids: Int,
        itemClick: ((ItemHolder, Item, Int) -> Unit)
    ) {
        mIds = ids.toTypedArray()
        this.mItemChildClick = itemClick
    }

}