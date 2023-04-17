package com.ljwx.recyclerview.quick.type

import android.view.View
import android.view.ViewGroup
import com.ljwx.recyclerview.quick.holder.ViewHolder

class ViewItemType<Item>(
    private val itemClass: Class<Item>,
    private val view: View,
    private val subtype: Int = 0,
    private val binder: (ViewHolder<View>, Item) -> Unit,
) : ItemType<Item, ViewHolder<View>> {

    override fun create(parent: ViewGroup): ViewHolder<View> = ViewHolder(view)

    override fun matches(item: Any) =
        itemClass.isInstance(item) && (subtype == 0 || (item is ItemSubtype && item.subtype == subtype))

    override fun bind(holder: ViewHolder<View>, item: Item) {
        binder(holder, item)
    }
}