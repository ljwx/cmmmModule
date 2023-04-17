package com.ljwx.recyclerview

import com.ljwx.recyclerview.quick.holder.ItemHolder
import com.ljwx.recyclerview.quick.type.ItemType

/**
 * ItemType容器类
 */
class ItemTypeRegistry {
    private val types = mutableListOf<ItemType<Any, ItemHolder>>()

    fun <Item : Any, Holder : ItemHolder> add(type: ItemType<Item, Holder>) {
        @Suppress("UNCHECKED_CAST")
        types.add(type as ItemType<Any, ItemHolder>)
    }

    fun toArray() = types.toTypedArray()
}