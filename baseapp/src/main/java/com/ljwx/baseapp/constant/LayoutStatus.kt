package com.ljwx.baseapp.constant

import androidx.annotation.IntDef

object LayoutStatus {

    const val CONTENT = 0
    const val LOADING = 1
    const val EMPTY = 2
    const val ERROR = 3
    const val OFFLINE = 4

    @IntDef(
        CONTENT,
        LOADING,
        EMPTY,
        ERROR,
        OFFLINE,
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class LayoutStatus

}