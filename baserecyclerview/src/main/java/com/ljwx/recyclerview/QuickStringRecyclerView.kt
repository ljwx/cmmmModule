package com.ljwx.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.ljwx.recyclerview.adapter.SingleTypeAdapter
import com.ljwx.recyclerview.holder.ItemHolder
import com.ljwx.recyclerview.itemtype.ItemTypeLayout

class QuickStringRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private lateinit var mItemType: ItemTypeLayout<Any>
    private lateinit var mAdapter: SingleTypeAdapter<Any, ItemHolder>
    private var mBindItem: ((ItemHolder, Any) -> Unit)? = null

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
            mBindItem?.invoke(itemHolder, any)
        }
    }

    fun bindItem(bind: (ItemHolder, Any) -> Unit) {
        mBindItem = bind
    }

    fun addData(list: List<Any>) {
        mAdapter.addList(list)
    }

}