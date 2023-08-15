package com.sisensing.common.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.TimeUtils
import com.ljwx.basemodule.BuildConfig
import com.ljwx.basemodule.R
import com.ljwx.recyclerview.quick.QuickSingleAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Vector
import java.util.concurrent.TimeUnit

object LogCacheUtils {

    private val map = HashMap<RecyclerView, Disposable>()

    private val map2 = HashMap<String, FixSizeVector<String>>()

    fun addLog(tag: String, log: String?) {
        var l = map2.get(tag)
        if (l == null) {
            l = FixSizeVector(500)
            map2.put(tag, l)
        }
        log?.let {
            l.add(TimeUtils.getNowString(SimpleDateFormat("HH:mm:ss")) + "-"+log)
        }
    }

    private fun getList(tag: String): Vector<String> {
        return map2.get(tag) ?: Vector<String>()
    }

    fun clear(tag: String) {
        map2.get(tag)?.clear()
    }

    fun initLogView(controlView: View, recyclerView: RecyclerView, tag: String) {
        if (!BuildConfig.DEBUG) {
            return
        }
        if (map.get(recyclerView) == null) {
//            val adapter = QuickSingleAdapter(String::class.java, R.layout.item_simple_log)
//            recyclerView.setAdapter(adapter)
//            map.put(recyclerView,
//                Observable.interval(500, 1500, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe { aLong: Long? ->
//                        val list = Vector<String>()
//                        list.addAll(getList(tag))
//                        adapter.submitList(list)
//                    })
        }
        controlView.setOnLongClickListener { v ->
            if (recyclerView.visibility === View.VISIBLE) {
                recyclerView.visibility = View.GONE
            } else {
                recyclerView.visibility = View.VISIBLE
            }
            false
        }
    }

}

object LogCacheTag {
    const val BLE_CONNECT = "蓝牙库"
}