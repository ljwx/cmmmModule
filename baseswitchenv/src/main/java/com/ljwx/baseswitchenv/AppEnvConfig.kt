package com.ljwx.baseswitchenv

object AppEnvConfig {

    private var envList = mutableListOf<AppConfigItem>()

    fun addEnv(item: AppConfigItem) {
        envList.add(item)
    }

    fun addAllEnv(items: List<AppConfigItem>) {
        envList.addAll(items)
    }

    fun getEnvList(): List<AppConfigItem> {
        return envList
    }

    fun getStringArray(): Array<String> {
        val list = arrayListOf<String>()
        envList.forEach {
            list.add(it.title + ":" + it.host)
        }
        return list.toTypedArray()
    }

}

open class AppConfigItem @JvmOverloads constructor(
    val title: String = "",
    val host: String = "",
    val key: String = "",
    val params: String = "",
    val tag: String = "",
)