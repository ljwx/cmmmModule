package com.ljwx.baselogcheck.display

import androidx.annotation.RestrictTo
import java.util.Vector

/**
 * 线程安全的固定大小集合
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class FixSizeVector<T>(private val length:Int) : Vector<T>() {

    override fun add(element: T): Boolean {

        if (size + 1 > length) {
            super.removeAt(0)
        }
        return super.add(element)
    }

}