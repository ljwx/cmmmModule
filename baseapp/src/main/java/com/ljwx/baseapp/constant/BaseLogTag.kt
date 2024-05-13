package com.ljwx.baseapp.constant

object BaseLogTag {
    const val COMMON_PRE = "-["
    const val MVVM = "${COMMON_PRE}mvvm"
    const val ACTIVITY = "${COMMON_PRE}activity"
    const val FRAGMENT = COMMON_PRE + "fragment"
    const val DATA_REPOSITORY = COMMON_PRE + "repository"
    const val VIEW_MODEL = COMMON_PRE + "VM"
    const val LOCAL_EVENT = COMMON_PRE + "event"
    const val VIEW_MODEL_SCOPE = COMMON_PRE + "VMScope"
    const val STATE_LAYOUT = COMMON_PRE + "stateLayout"
}