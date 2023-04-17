package com.ljwx.recyclerview.quick.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import com.ljwx.recyclerview.quick.diff.ItemDiffCallback
import com.ljwx.recyclerview.quick.holder.ItemHolder
import com.ljwx.recyclerview.quick.type.ItemType


class SingleTypeAdapter<Item : Any, Holder : ItemHolder>(
    type: ItemType<Item, Holder>,
    config: AsyncDifferConfig<Item> = AsyncDifferConfig.Builder<Item>(ItemDiffCallback()).build(),
) : ListAdapter<Item, Holder>(config) {

    private val TAG = "rv-" + this.javaClass.simpleName

    private val itemType: ItemType<Item, Holder> = type

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return itemType.create(parent)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.d(TAG, "onBindViewHolder:$position")
        itemType.bind(holder, getItem(position))
    }
}