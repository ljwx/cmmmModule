package com.ljwx.recyclerview.loadmore

import androidx.annotation.StringDef

object LoadMoreStatus {

    const val STATE_LOADING = "loading"
    const val STATE_OFFLINE = "offline"
    const val STATE_EMPTY = "empty"

    const val STATE_HAS_MORE = "hasMore"
    const val STATE_COMPLETE = "complete"
    const val STATE_ERROR = "error"

    @StringDef(STATE_LOADING, STATE_OFFLINE, STATE_EMPTY, STATE_HAS_MORE, STATE_COMPLETE, STATE_ERROR)
    @Retention(AnnotationRetention.SOURCE)
    annotation class LoadMoreStatus

}