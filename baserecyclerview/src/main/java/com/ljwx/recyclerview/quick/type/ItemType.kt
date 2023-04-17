package com.ljwx.recyclerview.quick.type

import android.view.ViewGroup
import com.ljwx.recyclerview.quick.holder.ItemHolder


interface ItemType<Item, Holder : ItemHolder> {

    /**
     * 创建ViewHolder
     */
    fun create(parent: ViewGroup): Holder

    /**
     * 判断ItemType是否同一种类
     */
    fun matches(item: Any): Boolean

    /**
     * 绑定holder和数据
     */
    fun bind(holder: Holder, item: Item)
}