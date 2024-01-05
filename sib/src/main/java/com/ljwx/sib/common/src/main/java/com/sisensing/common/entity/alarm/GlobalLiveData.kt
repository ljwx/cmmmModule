package com.sisensing.common.entity.alarm

import androidx.lifecycle.MutableLiveData
import com.sisensing.event.SingleLiveEvent

object GlobalLiveData {

    val eventChange = MutableLiveData<Boolean>()

    val scanCodeFail = SingleLiveEvent<Boolean>()

}