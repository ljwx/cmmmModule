package com.ljwx.recyclerview.itemtype

import com.ljwx.recyclerview.holder.ItemHolder

interface ItemBindClick<Item> {

    fun setOnItemBind(binder: (ItemHolder, Item) -> Unit)

    fun setOnItemClick(itemClick: ((ItemHolder, Item) -> Unit))

    fun setOnItemChildClick(vararg ids: Int, itemClick: ((ItemHolder, Item, Int) -> Unit))

}