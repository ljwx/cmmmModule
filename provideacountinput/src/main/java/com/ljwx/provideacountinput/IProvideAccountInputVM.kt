package com.ljwx.provideacountinput

interface IProvideAccountInputVM {

    fun accountDataChange(account: String, password: String)

    fun accountDataChange(
        account: String,
        verifyCode: String,
        password: String,
        confirmPassword: String
    )

}