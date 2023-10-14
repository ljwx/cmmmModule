package com.ljwx.provideacountinput

import androidx.annotation.IntDef

/**
 * Data validation state of the login form.
 */
data class AccountInputStateData(

    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false


) {
    companion object {

        const val EMPTY_ALL = 0x01
        const val EMPTY_ACCOUNT = 0x02
        const val EMPTY_PASSWORD = 0x04
        const val EMPTY_VERIFY_CODE = 0x08
        const val EMPTY_CONFIRM_PASSWORD = 0x10

        @IntDef(EMPTY_ALL, EMPTY_ACCOUNT, EMPTY_PASSWORD, EMPTY_VERIFY_CODE, EMPTY_CONFIRM_PASSWORD)
        @Retention(AnnotationRetention.SOURCE)
        annotation class AccountInputEmpty

        const val INVALID_ACCOUNT = 0x20
        const val INVALID_PASSWORD = 0x40
        const val INVALID_VERIFY_CODE = 0x80
        const val INVALID_CONFIRM_PASSWORD = 0x100

        @IntDef(INVALID_ACCOUNT, INVALID_PASSWORD, INVALID_VERIFY_CODE, INVALID_CONFIRM_PASSWORD)
        @Retention(AnnotationRetention.SOURCE)
        annotation class AccountInputInvalid

        const val ALL_PASS = 0x00
    }

}