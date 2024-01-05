package com.sisensing.common.ble

import com.ljwx.baselogcheck.LogCheck

object BleLog {

    val LIB = "蓝牙库"
    val LIB_LOG = "蓝牙库,日志"
    val APP = "蓝牙APP"
    val TEST = "蓝牙自测"
    val INTERRUPT = "蓝牙APP,中断"
    val GLUCOSE = "蓝牙血糖"
    val HOME_STATUS = "蓝牙首页"
    val RECONNECT = "蓝牙重连"
    private var gsDataTime = 0L

    /**
     * @param isGSData 是否是血糖数据,数量太大需要过滤
     */
    @JvmStatic
    @JvmOverloads
    fun dLib(content: String?, isGSData: Boolean = false) {
        if (!content.isNullOrBlank()) {
            if (isGSData) {
                if (System.currentTimeMillis() - gsDataTime > 20000) {
                    print(LIB, content)
                    gsDataTime = System.currentTimeMillis()
                }
            } else {
                print(LIB, content)
                gsDataTime = 0
            }
        }
    }

    @JvmStatic
    fun eLib(content: String?) {
        if (!content.isNullOrBlank()) {
            print(LIB, content, true)
        }
    }

    @JvmStatic
    fun dLibLog(content: String?) {
        print(LIB_LOG, content)
    }

    @JvmStatic
    fun dApp(content: String?) {
        print(APP, content)
    }

    @JvmStatic
    fun dTest(content: String?) {
        print(TEST, content)
    }

    @JvmStatic
    fun eApp(content: String?) {
        print(APP, content, true)
    }

    @JvmStatic
    fun dInterrupt(content: String?) {
        print(INTERRUPT, content)
    }

    @JvmStatic
    fun dGlucose(content: String?) {
        print(GLUCOSE, content)
    }

    @JvmStatic
    fun dHomeState(status: Int, content: String?) {
        print(HOME_STATUS, "当前状态:" + statusDisplay(status) + "--" + content)
    }

    @JvmStatic
    @JvmOverloads
    fun dReconnect(content: String?, red: Boolean = false) {
        print(RECONNECT, content, red)
    }

    private fun print(tag: String, content: String?, important: Boolean = false) {
        if (!content.isNullOrBlank()) {
            if (important) {
                LogCheck.e("蓝牙", tag, content)
            } else {
                LogCheck.d("蓝牙", tag, content)
            }
        }
    }

    private fun statusDisplay(status: Int): String {
        val STATUS_CONNECT = 1 //显示连接传感器

        val STATUS_CHANGE = 2 //显示更换传感器的

        val STATUS_EXCEPTION = 3 //30分钟的电流异常

        val STATUS_DISCONNECT = 4 //传感器断开连接

        val STATUS_SHOW_BS = 5 //血糖相关布局展示

        val STATUS_BS_SYNC = 6 //数据同步view

        val STATUS_SHOW_CONNECTING = 7 //连接中
        when (status) {
            STATUS_CONNECT -> return "显示连接传感器"
            STATUS_CHANGE -> return "更新换传感器"
            STATUS_EXCEPTION -> return "30分钟的电流异常"
            STATUS_DISCONNECT -> return "传感器断开连接"
            STATUS_SHOW_BS -> return "血糖相关布局展示"
            STATUS_BS_SYNC -> return "数据同步view"
            STATUS_SHOW_CONNECTING -> return "连接中"
        }
        return "未知状态"
    }

    @JvmStatic
    fun status(libStatus: Int?): String {
        val builder = StringBuilder()
        builder.append("状态码")
        when (libStatus) {
            no.sisense.android.api.Constant.STATE_DISCONNECTED -> {
                return builder.append(no.sisense.android.api.Constant.STATE_DISCONNECTED)
                    .append(":连接断开").toString()
            }

            no.sisense.android.api.Constant.STATE_CONNECTED -> {
                return builder.append(no.sisense.android.api.Constant.STATE_CONNECTED)
                    .append(":已连接").toString()
            }

            no.sisense.android.api.Constant.STATE_CONNECTING -> {
                return builder.append(no.sisense.android.api.Constant.STATE_CONNECTING)
                    .append(":连接中").toString()
            }

            no.sisense.android.api.Constant.STATE_DISCONNECTING -> {
                return builder.append(no.sisense.android.api.Constant.STATE_DISCONNECTING)
                    .append(":断开中").toString()
            }

            no.sisense.android.api.Constant.STATE_SCANNING -> {
                return builder.append(no.sisense.android.api.Constant.STATE_SCANNING)
                    .append(":扫描中").toString()
            }

            no.sisense.android.api.Constant.STATE_AUTHENTICATION_FAIL -> {
                return builder.append(no.sisense.android.api.Constant.STATE_AUTHENTICATION_FAIL)
                    .append(":鉴权失败").toString()
            }

        }
        return "未知状态:$libStatus"
    }

}