package com.sisensing.common.entity

import okhttp3.RequestBody

/**
 * @ClassName DeviceLogWithTimestamp
 * @Description
 * @Author xieyang
 * @Date 2023/8/4 16:21
 */
data class LogInfoRequest(val logInfoList:List<LogInfoEntity>, val requestBody: RequestBody)
