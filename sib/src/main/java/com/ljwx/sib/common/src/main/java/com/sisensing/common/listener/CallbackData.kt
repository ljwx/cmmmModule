package com.sisensing.common.listener

interface CallbackData {

    fun <D> invoke(d: D)

}