package com.sisensing.common.net.model

import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ObjectUtils
import com.ljwx.baseapp.response.DataResult
import com.sisensing.base.BaseViewModel
import com.sisensing.common.base.ResponseListenerSimple
import com.sisensing.common.base.RetrofitUtils
import com.sisensing.common.constants.GeneralModel
import com.sisensing.common.database.AppDatabase
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity
import com.sisensing.common.entity.Device.DeviceEntity
import com.sisensing.common.entity.Device.DeviceRepository
import com.sisensing.common.entity.QueryDeviceEntity
import com.sisensing.common.entity.personalcenter.SharerDeviceEntity
import com.sisensing.common.net.DeviceApiService
import com.sisensing.common.user.UserInfoUtils
import com.sisensing.common.utils.StreamFileUtils
import io.reactivex.functions.Consumer
import okhttp3.ResponseBody

open class DeviceDataModel : GeneralModel {

    val deviceApiService by lazy {
        RetrofitUtils.getInstance().getRequest(DeviceApiService::class.java)
    }

    constructor() : super()

    constructor(baseViewModel: BaseViewModel<*>) : super(baseViewModel)

    /**
     * 从服务器获取设备列表,并保存到服务其
     */
    fun getDeviceListAndSaveDB(userId: String) {
        val time = System.currentTimeMillis() - 90 * 24 * 60 * 60 * 1000L
        requestFromServerNew(
            deviceApiService.queryDevice(1, 100, time),
            true,
            object : ResponseListenerSimple<QueryDeviceEntity>() {
                override fun onSuccess(data: QueryDeviceEntity?, msg: String?) {
                    saveDevicesToDB(data, userId)
                }

            })
    }

    /**
     * 从服务器获取设备列表
     */
    fun getDeviceListFromServer(listener: ResponseListenerSimple<QueryDeviceEntity>) {
        val time = System.currentTimeMillis() - 90 * 24 * 60 * 60 * 1000L
        requestFromServerNew(deviceApiService.queryDevice(1, 100, time), true, listener)
    }

    /**
     * 保存设备列表到数据库
     */
    private fun saveDevicesToDB(data: QueryDeviceEntity?, userId: String?) {
        val records = data?.records
        if (records.isNullOrEmpty()) {
            return
        }
        DeviceRepository.getInstance().queryUserAllDevice(userId) { deviceEntities ->
            if (ObjectUtils.isEmpty(deviceEntities)) {
                serverDeviceData2DBData(records, null)
            } else {
                for (i in records.indices) {
                    val recordsDTO = records[i]
                    for (deviceEntity in deviceEntities!!) {
                        if (recordsDTO!!.name == deviceEntity!!.deviceName) {
                            if (ObjectUtils.isNotEmpty(recordsDTO!!.algorithmVersion)) {
                                deviceEntity!!.algorithmVersion =
                                    recordsDTO!!.algorithmVersion
                                DeviceRepository.getInstance().update(deviceEntity)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 将服务端数据转换为列表数据
     */
    fun serverDeviceData2DBData(
        records: List<QueryDeviceEntity.RecordsDTO>?,
        callback: ((ArrayList<DeviceEntity>) -> Unit)? = null
    ) {
        val dbRecords = ArrayList<DeviceEntity>()
        val userId = UserInfoUtils.getUserId()
        records?.forEachIndexed { index, recordsDTO ->
            val deviceEntity = DeviceEntity()
            AppDatabase.getInstance().deviceDao.findDeviceByDeviceId(userId, recordsDTO.id)
                .subscribe { t ->
                    if (ObjectUtils.isEmpty(t)) {
                        deviceEntity.deviceName = recordsDTO.name
                        deviceEntity.macAddress = recordsDTO.macAddress
                        deviceEntity.algorithmVersion = recordsDTO.algorithmVersion
                        deviceEntity.deviceStatus = recordsDTO.status
                        deviceEntity.blueToothNum = recordsDTO.blueToothNum
                        deviceEntity.userId = recordsDTO.userId
                        deviceEntity.deviceId = recordsDTO.id
                        if (ObjectUtils.isNotEmpty(recordsDTO.enableTime)) {
                            //第一笔激活时间
                            deviceEntity.firstBsMill = recordsDTO!!.enableTime
                        }
                        dbRecords.add(deviceEntity)
                        DeviceRepository.getInstance().insert(deviceEntity)
                    } else {
                        dbRecords.add(t.first())
                    }
                }
            if (index == records.lastIndex) {
                callback?.invoke(dbRecords)
            }
        }
    }

    /**
     * 获取设备远程血糖数据
     */
    fun getDeviceGlucoseData(
        deviceId: String,
        listener: ResponseListenerSimple<List<SharerDeviceEntity.GlucoseInfo>>
    ) {
        val map = HashMap<String, Any>()
        map["deviceId"] = deviceId
//        map["startDt"] = ""
//        map["endDt"] = ""
        //是否只查询有效数据(默认五分钟一笔)
//        map["validFlag"] = ""
        //是否开启分页(默认不开启)
//        map["startPage"] = "1000"
//        map["pageNum"] = "1"
//        map["pageSize"] = "1000"
        requestFromServerNew(
            deviceApiService.getDeviceGlucoseData(getRequestBody(map)),
            true,
            listener
        )
    }

    /**
     * 远程血糖数据转换
     */
    fun serverGlucoseData2DBData(data: List<SharerDeviceEntity.GlucoseInfo>?): ArrayList<BloodGlucoseEntity> {
        val dbGlucose = ArrayList<BloodGlucoseEntity>()
        data?.forEach {
            val entity = BloodGlucoseEntity()
            entity.index = it.i
            entity.processedTimeMill = it.t
            entity.glucoseValue = it.v
            entity.alarmStatus = it.ast
            dbGlucose.add(entity)
        }
        return dbGlucose
    }

    /**
     * 获取远程血糖excel
     */
    fun getDeviceGlucoseExcel(
        deviceId: String?,
        filePath: String,
        listener: ResponseListenerSimple<Any?>
    ) {
        val map = HashMap<String, Any?>()
        map["deviceId"] = deviceId
//        map["startDt"] = ""
//        map["endDt"] = ""
        //是否只查询有效数据(默认五分钟一笔)
//        map["validFlag"] = ""
        //是否开启分页(默认不开启)
//        map["startPage"] = "1000"
//        map["pageNum"] = "1"
//        map["pageSize"] = "1000"
        val disposable = deviceApiService.getDeviceGlucoseExcel(getRequestBody(map))
            .subscribe(object : QuickObserver<ResponseBody>() {

                override fun onError(e: Throwable) {
                    super.onError(e)
                    listener.onFail(0, "", "")
                }

                override fun onResponse(response: ResponseBody) {
                    if (FileUtils.createOrExistsFile(filePath)) {
                        if (StreamFileUtils.toFile(response, filePath)) {
                            listener.onSuccess("", "")
                        } else {
                            listener.onFail(0, "", "")
                        }
                    }
                }

                override fun onResponseSuccess(dataResult: DataResult.Success<ResponseBody>) {

                }

            })
    }

}