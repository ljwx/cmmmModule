package com.ljwx.basenetwork

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


fun Map<String, Any?>.toJson(): RequestBody {
    return this.toString()
        .toRequestBody("Content-Type, application/json".toMediaTypeOrNull())
}