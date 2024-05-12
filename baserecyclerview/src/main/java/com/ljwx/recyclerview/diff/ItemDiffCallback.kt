package com.ljwx.recyclerview.diff

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * 数据差异化判断
 */
open class ItemDiffCallback<Item : Any>(private val areSameItems: (o: Item, n: Item) -> Boolean = { o, n -> o == n }) :
    DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return areSameItems(oldItem, newItem)
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.toString() == newItem.toString()
    }
}
