package com.ljwx.baseapp.extensions

import android.view.View
import com.blankj.utilcode.util.NetworkUtils

@Deprecated(message = "请使用singleClick", replaceWith = ReplaceWith(expression = "singleClick"))
inline fun View.click(
    period: Long = 500,
    checkNetwork: Boolean = false,
    noinline block: View.() -> Unit
) {
    setOnClickListener(SingleClickListener(period, checkNetwork, block))
}

inline fun View.singleClick(
    period: Long = 500,
    checkNetwork: Boolean = false,
    noinline block: View.() -> Unit
) {
    setOnClickListener(SingleClickListener(period, checkNetwork, block))
}

inline fun <D> View.clickReport(
    reportType: String? = null,
    reportData: D? = null,
    checkNetwork: Boolean = false,
    period: Long = 500,
    noinline block: View.() -> Unit
) {
    setOnClickListener(ReportClickListener(period, checkNetwork, reportType, reportData, block))
}

open class SingleClickListener(
    private val period: Long = 500,
    private val checkNetwork: Boolean = false,
    private var block: (View.() -> Unit)?
) : View.OnClickListener {

    private var lastTime: Long = 0

    override fun onClick(v: View) {
        if (checkNetwork && !BaseNetworkCheck.checkNetworkEnable()) {
            BaseNetworkCheck.onNotAvailable(0)
            return
        }
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > period) {
            lastTime = currentTime
            block?.invoke(v)
        }
    }
}

open class ReportClickListener(
    private val period: Long = 500,
    private val checkNetwork: Boolean = false,
    private val reportType: String? = null,
    private val reportData: Any? = null,
    private var block: View.() -> Unit
) :
    View.OnClickListener {

    private var lastTime: Long = 0

    override fun onClick(v: View) {
        if (checkNetwork && !BaseNetworkCheck.checkNetworkEnable()) {
            BaseNetworkCheck.onNotAvailable(0)
            return
        }
        if (reportType != null) {
            BaseViewClickReport.report(reportType, reportData)
        }
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > period) {
            lastTime = currentTime
            block(v)
        }
    }
}

object BaseNetworkCheck {

    private var callback: ((type: Int) -> Unit)? = null

    fun checkNetworkEnable(): Boolean {
        return NetworkUtils.getMobileDataEnabled()
    }

    fun setNotAvailableListener(listener: ((type: Int) -> Unit)?) {
        callback = listener
    }

    fun onNotAvailable(type: Int) {
        callback?.invoke(type)
    }

}

object BaseViewClickReport {

    private var callback: ((type: String?, data: Any?) -> Unit)? = null

    fun setReportListener(listener: ((type: String?, data: Any?) -> Unit)?) {
        callback = listener
    }

    fun <T> report(type: String?, data: T) {
        callback?.invoke(type, data)
    }
}