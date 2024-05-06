package com.ljwx.basestate

import android.view.View
import androidx.annotation.LayoutRes

object StateLayoutConfig {

    @LayoutRes
    @JvmStatic
    var errorLayout = View.NO_ID

    @LayoutRes
    @JvmStatic
    var emptyLayout = View.NO_ID

    @LayoutRes
    @JvmStatic
    var loadingLayout = View.NO_ID

    @LayoutRes
    @JvmStatic
    var offlineLayout = View.NO_ID

    @LayoutRes
    @JvmStatic
    var extendLayout = View.NO_ID

}