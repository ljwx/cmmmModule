package com.ljwx.basemodule.vm

import androidx.lifecycle.MutableLiveData
import com.ljwx.baseapp.vm.empty.EmptyViewModel

class UserInfoVM : EmptyViewModel() {

    val userName = MutableLiveData<String>()



}