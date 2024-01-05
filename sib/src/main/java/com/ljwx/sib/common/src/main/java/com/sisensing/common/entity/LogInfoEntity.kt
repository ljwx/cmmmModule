package com.sisensing.common.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @ClassName DeviceLogInfoEntity
 * @Description 记录日志信息的本地数据库
 * @Author xieyang
 * @Date 2023/8/4 14:22
 */
@Entity
data class LogInfoEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var timestamp: Long = 0L,
    var logInfo: String?=null
)