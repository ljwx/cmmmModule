package com.ljwx.baseapp.response

open class BaseResponseListData<Item> {
    val items: List<Item>? = null
    val limit: Int? = null
    val offset: Int? = null
    val total: Int? = null

    fun hasMore(): Boolean {
        return (offset ?: 0) < (total ?: 0)
    }

    fun isRefresh(): Boolean {
        return offset == null || offset < 1
    }

}