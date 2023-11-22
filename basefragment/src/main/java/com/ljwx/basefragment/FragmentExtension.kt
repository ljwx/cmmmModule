package com.ljwx.basefragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ljwx.baseapp.constant.BaseConstBundleKey
import com.ljwx.baseapp.debug.ILogCheckRecyclerView
import com.ljwx.baseapp.extensions.visibleGone
import com.ljwx.baseapp.shake.registerShake

fun Fragment.logCheckDebugEx(open: Boolean) {
    val recycler = view?.findViewById<View>(com.ljwx.baseapp.R.id.base_log_check_recycler) ?: return
    registerShake(open) {
        var visible = recycler.visibility == View.VISIBLE
        recycler.visibleGone(!visible)
    }
    if (open && recycler is ILogCheckRecyclerView) {
        recycler.run(lifecycleScope)
    }
}

inline fun <reified F : Fragment> Fragment.fragmentInstanceEx(fromType: Int): F? {
    kotlin.runCatching {
        val fragment = F::class.java.newInstance()
        fragment.params { putInt(BaseConstBundleKey.FROM_TYPE, fromType) }
        return fragment
    }
    return null
}

inline fun Fragment.params(crossinline block: Bundle.() -> Unit = {}): Fragment {
    this.arguments = Bundle()
    arguments?.apply(block)
    return this
}