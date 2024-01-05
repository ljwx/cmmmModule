package com.sisensing.common.utils

import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject


object ApiRequestUtils {

    fun map2RequestBody(map: Map<String, Any?>): RequestBody {
        return RequestBody.create(
            MediaType.parse("application/json"),
            JSONObject(map).toString()
        )
    }

}