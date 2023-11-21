package com.ljwx.baselogcheck

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ljwx.baseapp.debug.ILogCheckRecyclerView
import com.ljwx.baselogcheck.display.FixSizeVector
import com.ljwx.baselogcheck.display.LogCheckPool
import com.ljwx.baselogcheck.recycler.BaseLogAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), ILogCheckRecyclerView {

    private var filterCategory = ""
    private var filterTag = ""
    private var intervalSecond = 2

    private var logPool: FixSizeVector<CharSequence>? = null

    private var time = 0L
    private var times = 0

    init {
        if (id == View.NO_ID) {
            id = com.ljwx.baseapp.R.id.base_log_check_recycler
        }
        adapter = BaseLogAdapter()
        layoutManager =
            layoutManager ?: LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val a = getContext().obtainStyledAttributes(attrs, R.styleable.LogRecyclerView)
        try {
            filterCategory = a.getString(R.styleable.LogRecyclerView_logrv_filter_category) ?: ""
            filterTag = a.getString(R.styleable.LogRecyclerView_logrv_filter_tag) ?: ""
            intervalSecond = a.getInt(R.styleable.LogRecyclerView_logrv_interval_second, 2)
        } finally {
            a.recycle()
        }
    }

    override fun run(lifecycleScope: CoroutineScope) {
        if (!filterCategory.isNullOrEmpty()) {
            logPool = LogCheckPool.getLogPool(filterCategory)
            lifecycleScope.launch(Dispatchers.IO) {
                while (true) {
                    delay(intervalSecond * 1000L)
                    val filterResult =
                        if (!filterTag.isNullOrEmpty()) logPool?.filter { it.contains(filterTag) } else logPool
                    withContext(Dispatchers.Main) {
                        (adapter as? BaseLogAdapter)?.submitData(filterResult)
                    }
                }
            }
            setOnTouchListener { v, event ->
                if (event.action != MotionEvent.ACTION_UP) {
                    return@setOnTouchListener false
                }
                if (System.currentTimeMillis() - time > 5000) {
                    time = System.currentTimeMillis()
                    times = 0
                } else {
                    times += 1
                }
                if (times > 23) {
                    LogCheckPool.getLogPool(filterCategory).clear()
                    times = 0
                    Toast.makeText(getContext(), "log clean success", Toast.LENGTH_LONG).show()
                }
                true
            }
        }
    }

    fun addData(list: List<Any>?) {
        (adapter as? BaseLogAdapter)?.addData(list)
    }

}