package com.ljwx.basemodule.second

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.ljwx.baseapp.vm.BaseSaveStateViewModel
import com.ljwx.baseapp.vm.empty.EmptyDataRepository

class SecondViewModel(savedStateHandle: SavedStateHandle) : BaseSaveStateViewModel<EmptyDataRepository>(savedStateHandle) {
    override fun createRepository(): EmptyDataRepository {
        return EmptyDataRepository()
    }

    val saveStateTest = savedStateHandle.getLiveData<String>("test")

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Log.d("ljwx2", "saveStateHandle测试值:"+saveStateTest.value)
    }

}