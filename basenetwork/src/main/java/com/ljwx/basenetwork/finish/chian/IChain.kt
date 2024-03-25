package com.ljwx.basenetwork.finish.chian

import com.ljwx.basenetwork.finish.IResponse
import com.ljwx.basenetwork.finish.request.IRequest
import java.io.IOException

interface IChain {

    fun getRequestInfo(): IRequest

    @Throws(IOException::class)
    fun process(request: IRequest): IResponse

}