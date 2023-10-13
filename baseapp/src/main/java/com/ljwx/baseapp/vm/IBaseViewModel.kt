package com.ljwx.baseapp.vm

import com.ljwx.baseapp.response.DataResult

interface IBaseViewModel<M> {

    fun createRepository(): M

    fun commonResponseNotSuccess(result: DataResult<*>)

}