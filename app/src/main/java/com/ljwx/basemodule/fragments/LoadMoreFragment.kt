package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ljwx.baseapp.BaseViewModel
import com.ljwx.basefragment.BaseMVVMFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentRecyclerViewBinding
import com.ljwx.basescaffold.BaseLoadMoreFragment
import com.ljwx.recyclerview.loadmore.LoadMoreAdapter
import com.ljwx.recyclerview.adapter.MultipleTypeAdapter
import com.ljwx.recyclerview.quick.QuickSingleAdapter
import com.ljwx.recyclerview.itemtype.ItemTypeBinding
import com.ljwx.recyclerview.quick.QuickLoadMoreAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadMoreFragment :
    BaseLoadMoreFragment<FragmentRecyclerViewBinding, BaseViewModel, String>(
        R.layout.fragment_recycler_view,
        com.ljwx.recyclerview.R.layout.holder
    ) {

    private var mTimes = 1
    override val mItemClass = String::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onLoadData(true)

    }

    override fun initRecyclerView() {
        mBinding.recycler.adapter = mLoadMoreAdapter
    }

    override fun onLoadData(isRefresh: Boolean) {
        changePage(isRefresh)
        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            val list = ArrayList<String>()
            val end = if (mTimes < 3) 21 else 10
            Log.d("ljwx2", "加多少:" + end)
            for (i in 0 until end) {
                list.add((mLoadMoreAdapter.itemCount + i).toString())
            }
            withContext(Dispatchers.Main) {
                mTimes += 1
                addLoadMoreData(list, isRefresh)
            }
        }
    }
}