package com.sisensing.common.entity.privacy

import com.sisensing.common.constants.ConstTypeValue

class CheckUserPrivacyEntity(
    val privacyPolicyVersionUpdate: Boolean?,
    val userAgreementVersionUpdate: Boolean?
) {
    fun getUpdateType(): Int {
        if (userAgreementVersionUpdate == true && privacyPolicyVersionUpdate == true) {
            return ConstTypeValue.USER_ALL_UPDATE
        }
        if (userAgreementVersionUpdate == true) {
            return ConstTypeValue.USER_AGREEMENT_UPDATE
        }
        if (privacyPolicyVersionUpdate == true) {
            return ConstTypeValue.USER_PRIVACY_UPDATE
        }
        return -1
    }
}