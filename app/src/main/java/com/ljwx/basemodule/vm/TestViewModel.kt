package com.ljwx.basemodule.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.basenetwork.retrofit.test.TestRepository

class TestViewModel : BaseViewModel<TestRepository>() {

    val mResponse = MutableLiveData<String>()

    override fun createRepository(): TestRepository {
        return TestRepository()
    }

    fun requestTest() {
        mRepository.requestTest()
    }

    //https://developer.android.com/topic/libraries/architecture/viewmodel?hl=zh-cn
    private val users: MutableLiveData<List<TestData>> by lazy {
        MutableLiveData<List<TestData>>().also {
            loadUsers()
        }
    }

    fun getUsers(): LiveData<List<TestData>> {
        return users
    }

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

}