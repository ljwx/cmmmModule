package com.sisensing.common.entity.guardianship

/**
 * @ProjectName: 硅基动感
 * @Package: com.sisensing.common.entity.guardianship
 * @ClassName: RelativesFollowInfoEntity
 * @Description: 获取关注的用户的详细信息
 * @Author: xy
 * @Date: 2023/3/13 17:02
 */
data class RelativesFollowInfoEntity(
    val followType: Int? = null,
    val followedUserId: String? = null,
    val followedUserInfo: FollowedUserInfo? = null,
    val friendCancelFollow: Boolean? = false,
    val id: String? = null,
    val otherInfo: OtherInfo? = null,
    val permissions: Permissions? = null,
    val status: Int? = null,
    val userId: String? = null
) {
    data class FollowedUserInfo(
        val avatar: String? = null,
        val drType: Int,
        val nickName: String? = null,
        val userName: String? = null
    )

    data class OtherInfo(
        val dataSyncEnable: Boolean? = null,
        val dataSyncNoticeRate: Int,
        val dataSyncNoticeSound: String? = null,
        val dataWarnLower: String? = null,
        val dataWarnUpper: String? = null,
        val datawarnEnable: Boolean? = null,
        val deviceStatusEnable: Boolean? = null,
        val followedRemark: String? = null,
        val mobilePush: Boolean? = null,
        val noticeRate: Int? = null,
        val warnWayOfficialAccount: Boolean? = null
    )

    data class Permissions(
        val action: Boolean? = null,
        val reportHis: Boolean? = null,
        val rtGlucose: Boolean? = null
    )
}