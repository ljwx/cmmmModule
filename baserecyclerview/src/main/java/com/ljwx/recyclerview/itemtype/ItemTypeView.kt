package com.ljwx.recyclerview.itemtype

import android.view.View
import android.view.ViewGroup
import com.ljwx.recyclerview.holder.ItemHolder
import com.ljwx.recyclerview.holder.ViewHolder
import com.ljwx.recyclerview.singleClick

class ItemTypeView<Item>(
    private val itemClass: Class<Item>,
    private val view: View,
    private val subtype: Int = 0,
    private var binder: ((ViewHolder<View>, Item) -> Unit)? = null,
) : ItemType<Item, ViewHolder<View>> ,ItemBindClick<Item>{

    private var mItemClick: ((ItemHolder, Item) -> Unit)? = null
    private var mItemChildClick: ((ItemHolder, Item, Int) -> Unit)? = null
    private var mIds: Array<Int>? = null

    override fun create(parent: ViewGroup): ViewHolder<View> = ViewHolder(view)

    override fun matches(item: Any) =
        itemClass.isInstance(item) && (subtype == 0 || (item is ItemSubtype && item.subtype == subtype))

    override fun bind(holder: ViewHolder<View>, item: Item) {
        binder?.let { it(holder, item) }
        mItemClick?.let {
            holder.itemView.singleClick {
                mItemClick?.invoke(holder, item)
            }
        }
    }

    override fun setOnItemBind(binder: (ItemHolder, Item) -> Unit) {
        this.binder = binder
    }

    override fun setOnItemClick(itemClick: ((ItemHolder, Item) -> Unit)) {
        this.mItemClick = itemClick
    }

    override fun setOnItemChildClick(
        vararg ids: Int,
        itemClick: ((ItemHolder, Item, Int) -> Unit)
    ) {
        mIds = ids.toTypedArray()
        this.mItemChildClick = itemClick
    }
}