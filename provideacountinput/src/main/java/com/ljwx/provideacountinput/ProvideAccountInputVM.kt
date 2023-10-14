package com.ljwx.provideacountinput

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ljwx.baseapp.vm.BaseViewModel
import com.ljwx.baseapp.vm.model.BaseDataRepository

abstract class ProvideAccountInputVM<R : BaseDataRepository<*>> : BaseViewModel<R>(),
    IProvideAccountInputVM {

    private val _dataState = MutableLiveData<Int>()
    val dataState: LiveData<Int> = _dataState

    override fun accountDataChange(account: String, password: String) {

        if (account.isBlank() && password.isBlank()) {
            _dataState.value = AccountInputStateData.EMPTY_ALL
        } else {
            val sAccount = if (account.isBlank()) AccountInputStateData.EMPTY_ACCOUNT else 0x00
            val sPassword = if (password.isBlank()) AccountInputStateData.EMPTY_PASSWORD else 0x00
            _dataState.value = sAccount or sPassword
        }
    }

    override fun accountDataChange(
        account: String,
        verifyCode: String,
        password: String,
        confirmPassword: String
    ) {
        if (account.isBlank() && password.isBlank() && verifyCode.isBlank() && confirmPassword.isBlank()) {
            _dataState.value = AccountInputStateData.EMPTY_ALL
        } else {
            val sAccount = if (account.isBlank()) AccountInputStateData.EMPTY_ACCOUNT else 0x00
            val sVerifyCode =
                if (verifyCode.isBlank()) AccountInputStateData.EMPTY_VERIFY_CODE else 0x00
            val sPassword = if (password.isBlank()) AccountInputStateData.EMPTY_PASSWORD else 0x00
            val sConfirm =
                if (confirmPassword.isBlank()) AccountInputStateData.EMPTY_CONFIRM_PASSWORD else 0x00
            _dataState.value = sAccount or sVerifyCode or sPassword or sConfirm
        }
    }

    fun verifyCodeIsEmpty(result: Int): Boolean {
        return (result and AccountInputStateData.EMPTY_VERIFY_CODE) != 0
    }

    fun confirmPasswordInvalid() {

    }

}