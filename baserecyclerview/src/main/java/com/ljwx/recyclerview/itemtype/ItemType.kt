package com.ljwx.recyclerview.itemtype

import android.view.ViewGroup
import com.ljwx.recyclerview.holder.ItemHolder


interface ItemType<Item, Holder : ItemHolder> {

    fun create(parent: ViewGroup): Holder

    fun matches(item: Any): Boolean

    fun bind(holder: Holder, item: Item)
}