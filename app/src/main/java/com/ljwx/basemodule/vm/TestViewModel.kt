package com.ljwx.basemodule.vm

import android.content.res.Resources.NotFoundException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ljwx.baseapp.response.DataResult
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.basenetwork.retrofit.test.TestRepository

class TestViewModel : BaseViewModel<TestRepository>() {

    var mResponse = MutableLiveData<String>()

    override fun createRepository(): TestRepository {
        return TestRepository()
    }

    fun requestTest() {
        mRepository.requestTest(object :DataResult.Result<String>{
            override fun call(result: DataResult<String>) {
                if (result is DataResult.Success) {
                    mResponse.postValue(result.data)
                    Log.d("ljwx2,sadfasdf", result.data)
                }
                if (result is DataResult.Error) {
                    val match = result.exception is NotFoundException
                    Log.d("ljwx2,类型", match.toString())
                }
            }
        })
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