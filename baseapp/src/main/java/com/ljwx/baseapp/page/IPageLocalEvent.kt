package com.ljwx.baseapp.page

import android.content.Intent
import com.ljwx.baseapp.event.ISendLocalEvent

interface IPageLocalEvent : ISendLocalEvent {

    fun registerLocalEvent(
        action: String?,
        observer: (action: String, type: Int?, intent: Intent) -> Unit
    )

    fun unregisterLocalEvent(action: String?)

}