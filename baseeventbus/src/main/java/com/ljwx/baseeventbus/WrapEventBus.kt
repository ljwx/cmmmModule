package com.ljwx.baseeventbus

import com.kunminx.architecture.ui.callback.UnPeekLiveData

class WrapEventBus {

    fun test(){
        val a = UnPeekLiveData<String>()

        FlowEventBus.post("")
    }

}