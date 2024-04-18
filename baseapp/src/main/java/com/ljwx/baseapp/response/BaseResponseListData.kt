package com.ljwx.baseapp.response

open class BaseResponseListData<Item> {
    var items: List<Item>? = null
    var limit: Int? = null
    var offset: Int? = null
    var total: Int? = null

    open fun hasMore(): Boolean {
        return (offset ?: 0) < (total ?: 0)
    }

    open fun isRefresh(): Boolean {
        return offset == null || offset!! < 1
    }

    override fun toString(): String {
        return "BaseResponseListData(limit=$limit, offset=$offset, total=$total, itemsSize=${items?.size?:0})"
    }


}