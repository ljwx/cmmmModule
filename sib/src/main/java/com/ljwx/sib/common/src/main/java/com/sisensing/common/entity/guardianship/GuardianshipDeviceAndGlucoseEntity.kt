package com.sisensing.common.entity.guardianship

/**
 * @ProjectName: 硅基动感
 * @Package: com.sisensing.common.entity.guardianship
 * @ClassName: GuardianshipSelfDevice
 * @Description: 监护模式设备和血糖数据
 * @Author: xy
 * @Date: 2023/3/3 15:35
 */
data class GuardianshipDeviceAndGlucoseEntity(
    val glucoseDataList: List<GlucoseData?>? = null,
    val userFingerBloodVO: UserFingerBloodVO? = null
) {
    data class GlucoseData(
        val bloodGlucoseTrend: Int = 0,
        val dailyData: List<DailyData?>? = null,
        val deviceAlarmStatus: Int? = null,
        val deviceEnableTime: Long? = null,
        val deviceId: String? = null,
        val deviceLastTime: Long = 0,
        val deviceName: String? = null,
        val deviceStatus: Int? = null,
        val glucoseInfos: List<GlucoseInfo?>? = null,
        val latestGlucoseTime: Long= 0,
        val latestGlucoseValue: String? = null,
        val target: Target? = null,
        val userId: String? = null
    ) {
        data class DailyData(
            val `data`: List<Data?>? = null,
            val dateTime:Long = 0,
            val tirScale: String? = null
        ) {
            data class Data(
                val effective: Boolean,
                val t: Long,
                val v: String? = null
            )
        }

        data class GlucoseInfo(
            val ast: Int? = null,
            val bl: String? = null,
            val effective: Boolean,
            val i: Int? = null,
            val s: Int? = null,
            val t: Long,
            val v: Float
        ):Comparable<GlucoseInfo> {
            override fun compareTo(other: GlucoseInfo): Int {
                return v.compareTo(other.v)
            }
        }

        data class Target(
            val lower_1: String? = null,
            val lower_2: String? = null,
            val upper_1: String? = null,
            val upper_2: String? = null
        )
    }

    data class UserFingerBloodVO(
        val actionStartTime: Long = 0,
        val fingerBlood: String? = null,
        val sceneType: Int? = null,
        val userId: String? = null
    )
}