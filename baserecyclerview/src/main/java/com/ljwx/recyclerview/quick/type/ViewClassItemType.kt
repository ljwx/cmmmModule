package com.ljwx.recyclerview.quick.type

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.ljwx.recyclerview.quick.holder.ViewHolder

class ViewClassItemType<Item, V: View>(
    private val itemClass: Class<Item>,
    private val viewClass: Class<V>,
    private val subtype: Int = 0,
    private val binder: (ViewHolder<V>, Item) -> Unit
) : ItemType<Item, ViewHolder<V>> {

    override fun create(parent: ViewGroup): ViewHolder<V> = ViewHolder(viewClass.getConstructor(Context::class.java).newInstance(parent.context))

    override fun matches(item: Any) = itemClass.isInstance(item) && (subtype == 0 || (item is ItemSubtype && item.subtype == subtype))

    override fun bind(holder: ViewHolder<V>, item: Item) {
        binder(holder, item)
    }
}