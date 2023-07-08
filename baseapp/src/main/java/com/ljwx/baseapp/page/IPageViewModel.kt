package com.ljwx.baseapp.page

import com.ljwx.baseapp.vm.BaseViewModel

interface IPageViewModel {

    fun <VM : BaseViewModel<*>> createViewModel(): VM

}