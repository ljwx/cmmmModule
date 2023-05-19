package com.ljwx.basemodule.vm

import androidx.lifecycle.MutableLiveData
import com.ljwx.baseapp.BaseViewModel

class TestViewModel : BaseViewModel() {

    private var mTestRepository: TestRepository? = null

    val mResponse = MutableLiveData<String>()

    override fun initRepository() {
        mTestRepository = TestRepository()
    }

    fun requestTest() {
        mTestRepository?.requestTest()
    }

}