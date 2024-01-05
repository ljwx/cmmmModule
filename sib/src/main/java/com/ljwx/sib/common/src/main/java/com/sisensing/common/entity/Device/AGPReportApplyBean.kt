package com.sisensing.common.entity.Device

data class AGPReportApplyBean(
    val ossUrl: String?,//报告OSS地址
    val pdfHasCreate: Int?, //0:未创建，1：已创建，2：生成失败
    val taskId: Long?,//任务ID
    val failCode:Int?//错误码
) {

    companion object{
        const val INSUFFICIENT_DATA = 204003
        const val NET_ERROR = -2
        const val DOWNLOAD_FAIL = -3
    }

    fun createSuccess(): Boolean {
        return pdfHasCreate == 1
    }
}