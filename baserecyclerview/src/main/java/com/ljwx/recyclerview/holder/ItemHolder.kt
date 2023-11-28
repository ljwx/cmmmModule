package com.ljwx.recyclerview.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

/**
 * 封装ViewHolder
 */
open class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun getImage(@IdRes id: Int): ImageView? {
        return itemView.findViewById(id)
    }

    open fun setText(@IdRes id: Int, value: CharSequence?) {
        value?.let {
            getView<TextView>(id)?.setText(it)
        }
    }

    open fun visibleGone(@IdRes id: Int, visible: Boolean) {
        getView<View>(id)?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    open fun <V : View> getView(@IdRes id: Int): V? {
        return itemView.findViewById(id)
    }

}