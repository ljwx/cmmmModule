package com.ljwx.basemodule.vm

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ljwx.baseapp.response.DataResult
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.basenetwork.retrofit.test.TestRepository
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

open class TestViewModel : BaseViewModel<TestRepository>() {

    var mResponse = MutableLiveData<String>()

    val mIntervelTest = MutableLiveData<String>()

    override fun createRepository(): TestRepository {
        return TestRepository()
    }

    fun intervalPost() {
        val task = Observable.intervalRange(1, 20, 1, 3, TimeUnit.SECONDS).subscribe {
            mIntervelTest.postValue(it.toString())
        }
        autoClear(task)
    }

    fun requestTest() {
        showPopLoading()
        mRepository.requestTest(object : DataResult.Result<String> {
            override fun onResult(result: DataResult<String>) {
                dismissPopLoading()
                if (result is DataResult.Success) {
                    mResponse.postValue(result.data)
                }
                if (result is DataResult.Error) {
                    val match = result.exception is NotFoundException
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