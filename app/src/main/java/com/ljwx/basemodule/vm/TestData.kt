package com.ljwx.basemodule.vm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class TestData(val code: Int) : Parcelable {
    var type: Int = 0
}