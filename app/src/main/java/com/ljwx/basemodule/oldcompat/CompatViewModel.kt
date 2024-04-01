package com.ljwx.basemodule.oldcompat

import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.basenetwork.retrofit.test.TestRepository

class CompatViewModel: BaseViewModel<TestRepository>() {
    override fun createRepository(): TestRepository {
        return TestRepository()
    }



}