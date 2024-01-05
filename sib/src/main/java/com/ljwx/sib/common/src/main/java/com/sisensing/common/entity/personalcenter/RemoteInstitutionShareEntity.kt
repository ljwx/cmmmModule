package com.sisensing.common.entity.personalcenter

import androidx.core.text.isDigitsOnly
import com.blankj.utilcode.util.TimeUtils
import com.sisensing.common.utils.isDigit

data class RemoteInstitutionShareEntity(
    val createTime: String?,//创建时间
    val id: String?,
    val orgId: String?,
    val orgName: String?,
    val status: Int?, //0：待处理，1：已同意，2：已拒绝，3：已解除，4：已过期
    val shareTime: String?, //当前共享机构,分享时间
    val patientId: String?, //当前共享机构,患者ID
) {
    companion object {
        const val PENDING = 0
        const val AGREED = 1
        const val REMOVED = 3
        const val DECLINED = 2
        const val EXPIRED = 4
    }

    val isLast: Boolean
        get() {
            var last = true
            if (!createTime.isNullOrBlank() && createTime.isDigitsOnly()) {
                val diff = 3600 * 1000 * 24 * 7
                val serverTime = createTime.toLong()
                val now = TimeUtils.getNowMills()
                return now < serverTime + diff
            }
            return last
        }

}