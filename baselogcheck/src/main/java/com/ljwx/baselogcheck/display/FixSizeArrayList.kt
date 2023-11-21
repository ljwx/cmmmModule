package com.ljwx.baselogcheck.display

import kotlin.collections.ArrayList

class FixSizeArrayList<T>(private val length:Int) : ArrayList<T>() {

    override fun add(element: T): Boolean {

        if (size + 1 > length) {
            super.removeAt(0)
        }
        return super.add(element)
    }

}