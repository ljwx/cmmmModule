package com.ljwx.recyclerview

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ljwx.recyclerview.holder.ItemHolder
import com.ljwx.recyclerview.holder.ViewHolder
import com.ljwx.recyclerview.itemtype.*
import com.ljwx.recyclerview.itemtype.getLifecycleOwner

/**
 * 使用view对象
 */
inline fun <reified Item, reified V> viewType(
    subtype: Int = 0
): ItemType<Item, ViewHolder<V>> where V : View, V : ItemView<Item> {
    return ItemTypeViewClass(Item::class.java, V::class.java, subtype) { holder, item ->
        holder.view.bind(item)
    }
}

/**
 * 不带databinding
 */
inline fun <reified Item> layoutType(
    @LayoutRes layoutResId: Int,
    subtype: Int = 0,
    noinline binder: (ItemHolder, Item) -> Unit,
): ItemType<Item, ItemHolder> {
    return ItemTypeLayout(Item::class.java, layoutResId, subtype, binder)
}

/**
 * 使用databinding数据绑定
 */
inline fun <reified Item> bindingType(
    @LayoutRes layoutResId: Int,
    subtype: Int = 0,
    brId: Int = BR.item,
    noinline onClick: ((ItemHolder, Item) -> Unit)? = null,
): ItemType<Item, ItemHolder> {
    return ItemTypeLayout(Item::class.java, layoutResId, subtype) { holder, item ->
        onClick?.let {
            holder.itemView.setOnClickListener {
                onClick(holder, item)
            }
        }
        DataBindingUtil.bind<ViewDataBinding>(holder.itemView)?.let {
            it.setVariable(brId, item)
            if (it.lifecycleOwner == null) {
                it.lifecycleOwner = holder.itemView.getLifecycleOwner()
            }
        }
    }
}