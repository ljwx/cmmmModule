package com.ljwx.basemodule.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.basemodule.R
import com.ljwx.basemodule.databinding.FragmentRecyclerViewBinding
import com.ljwx.recyclerview.adapter.MultipleTypeAdapter
import com.ljwx.recyclerview.quick.QuickSingleAdapter
import com.ljwx.recyclerview.itemtype.ItemTypeBinding
import com.ljwx.recyclerview.quick.QuickLoadMoreAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerViewFragment :
    BaseBindingFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadMore()

    }

    fun single() {
        val adapter = QuickSingleAdapter(
            String::class.java,
            com.ljwx.recyclerview.R.layout.holder
        ) { holder, item ->
            Log.d("ljwx2", "点击")
        }
        adapter.setOnItemBind { itemHolder, s ->
            Log.d("ljwx2", "绑定:$s")
        }
//        adapter.setOnItemClick(com.ljwx.recyclerview.R.id.tv_left, R.id.view_pager) { holder, s, id ->
//            when (id) {
//                com.ljwx.recyclerview.R.id.tv_left -> {
//                    Log.d("ljwx2", "左边")
//                }
//                com.ljwx.recyclerview.R.id.tv_right -> {
//                    Log.d("ljwx2", "右边")
//                }
//                else -> {
//                    Log.d("ljwx2", "整个")
//                }
//            }
//        }
        mBinding.recycler.adapter = adapter
        adapter.addList(
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        )
        adapter.addList(listOf("11", "12", "13", "14", "15", "16", "17", "18", "19", "20"))
    }

    fun multiple() {
        val item = ItemTypeBinding(String::class.java, com.ljwx.recyclerview.R.layout.holder)
        val item2 = ItemTypeBinding(Integer::class.java, com.ljwx.recyclerview.R.layout.holder2)
        val adapter = MultipleTypeAdapter(item, item2)
        item.setOnItemChildClick(
            com.ljwx.recyclerview.R.id.tv_left,
            R.id.view_pager
        ) { holder, s, id ->
            when (id) {
                com.ljwx.recyclerview.R.id.tv_left -> {
                    Log.d("ljwx2", "左边")
                }
                com.ljwx.recyclerview.R.id.tv_right -> {
                    Log.d("ljwx2", "右边")
                }
                else -> {
                    Log.d("ljwx2", "整个")
                }
            }
        }
        mBinding.recycler.adapter = adapter
        adapter.addList(
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        )
        adapter.addList(listOf(11, 12, 13, 14, 15))
    }

    fun loadMore() {
        mBinding.recycler
        val item = ItemTypeBinding(String::class.java, com.ljwx.recyclerview.R.layout.holder)
        val item2 = ItemTypeBinding(Integer::class.java, com.ljwx.recyclerview.R.layout.holder2)
        val adapter = QuickLoadMoreAdapter(
            String::class.java,
            com.ljwx.recyclerview.R.layout.holder
        ) { holder, item ->
            Log.d("ljwx2", "点击")
        }
        item.setOnItemChildClick(
            com.ljwx.recyclerview.R.id.tv_left,
            R.id.view_pager
        ) { holder, s, id ->
            when (id) {
                com.ljwx.recyclerview.R.id.tv_left -> {
                    Log.d("ljwx2", "左边")
                }
                com.ljwx.recyclerview.R.id.tv_right -> {
                    Log.d("ljwx2", "右边")
                }
                else -> {
                    Log.d("ljwx2", "整个")
                }
            }
        }
        adapter.setOnLoadMoreListener {
            lifecycleScope.launch(Dispatchers.IO) {
                delay(2000)
                withContext(Dispatchers.Main) {
                    val list = ArrayList<String>()
                    for (i in 0 until 11) {
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