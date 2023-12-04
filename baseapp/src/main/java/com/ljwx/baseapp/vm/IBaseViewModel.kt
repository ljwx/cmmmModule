package com.ljwx.baseapp.vm

import androidx.annotation.StringRes
import com.ljwx.baseapp.response.DataResult

interface IBaseViewModel<M> {

    fun createRepository(): M

    fun commonResponseNotSuccess(result: DataResult<*>)

    fun getString(@StringRes string: Int)

    fun finishActivity(finish: Boolean = true)
}