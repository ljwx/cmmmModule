package com.ljwx.recyclerview.itemtype

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import androidx.core.app.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.ljwx.recyclerview.BR
import com.ljwx.recyclerview.holder.ItemHolder

class ItemTypeBinding<Item>@JvmOverloads constructor(
    private val itemClass: Class<Item>,
    @LayoutRes
    private val layoutResId: Int,
    private val subtype: Int = 0,
    private val brId: Int? = BR.item,
    private var itemClick: ((ItemHolder, Item) -> Unit)? = null,
) : ItemTypeLayout<Item>(itemClass, layoutResId, subtype, itemClick) {

    override fun bind(holder: ItemHolder, item: Item) {
        DataBindingUtil.bind<ViewDataBinding>(holder.itemView)?.let {
            if (brId != null) {
                it.setVariable(brId, item)
            }
            if (it.lifecycleOwner == null) {
                it.lifecycleOwner = holder.itemView.getLifecycleOwner()
            }
        }
        super.bind(holder, item)
    }

}

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal fun View.getLifecycleOwner(): LifecycleOwner {
    if (this is LifecycleOwner) return this
    var obj: Context? = context
    do {
        if (obj is ComponentActivity) return obj
        obj = if (obj is ContextWrapper) obj.baseContext else null
    } while (obj != null)
    throw Exception("can not find LifecycleOwner")
}