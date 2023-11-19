package com.ljwx.recyclerview.text

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ljwx.recyclerview.R
import com.ljwx.recyclerview.adapter.SingleTypeAdapter
import com.ljwx.recyclerview.holder.ItemHolder
import com.ljwx.recyclerview.itemtype.ItemTypeLayout

class QuickTextRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private lateinit var mItemType: ItemTypeLayout<Any>
    private lateinit var mAdapter: SingleTypeAdapter<Any, ItemHolder>
    private var mBindItem: ((ItemHolder, CharSequence) -> Unit)? = null

    init {
        init()
    }

    private fun init() {
        mItemType = ItemTypeLayout(
            Any::class.java,
            R.layout.base_recyclerview_simple_text
        )
        mAdapter = SingleTypeAdapter(mItemType)
        adapter = mAdapter
        mItemType.setOnItemBind { itemHolder, any ->
            val text = if (any is CharSequence) any else any.toString()
            if (mBindItem == null) {
                itemHolder.itemView.findViewById<TextView>(R.id.text_view)?.text = text
            } else {
                mBindItem?.invoke(itemHolder, text)
            }
        }
        layoutManager =
            layoutManager ?: LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    open fun bindItem(bind: (ItemHolder, Any) -> Unit) {
        mBindItem = bind
    }

    open fun setOnItemClick(itemClick: ((ItemHolder, Any) -> Unit)) {
        mItemType.setOnItemClick(itemClick)
    }

    fun addData(list: List<Any>) {
        mAdapter.addList(list)
    }

}