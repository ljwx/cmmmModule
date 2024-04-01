package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ljwx.baseapp.vm.empty.EmptyViewModel
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentRecyclerViewBinding
import com.ljwx.basescaffold.BaseLoadMoreFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class LoadMoreFragment :
    BaseLoadMoreFragment<FragmentRecyclerViewBinding, EmptyViewModel, String>(
        R.layout.fragment_recycler_view,
        com.ljwx.recyclerview.R.layout.holder
    ) {

    private var mTimes = 0
    override val mItemClass = String::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onLoadData(true)

    }

    override fun initRecyclerView() {
        mBinding.recycler.adapter = mLoadMoreAdapter
        mLoadMoreAdapter.setLoadMoreLoadingView(R.layout.loadmore_custom_footer_loading)
        mLoadMoreAdapter.setLoadMoreErrorView(R.layout.loadmore_custom_footer_error, null)
        mLoadMoreAdapter.setLoadMoreCompleteView(R.layout.loadmore_custom_footer_complete)
    }

    override fun onLoadData(isRefresh: Boolean) {
        changePageNum(isRefresh)
        lifecycleScope.launch(Dispatchers.IO) {
            delay(Random.nextLong(1000, 3500))
            mTimes += 1
            withContext(Dispatchers.Main) {
                if (mTimes > 8) {
                    Log.d("加载更多", "全部完成," + mTimes)
                    onLoadMoreFinish()
                } else if (mTimes > 2 && mTimes % 3 == 0) {
                    Log.d("加载更多", "加载错误," + mTimes)
                    onLoadMoreError()
                } else {
                    Log.d("加载更多", "正常加载," + mTimes)
                    val list = ArrayList<String>()
                    val end = Random.nextInt(9, 17)
                    for (i in 0 until end) {
                        list.add((mLoadMoreAdapter.itemCount + i).toString())
                    }
                    addLoadMoreData(list, isRefresh)
                }
            }
        }
    }

    override fun hasMore(datas: List<Any>): Boolean {
        return if (mTimes > 8) false else true
    }

}