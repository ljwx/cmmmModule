package com.ljwx.basemodule

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ljwx.baseapp.BaseViewModel
import com.ljwx.basefragment.BaseMVVMFragment
import com.ljwx.basemodule.databinding.FragmentRecyclerViewBinding
import com.ljwx.recyclerview.bindingType
import com.ljwx.recyclerview.loadmore.LoadMoreAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerViewFragment :
    BaseMVVMFragment<FragmentRecyclerViewBinding, BaseViewModel>(R.layout.fragment_recycler_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LoadMoreAdapter()
        adapter.setup {
            add(bindingType(com.ljwx.recyclerview.R.layout.holder))
        }
        adapter.setOnLoadMoreListener {
            lifecycleScope.launch(Dispatchers.IO) {
                delay(2000)
                withContext(Dispatchers.Main) {
                    val list = ArrayList<String>()
                    for (i in 0 until 10) {
                        list.add((adapter.itemCount + i).toString())
                    }
                    adapter.addList(
                        list,
                        true,
                        false
                    )
                }
            }
        }
        mBinding.recycler.adapter = adapter
        adapter.addList(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"), true, true)
    }

}