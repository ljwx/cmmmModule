package com.ljwx.basenetwork.finish

import com.ljwx.basenetwork.finish.chian.IChain
import java.io.IOException

interface INetInterceptor {

    @Throws(IOException::class)
    fun interceptor(chain: IChain):IResponse

}