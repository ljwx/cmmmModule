package com.ljwx.basescaffold

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.basefragment.BaseMVVMFragment
import com.ljwx.recyclerview.quick.QuickLoadMoreAdapter

abstract class BaseLoadMoreFragment<Binding : ViewDataBinding, ViewModel : BaseViewModel<*>, Item : Any>(
    @LayoutRes private val fragmentLayout: Int, @LayoutRes private val itemLayout: Int
) : BaseMVVMFragment<Binding, ViewModel>(fragmentLayout) {

    abstract val mItemClass: Class<Item>

    private var mPage = 1

    protected var mPageSize = 15

    protected var mLoadMoreOffset = 0

    protected lateinit var mLoadMoreAdapter: QuickLoadMoreAdapter<Item>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLoadMoreAdapter = QuickLoadMoreAdapter(mItemClass, itemLayout)
        mLoadMoreAdapter.setOnLoadMoreListener {
            onLoadData(false)
        }
        initRecyclerView()

    }

    abstract fun initRecyclerView()

    abstract fun onLoadData(isRefresh: Boolean)

    override fun onRefreshData(type: Long) {
        super.onRefreshData(type)
        onLoadData(true)
    }

    open fun changePageNum(isRefresh: Boolean) {
        mPage = if (isRefresh) 0 else mPage + 1
    }

    open fun addLoadMoreData(datas: List<Any>, isRefresh: Boolean) {
        mLoadMoreAdapter.addList(datas, hasMore(datas), isRefresh)
    }

    fun onLoadMoreError() {
        mLoadMoreAdapter.showError()
    }

    fun onLoadMoreFinish() {
        mLoadMoreAdapter.showComplete()
    }

    open fun hasMore(datas: List<Any>): Boolean {
        return !(datas.isNullOrEmpty() || datas.size < mPageSize)
    }
}