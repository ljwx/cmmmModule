package com.ljwx.recyclerview.quick.type

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.ljwx.recyclerview.quick.holder.ItemHolder

class LayoutItemType<Item>(
    private val itemClass: Class<Item>,
    @LayoutRes
    private val layoutResId: Int,
    private val subtype: Int = 0,
    private val binder: (ItemHolder, Item) -> Unit,
) : ItemType<Item, ItemHolder> {

    /**
     * 创建ViewHolder
     */
    override fun create(parent: ViewGroup): ItemHolder {
        return ItemHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false))
    }

    /**
     * 判断是否同一类型
     */
    override fun matches(item: Any): Boolean {
        return itemClass.isInstance(item) && (subtype == 0 || (item is ItemSubtype && item.subtype == subtype))
    }

    /**
     * 绑定holder和数据
     */
    override fun bind(holder: ItemHolder, item: Item) {
        binder(holder, item)
    }
}