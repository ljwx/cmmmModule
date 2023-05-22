package com.ljwx.recyclerview.itemtype

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.ljwx.recyclerview.holder.ItemHolder
import com.ljwx.recyclerview.holder.ViewHolder
import com.ljwx.recyclerview.singleClick

class ItemTypeViewClass<Item, V : View>(
    private val itemClass: Class<Item>,
    private val viewClass: Class<V>,
    private val subtype: Int = 0,
    private var binder: ((ViewHolder<V>, Item) -> Unit)? = null,
) : ItemType<Item, ViewHolder<V>> ,ItemBindClick<Item>{

    private var mItemClick: ((ItemHolder, Item) -> Unit)? = null
    private var mItemChildClick: ((ItemHolder, Item, Int) -> Unit)? = null
    private var mIds: Array<Int>? = null

    override fun create(parent: ViewGroup): ViewHolder<V> =
        ViewHolder(viewClass.getConstructor(Context::class.java).newInstance(parent.context))

    override fun matches(item: Any) =
        itemClass.isInstance(item) && (subtype == 0 || (item is ItemSubtype && item.subtype == subtype))

    override fun bind(holder: ViewHolder<V>, item: Item) {
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