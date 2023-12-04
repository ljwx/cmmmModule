package com.ljwx.provideclipboardauto

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.ljwx.baseapp.regex.CommonRegex
import com.ljwx.baseapp.extensions.delayRun
import com.ljwx.baseapp.extensions.isMatch
import com.ljwx.baseapp.extensions.singleClick
import com.ljwx.basefragment.BaseBindingFragment
import com.ljwx.provideclipboardauto.database.ClipboardDataEntity
import com.ljwx.provideclipboardauto.database.DBManager
import com.ljwx.provideclipboardauto.databinding.FragmentClipboardBinding
import com.ljwx.recyclerview.quick.QuickSingleAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClipboardFragment :
    BaseBindingFragment<FragmentClipboardBinding>(R.layout.fragment_clipboard) {

    private val clipboard by lazy {
        context?.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as? ClipboardManager
    }

    private val adapter by lazy {
        QuickSingleAdapter(ClipboardDataEntity::class.java, R.layout.item_clipboard)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonProcessSteps()
        updateList()

    }

    override fun initUIView() {
        super.initUIView()
        mBinding.edit.setOnFocusChangeListener { view, b ->
            if (b) {
                delayRun(300) {
                    showAndSave()
                    updateList()
                }
            }
        }
        mBinding.recycler.adapter = adapter
        adapter.setOnItemClick { itemHolder, clipboardDataEntity ->
            clipboard?.setPrimaryClip(
                ClipData.newPlainText(
                    "MIMETYPE_TEXT_PLAIN",
                    clipboardDataEntity.url
                )
            )
            ToastUtils.showShort("已复制")
            lifecycleScope.launch(Dispatchers.IO) {
                clipboardDataEntity.time = System.currentTimeMillis() - (1000 * 3600 * 24 * 30)
                DBManager.clipboardDao().updateItem(clipboardDataEntity)
                updateList()
            }
        }
    }

    override fun setClickListener() {
        super.setClickListener()
        mBinding.get.singleClick {
            lifecycleScope.launch(Dispatchers.IO) {
                val item = DBManager.clipboardDao().getLast()
                handle(item)
            }
        }
        mBinding.clear.setOnLongClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                DBManager.clipboardDao().clear()
                updateList()
            }
            true
        }
    }

    fun handle(clipboardDataEntity:ClipboardDataEntity?) {
        if (clipboardDataEntity == null) {
            return
        }
        clipboard?.setPrimaryClip(
            ClipData.newPlainText(
                "MIMETYPE_TEXT_PLAIN",
                clipboardDataEntity?.url?:"空的"
            )
        )
        ToastUtils.showLong("已复制")
        lifecycleScope.launch(Dispatchers.IO) {
            clipboardDataEntity.time = System.currentTimeMillis() - (1000 * 3600 * 24 * 30)
            DBManager.clipboardDao().updateItem(clipboardDataEntity)
            updateList()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        delayRun(300) {
            showAndSave()
            updateList()
        }
    }

    override fun onResume() {
        super.onResume()
        mBinding.edit.requestFocus()
        delayRun(300) {
            showAndSave()
            updateList()
        }
    }

    private fun updateList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val all = DBManager.clipboardDao().getAll()
            withContext(Dispatchers.Main) {
                adapter.submitList(all)
            }
        }
    }

    private fun showAndSave() {
        getClipboardData()?.forEach {
            delayRun(200) {
                mBinding.edit.setText("")
                mBinding.edit.setText(it)
                save(it.toString())
            }
        }
    }

    private fun save(clipboard: String) {
        var content = clipboard
        if (!content.contains("http")) {
            return
        }
        content = content.replace("复制打开抖音，看看", "")
        val split = content.split(" ")
        val item = ClipboardDataEntity()
        lifecycleScope.launch(Dispatchers.IO) {
            split.forEachIndexed { index, iit ->
                var it = iit
                if (iit.isMatch(CommonRegex.decimal)) {
                    it = ""
                }
                if (it.startsWith("http")) {
                    val exist = DBManager.clipboardDao().findItem(it)
                    if (exist != null) {
                        return@launch
                    } else {
                        item.url = it
                    }
                } else {
                    if (item.url.isNullOrEmpty()) {
                        item.title = item.title + it
                    }
                }
            }
            item.time = TimeUtils.getNowMills()
            DBManager.clipboardDao().addItem(item)
            updateList()
        }
    }

    private fun getClipboardData(): List<CharSequence>? {
        val clipData = clipboard?.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            ToastUtils.showShort("有" + clipData.itemCount + "条")
            val list = ArrayList<CharSequence>()
            for (i in 0 until clipData.itemCount) {
                list.add(clipData.getItemAt(i).text)
            }
            return list
        }
        Log.d("剪切板", "空的")
        return null
    }

}