package com.ljwx.basemodule.vm

import androidx.lifecycle.MutableLiveData
import com.ljwx.baseapp.vm.BaseViewModel

class TestViewModel : BaseViewModel() {

    private var mTestRepository: TestRepositoryModel? = null

    val mResponse = MutableLiveData<String>()

    override fun initRepository() {
        mTestRepository = TestRepositoryModel()
    }

    fun requestTest() {
        mTestRepository?.requestTest()
    }

}