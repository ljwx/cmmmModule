package com.ljwx.baseapp.extensions

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.Utils
import com.ljwx.baseapp.vm.BaseViewModel

private var mToast: Toast? = null

private var oldContent: CharSequence? = null
private var oldTime: Long = 0

inline fun Context.showToast(
    content: String?,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.CENTER,
    repeat: Boolean = false
) {
    if (content.isNullOrEmpty()) {
        return
    }
    show(content, duration, gravity, repeat)
}

inline fun Fragment.showToast(
    content: String?,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.CENTER,
    repeat: Boolean = false
) {
    show(content, duration, gravity, repeat)
}

inline fun BaseViewModel.showToast(
    content: String?,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.CENTER,
    repeat: Boolean = false
) {
    show(content, duration, gravity, repeat)
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
@PublishedApi
internal fun show(
    content: CharSequence?,
    duration: Int = Toast.LENGTH_SHORT,
    gravity: Int = Gravity.CENTER,
    repeat: Boolean = false
) {
    if (content.isNullOrEmpty()) {
        return
    }
    if (repeat) {
        mToast = Toast.makeText(Utils.getApp(), content, duration)
    } else {
        mToast = mToast ?: Toast.makeText(Utils.getApp(), null, duration)
        if (oldContent == content) {
            val d = if (duration == Toast.LENGTH_SHORT) 2000 else 3500
            if (System.currentTimeMillis() - oldTime < d) {
                return
            }
        }
    }
    mToast?.setText(content)
    oldContent = content
    oldTime = System.currentTimeMillis()
    mToast?.setGravity(gravity, 0, 0)
    mToast?.show()

}