package com.sisensing.common.glucose

import com.blankj.utilcode.util.TimeUtils
import com.sisensing.common.constants.DailyTrendConst
import com.sisensing.common.database.AppDatabase
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity
import com.sisensing.common.entity.Device.DeviceGlucoseEntity
import com.sisensing.common.entity.Device.RemoteGlucoseBean
import com.sisensing.common.user.UserInfoUtils
import com.sisensing.common.utils.Log
import com.sisensing.common.utils.isDigit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DBGlucoseUtils {

    private var saveDataJob: Job? = null

    fun getDBRangeData(
        userId: String?,
        deviceName: String?,
        startMillis: Long,
        endMillis: Long,
        resultCall: (MutableList<BloodGlucoseEntity>) -> Unit,
    ) {
        GlobalScope.launch(Dispatchers.Default) {
            getRangeData(userId, deviceName, startMillis, endMillis, resultCall)
        }
    }

    /**
     * 获取时间段数据
     */
    private fun getRangeData(
        userId: String?,
        deviceName: String?,
        startMillis: Long,
        endMillis: Long,
        resultCall: (MutableList<BloodGlucoseEntity>) -> Unit,
    ) {
        val range = TimeUtils.millis2String(startMillis) + "-" + TimeUtils.millis2String(endMillis)
        Log.d("AGP,时间段数据", "获取时间段数据:$range")
        var remote =
            AppDatabase.getInstance().deviceGlucoseEntityDao.getRangeValidBloodGlucoseSuspend(
                userId, startMillis, endMillis
            )
        val result = mutableListOf<BloodGlucoseEntity>()
        val count = AppDatabase.getInstance().deviceGlucoseEntityDao.getCount(userId)
        Log.d("AGP", "远程数据库总量$count")
        var local: List<BloodGlucoseEntity> = emptyList()
        if (remote.isNullOrEmpty()) {
            //远程数据为空,直接取本地数据
            local = AppDatabase.getInstance().bloodEntityDao.getRangeValidBloodGlucoseSuspend(
                userId, deviceName, startMillis, endMillis
            )
        } else {
            val start = TimeUtils.millis2String(remote.first().processedTimeMill)
            val end = TimeUtils.millis2String(remote.last().processedTimeMill)
            Log.d("AGP,时间段数据", "远程时间段数据:$start - $end" + ",数据大小:" + remote.size)
            result.addAll(remote)
            val localStart = remote.last().processedTimeMill
            //远程数据缺大于五分钟,则取本地数据
            if (localStart < (endMillis + DailyTrendConst.ONE_MINUTE_MILLIS * 5)) {
                val localEnd = endMillis
                local = AppDatabase.getInstance().bloodEntityDao.getRangeValidBloodGlucoseSuspend(
                    userId, deviceName, localStart, localEnd
                )
            }
        }
        if (!local.isNullOrEmpty()) {
            val start = TimeUtils.millis2String(local.first().processedTimeMill)
            val end = TimeUtils.millis2String(local.last().processedTimeMill)
            Log.d("AGP,时间段数据", "补加本地时间段血糖:$start - $end" + ",数据大小:" + local.size)
            result.addAll(local)
        }
        result.sortBy { it.processedTimeMill }
        resultCall.invoke(result)
//        checkDataEnough(userId, deviceName, startMillis, endMillis)
    }

    /**
     * 最早数据
     */
    suspend fun getFirstData(userId: String?, deviceName: String?): BloodGlucoseEntity? {
        val remote = getRemoteFirstData(userId)
        val local = getLocalFirstData(userId, deviceName)
        if (remote == null && local == null) {
            Log.d("AGP:最早", "本地,远程,数据库都为空")
            return null
        }
        if (remote == null) {
            Log.d("AGP:最早", "本地最早,${TimeUtils.millis2String(local!!.processedTimeMill)}")
            return local
        }
        if (local == null) {
            Log.d("AGP:最早", "远程最早,${TimeUtils.millis2String(remote!!.processedTimeMill)}")
            return remote
        }
        if ((remote?.processedTimeMill ?: 0) < (local?.processedTimeMill ?: 0)) {
            Log.d("AGP:最早", "远程最早,${TimeUtils.millis2String(remote!!.processedTimeMill)}")
            return remote
        } else {
            Log.d("AGP:最早", "本地最早,${TimeUtils.millis2String(local!!.processedTimeMill)}")
            return local
        }
    }

    /**
     * 最新数据
     */
    suspend fun getLastData(userId: String?, deviceName: String?): BloodGlucoseEntity? {
        val remote = getRemoteLastData(userId)
        val local = getLocalLastData(userId, deviceName)
        if (remote == null && local == null) {
            Log.d("AGP:最晚", "本地,远程,数据库都为空")
            return null
        }
        if (remote == null) {
            Log.d("AGP:最晚", "本地最晚,${TimeUtils.millis2String(local!!.processedTimeMill)}")
            return local
        }
        if (local == null) {
            Log.d("AGP:最晚", "远程最晚,${TimeUtils.millis2String(remote!!.processedTimeMill)}")
            return remote
        }
        if ((remote?.processedTimeMill ?: 0) > (local?.processedTimeMill ?: 0)) {
            Log.d("AGP:最晚", "远程最晚,${TimeUtils.millis2String(remote!!.processedTimeMill)}")
            return remote
        } else {
            Log.d("AGP:最早", "本地最晚,${TimeUtils.millis2String(local!!.processedTimeMill)}")
            return local
        }
    }

    /**
     * 远程最早数据
     */
    private suspend fun getRemoteFirstData(userId: String?): BloodGlucoseEntity? {
        return AppDatabase.getInstance().deviceGlucoseEntityDao.getFirstBloodGlucoseSuspend(
            userId
        )
    }

    /**
     * 本地最早数据
     */
    private suspend fun getLocalFirstData(
        userId: String?,
        deviceName: String?
    ): BloodGlucoseEntity? {
        return AppDatabase.getInstance().bloodEntityDao.getFirstBloodGlucoseSuspend(
            userId, deviceName
        )
    }

    /**
     * 远程最新数据
     */
    private suspend fun getRemoteLastData(userId: String?): BloodGlucoseEntity? {
        return AppDatabase.getInstance().deviceGlucoseEntityDao.getLastBloodGlucoseSuspend(
            userId
        )
    }

    /**
     * 本地最新数据
     */
    private suspend fun getLocalLastData(
        userId: String?,
        deviceName: String?
    ): BloodGlucoseEntity? {
        return AppDatabase.getInstance().bloodEntityDao.getLastValidBloodGlucoseSuspend(
            userId, deviceName
        )
    }

    /**
     * 将远程血糖数据保存到数据库
     */
    fun saveRemoteDatabase(
        result: List<RemoteGlucoseBean>?,
        call: () -> Unit
    ) {
        if (result.isNullOrEmpty()) {
            Log.d("AGP", "从远程拉取的血糖数据是空的,不做操作")
        } else {
            try {
                val startDate = TimeUtils.millis2Date((result.first().t ?: "0").toLong())
                val start = TimeUtils.date2String(startDate)
                val endDate = TimeUtils.millis2Date((result.last().t ?: "0").toLong())
                val end = TimeUtils.date2String(endDate)
                val range = "$start-$end"
                Log.d("AGP", "从服务端拉取的数据大小:" + result.size + "-起始时间:" + range)
            } catch (e: Exception) {
            }
            GlobalScope.launch(Dispatchers.IO) {
                executeSaveData(result, call)
            }
        }
    }

    private suspend fun executeSaveData(result: List<RemoteGlucoseBean>?, call: () -> Unit) {
        saveDataJob?.cancel()
        saveDataJob = GlobalScope.launch(Dispatchers.IO) {
            val userId = UserInfoUtils.getUserId()
            val dao = AppDatabase.getInstance().deviceGlucoseEntityDao
            Log.d("AGP", "远程数据有更新,清空远程数据库")
            dao.deleteUserData(userId)
            result?.forEach {
                ensureActive()
                dao.insert(remoteTransLocal(userId, it))
            }
            withContext(Dispatchers.Main) {
                Log.d("AGP", "远程数据有更新,数据全部存入数据库后的回调,")
                call.invoke()
            }
        }
        saveDataJob?.join()
    }

    /**
     * 远程数据转换
     */
    private fun remoteTransLocal(userId: String?, remote: RemoteGlucoseBean): DeviceGlucoseEntity {
        val entity = DeviceGlucoseEntity()
        entity.glucoseValue = remote.v ?: 0f
        entity.index = remote.i ?: 0
        entity.userId = userId
        entity.glucoseTrend = remote.s ?: 0
        entity.alarmStatus = remote.ast ?: 0
        if (remote.t.isDigit()) {
            entity.processedTimeMill = remote.t!!.toLong();
        }
        return entity
    }

}