package com.ljwx.baseapp.response

open class BaseResponse<Data> {

    companion object {

        private var RESPONSE_SUCCESS = 200

        fun setSuccessCode(code: Int) {
            RESPONSE_SUCCESS = code
        }
    }

    var code: Int? = null

    @Deprecated(message = "useless", replaceWith = ReplaceWith(expression = "msg"))
    var message: String? = null

    var msg: String? = null

    var data: Data? = null

    var errorData: Any? = null

    var isRefresh: Boolean? = null

    var extensionField: Any? = null

    open fun isSuccess(): Boolean {
        return code == RESPONSE_SUCCESS
    }

    open fun isSuccessAndDataNotNull(): Boolean {
        return isSuccess() && data != null
    }

}