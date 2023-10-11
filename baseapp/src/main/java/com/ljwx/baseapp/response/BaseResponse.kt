package com.ljwx.baseapp.response

open class BaseResponse<Data> : IBaseResponse<Data> {

    companion object {

        private var RESPONSE_SUCCESS = 200

        fun setSuccessCode(code: Int) {
            RESPONSE_SUCCESS = code
        }
    }

    open var code: Int? = null

    @Deprecated(message = "useless", replaceWith = ReplaceWith(expression = "msg"))
    open var message: String? = null

    open var msg: String? = null

    open var data: Data? = null

    open var isRefresh: Boolean? = null

    open var extensionField: Any? = null

    override fun isSuccess(): Boolean {
        return code == RESPONSE_SUCCESS
    }

    open fun isSuccessAndDataNotNull(): Boolean {
        return isSuccess() && data != null
    }

}