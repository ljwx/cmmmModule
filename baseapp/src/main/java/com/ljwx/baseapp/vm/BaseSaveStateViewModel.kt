package com.ljwx.baseapp.vm

import androidx.lifecycle.SavedStateHandle
import com.ljwx.baseapp.vm.model.BaseDataRepository

abstract class BaseSaveStateViewModel<M : BaseDataRepository<*>>(savedStateHandle: SavedStateHandle) : BaseViewModel<M>() {

    protected val example = savedStateHandle.getLiveData<String>("example")

}