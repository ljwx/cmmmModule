package com.ljwx.baseswitchenv

object AppEnvConfig {

    private var envList = mutableListOf<AppEnvItem>()

    fun addEnv(item: AppEnvItem) {
        envList.add(item)
    }

    fun addAllEnv(items: List<AppEnvItem>) {
        envList.addAll(items)
    }

    fun getEnvList(): List<AppEnvItem> {
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

open class AppEnvItem @JvmOverloads constructor(
    val title: String = "",
    val host: String = "",
    val key: String = "",
    val params: String = "",
    val tag: String = "",
)