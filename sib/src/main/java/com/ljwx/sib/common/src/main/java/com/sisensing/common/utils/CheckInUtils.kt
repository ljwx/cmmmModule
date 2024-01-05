package com.sisensing.common.utils

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.Utils
import com.ljwx.baseapp.extensions.isFloat
import com.ljwx.baseapp.extensions.isInt
import com.ljwx.baseapp.extensions.notNullOrBlank
import com.sisensing.common.R
import com.sisensing.common.constants.Constant
import com.sisensing.common.database.AppDatabase
import com.sisensing.common.database.RoomResponseListener
import com.sisensing.common.entity.actionRecord.ActionRecordEntity
import com.sisensing.common.entity.actionRecord.ActionRecordEnum
import com.sisensing.common.entity.clcok.CheckInData
import com.sisensing.common.entity.personalcenter.LifeEventEntity
import com.sisensing.common.router.RouterActivityPath
import com.sisensing.common.user.UserInfoUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.UUID

object CheckInUtils {

    /**
     * 服务端数据转数据库
     */
    fun mergeMemoryServerData(
        checkInData: CheckInData,
        serverData: LifeEventEntity.RecordsDTO,
        type: Int
    ): ActionRecordEntity {
        val entity = ActionRecordEntity()
        when (type) {
            CheckInData.CHECKIN_MEAL -> {
                entity.startTime = checkInData.startTimeMill
                entity.eventConsume = checkInData.firstInputText.get()
                entity.eventDetail = checkInData.secondInputText.get()
                entity.actionImgs = checkInData.getAllPictures().values.toList()
            }

            CheckInData.CHECKIN_SPORTS -> {
                entity.startTime = checkInData.startTimeMill
                entity.endTime = checkInData.endTimeMill
                entity.eventDetail = checkInData.firstInputText.get()
            }

            CheckInData.CHECKIN_SLEEP -> {
                entity.startTime = checkInData.startTimeMill
                entity.endTime = checkInData.endTimeMill
            }

            CheckInData.CHECKID_FINGER_BLOOD -> {
                entity.startTime = checkInData.startTimeMill
                entity.sgUnit = BgUnitUtils.getUserUnitType().toString()
                val serverIsMol = BgUnitUtils.isTypeMol(serverData.actionData.unit)
                val serverValue = checkInData.secondInputText.get()
                if (!serverValue.isNullOrEmpty()) {
                    entity.eventDetail = serverValue
                }
//                if (serverValue.notNullOrBlank() && (serverValue.isInt() || serverValue.isFloat())) {
//                    entity.eventDetail =
//                        GlucoseUtils.getConvertValue(value!!.toFloat(), isMol, BgUnitUtils.isUserMol())
//                }
                Log.d("打卡,指血", "本地无数据,内存转数据库,单位:"+entity.sgUnit+",值:"+entity.eventDetail)
            }

            CheckInData.CHECKIN_MEDICATIONS -> {
                entity.startTime = checkInData.startTimeMill
                entity.eventDetail = checkInData.firstInputText.get()
                entity.eventConsume = checkInData.secondInputText.get()
            }

            CheckInData.CHECKIN_INSULIN -> {
                entity.startTime = checkInData.startTimeMill
                entity.eventDetail = checkInData.firstInputText.get()
                entity.eventConsume = checkInData.secondInputText.get()
            }

            CheckInData.CHECKIN_MOOD -> {
                entity.startTime = checkInData.startTimeMill
                entity.eventDetail = checkInData.firstInputText.get()
            }

        }
        entity.userId = serverData.userId
        entity.type = serverData.type
        entity.name = getNameFromType(serverData.type)
        entity.endTime = serverData.actionEndTime
        entity.dataId = serverData.dataId
        entity.uploadService = 0
        return entity
    }

    /**
     * 根据type获取类型名称
     * 注意获取type对应名字时，使用getName()方法获取。不能直接通过enum.name去获取，否则会调用enum的.name()方法
     */
    private fun getNameFromType(type: Int): String {
        when (type) {
            ActionRecordEnum.FOOD.type -> return ActionRecordEnum.FOOD.getName()
            ActionRecordEnum.SPORTS.type -> return ActionRecordEnum.SPORTS.getName()
            ActionRecordEnum.MEDICATIONS.type -> return ActionRecordEnum.MEDICATIONS.getName()
            ActionRecordEnum.INSULIN.type -> return ActionRecordEnum.INSULIN.getName()
            ActionRecordEnum.SLEEP.type -> return ActionRecordEnum.SLEEP.getName()
            ActionRecordEnum.FINGER_BLOOD.type -> return ActionRecordEnum.FINGER_BLOOD.getName()
            ActionRecordEnum.PHYSICAL_STATE.type -> return ActionRecordEnum.PHYSICAL_STATE.getName()
        }
        return ""
    }

    /**
     * 内存数据转数据库
     */
    fun memoryToDB(memoryData: CheckInData, type: Int): ActionRecordEntity {
        val recordEntity = ActionRecordEntity()
        when (type) {
            CheckInData.CHECKIN_MEAL -> {
                recordEntity.startTime = memoryData.startTimeMill
                recordEntity.eventConsume = memoryData.firstInputText.get()
                recordEntity.eventDetail = memoryData.secondInputText.get()
                recordEntity.actionImgs = memoryData.getAllPictures().values.toList()


                recordEntity.type = ActionRecordEnum.FOOD.type
                recordEntity.name = ActionRecordEnum.FOOD.getName()

            }

            CheckInData.CHECKIN_SPORTS -> {
                recordEntity.startTime = memoryData.startTimeMill
                recordEntity.endTime = memoryData.endTimeMill
                recordEntity.eventDetail = memoryData.firstInputText.get()

                recordEntity.type = ActionRecordEnum.SPORTS.type
                recordEntity.name = ActionRecordEnum.SPORTS.getName()
            }

            CheckInData.CHECKIN_SLEEP -> {
                recordEntity.startTime = memoryData.startTimeMill
                recordEntity.endTime = memoryData.endTimeMill

                recordEntity.type = ActionRecordEnum.SLEEP.type
                recordEntity.name = ActionRecordEnum.SLEEP.getName()
            }

            CheckInData.CHECKID_FINGER_BLOOD -> {
                recordEntity.startTime = memoryData.startTimeMill
                recordEntity.sgUnit = memoryData.firstInputText.get()
                val value = memoryData.secondInputText.get()
                if (value.notNullOrBlank() && (value.isInt() || value.isFloat())) {
                    recordEntity.eventDetail = GlucoseUtils.getConvertValue(
                        value!!.toFloat(),
                        BgUnitUtils.isTypeMol(recordEntity.sgUnit),
                        BgUnitUtils.isUserMol()
                    )
                }
            }

            CheckInData.CHECKIN_MEDICATIONS -> {
                recordEntity.startTime = memoryData.startTimeMill
                recordEntity.eventDetail = memoryData.firstInputText.get()
                recordEntity.eventConsume = memoryData.secondInputText.get()

                recordEntity.type = ActionRecordEnum.MEDICATIONS.type
                recordEntity.name = ActionRecordEnum.MEDICATIONS.getName()

            }

            CheckInData.CHECKIN_INSULIN -> {
                recordEntity.startTime = memoryData.startTimeMill
                recordEntity.eventDetail = memoryData.firstInputText.get()
                recordEntity.eventConsume = memoryData.secondInputText.get()

                recordEntity.type = ActionRecordEnum.INSULIN.type
                recordEntity.name = ActionRecordEnum.INSULIN.getName()

            }

            CheckInData.CHECKIN_MOOD -> {
                recordEntity.startTime = memoryData.startTimeMill
                recordEntity.eventDetail = memoryData.firstInputText.get()

                recordEntity.type = ActionRecordEnum.PHYSICAL_STATE.type
                recordEntity.name = ActionRecordEnum.PHYSICAL_STATE.getName()

            }
        }
        recordEntity.dataId = UUID.randomUUID().toString()
        recordEntity.userId = UserInfoUtils.getUserId()
        recordEntity.uploadService = 0
        return recordEntity
    }

    /**
     * 修改数据库数据
     */
    fun mergeMemoryDBData(
        memoryData: CheckInData,
        db: ActionRecordEntity,
        type: Int
    ): ActionRecordEntity {
        Log.d("打卡修改", "type:$type")
        when (type) {
            CheckInData.CHECKIN_MEAL -> {
                db.startTime = memoryData.startTimeMill
                db.eventConsume = memoryData.firstInputText.get()
                db.eventDetail = memoryData.secondInputText.get()
                db.actionImgs = memoryData.getAllPictures().values.toList()
            }

            CheckInData.CHECKIN_SPORTS -> {
                db.startTime = memoryData.startTimeMill
                db.endTime = memoryData.endTimeMill
                db.eventDetail = memoryData.firstInputText.get()
            }

            CheckInData.CHECKIN_SLEEP -> {
                db.startTime = memoryData.startTimeMill
                db.endTime = memoryData.endTimeMill
            }

            CheckInData.CHECKIN_MEDICATIONS -> {
                db.startTime = memoryData.startTimeMill
                db.eventDetail = memoryData.firstInputText.get()
                Log.d("打卡修改", "修改detail:" + db.eventDetail)
                db.eventConsume = memoryData.secondInputText.get()
            }

            CheckInData.CHECKIN_INSULIN -> {
                db.startTime = memoryData.startTimeMill
                db.eventDetail = memoryData.firstInputText.get()
                db.eventConsume = memoryData.secondInputText.get()
            }

            CheckInData.CHECKIN_MOOD -> {
                db.startTime = memoryData.startTimeMill
                db.eventDetail = memoryData.firstInputText.get()
            }

            CheckInData.CHECKID_FINGER_BLOOD -> {
                db.startTime = memoryData.startTimeMill
                val value = memoryData.secondInputText.get()
                db.sgUnit = BgUnitUtils.getUserUnitType().toString()
                if (value.isNullOrEmpty()) {
                    db.eventDetail = ""
                } else {
                    if ((value.isInt() || value.isFloat())) {
                        db.eventDetail = GlucoseUtils.getConvertValue(
                            value!!.toFloat(),
                            BgUnitUtils.isUserMol(),
                            BgUnitUtils.isUserMol()
                        )
                    }
                }
                Log.d("打卡,指血", "本地有数据,内存转数据库,单位:"+db.sgUnit+",值:"+db.eventDetail)
            }
        }
        db.uploadService = 0
        return db
    }

    fun serverToMemoryData(serverData: LifeEventEntity.RecordsDTO?, memoryData: CheckInData) {
        serverData?.apply {
            memoryData.startTime.set(
                TimeUtils.millis2String(actionStartTime, Constant.NORMAL_TIME_FORMAT_)
            )
            actionData?.apply {
                when (serverData.type) {
                    CheckInData.CHECKIN_MEAL -> {
                        memoryData.firstInputText.set(eventConsume?:"")
                        memoryData.secondInputText.set(eventDetail?:"")
                        actionImgs?.forEach {
                            if (it.notNullOrBlank()) {
                                memoryData.addPicture(it, it)
                            }
                        }
                    }

                    CheckInData.CHECKIN_SPORTS -> {
                        memoryData.endTime.set(
                            TimeUtils.millis2String(actionEndTime, Constant.NORMAL_TIME_FORMAT_)
                        )
                        memoryData.firstInputText.set(eventDetail ?: "")
                    }

                    CheckInData.CHECKIN_SLEEP -> {
                        memoryData.endTime.set(
                            TimeUtils.millis2String(actionEndTime, Constant.NORMAL_TIME_FORMAT_)
                        )
                    }

                    CheckInData.CHECKID_FINGER_BLOOD -> {
                        memoryData.firstInputText.set(BgUnitUtils.getUserUnitType().toString())
                        val value = eventDetail ?: ""
                        if (value.notNullOrBlank() && (value.isInt() || value.isFloat())) {
                            memoryData.secondInputText.set(
                                GlucoseUtils.getConvertValue(
                                    value!!.toFloat(),
                                    BgUnitUtils.isTypeMol(unit),
                                    BgUnitUtils.isUserMol()
                                )
                            )
                        }
                        Log.d("打卡,指血", "服务数据转内存,单位:"+memoryData.firstInputText.get()+",值:"+memoryData.secondInputText.get())
                    }

                    CheckInData.CHECKIN_MEDICATIONS -> {
                        memoryData.firstInputText.set(eventDetail ?: "")
                        memoryData.secondInputText.set(eventConsume ?: "")
                    }

                    CheckInData.CHECKIN_INSULIN -> {
                        memoryData.firstInputText.set(eventDetail ?: "")
                        memoryData.secondInputText.set(eventConsume ?: "")
                    }

                    CheckInData.CHECKIN_MOOD -> {
                        memoryData.firstInputText.set(eventDetail ?: "")
                    }
                }
            }
        }
    }

    /**
     * 插入数据库
     */
    fun insertCheckInDataToDB(
        recordEntity: ActionRecordEntity,
        listener: RoomResponseListener<Any?>
    ): Disposable {
        return AppDatabase.getInstance()
            .actionRecordEntityDao
            .insert(recordEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listener.response(null) }
    }

    /**
     * 更新数据库
     */
    fun updateCheckInDataToDB(
        recordEntity: ActionRecordEntity,
        listener: Action
    ): Disposable {
        return AppDatabase.getInstance()
            .actionRecordEntityDao
            .update(recordEntity)
            .doOnError {
                Log.d("打卡", "更新数据库出错:"+it.toString())
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(listener)
    }

    /**
     * 查询数据库
     */
    fun getDBData(
        userId: String,
        dataId: String,
        listener: Consumer<List<ActionRecordEntity>?>
    ): Disposable {
        return AppDatabase.getInstance().actionRecordEntityDao.findDataByDataId(userId, dataId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(listener)
    }

    /**
     * 删除数据
     */
    fun deleteData(
        userId: String,
        dataId: String,
        listener: Action
    ): Disposable {
        return getDBData(userId, dataId) {
            if (!it.isNullOrEmpty()) {
                AppDatabase.getInstance().actionRecordEntityDao.delete(it.first())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(listener)
            }
        }
    }

    fun getName() {

    }

    @JvmStatic
    open fun getRouterPath(type: Int): String? {
        return if (type == ActionRecordEnum.FOOD.type) {
            RouterActivityPath.BsMonitoring.PAGE_DIET_RECORD
        } else if (type == ActionRecordEnum.SPORTS.type) {
            RouterActivityPath.BsMonitoring.PAGE_MOTION_RECORD
        } else if (type == ActionRecordEnum.MEDICATIONS.type) {
            RouterActivityPath.BsMonitoring.PAGE_MD_AND_INSULIN
        } else if (type == ActionRecordEnum.INSULIN.type) {
            RouterActivityPath.BsMonitoring.PAGE_MD_AND_INSULIN
        } else if (type == ActionRecordEnum.SLEEP.type) {
            RouterActivityPath.BsMonitoring.PAGE_SLEEP_RECORD
        } else if (type == ActionRecordEnum.FINGER_BLOOD.type) {
            RouterActivityPath.BsMonitoring.PAGE_FINGER_BLOOD_RECORD
        } else if (type == ActionRecordEnum.PHYSICAL_STATE.type) {
            RouterActivityPath.BsMonitoring.PAGE_HEALTH_STATUS_RECORD
        } else {
            ""
        }
    }

    @JvmStatic
    fun getIcon(type: Int?): Drawable? {
        return if (type == ActionRecordEnum.FOOD.type) {
            ContextCompat.getDrawable(Utils.getApp(), R.mipmap.icon_food)
        } else if (type == ActionRecordEnum.SPORTS.type) {
            ContextCompat.getDrawable(Utils.getApp(), R.mipmap.icon_event_sport)
        } else if (type == ActionRecordEnum.MEDICATIONS.type) {
            ContextCompat.getDrawable(Utils.getApp(), R.mipmap.common_icon_pills)
        } else if (type == ActionRecordEnum.INSULIN.type) {
            ContextCompat.getDrawable(Utils.getApp(), R.mipmap.icon_needle)
        } else if (type == ActionRecordEnum.SLEEP.type) {
            ContextCompat.getDrawable(Utils.getApp(), R.mipmap.common_icon_sleep)
        } else if (type == ActionRecordEnum.FINGER_BLOOD.type) {
            ContextCompat.getDrawable(Utils.getApp(), R.mipmap.icon_blodd)
        } else if (type == ActionRecordEnum.PHYSICAL_STATE.type) {
            ContextCompat.getDrawable(Utils.getApp(), R.mipmap.icon_bs_body_small)
        } else {
            null
        }
    }

}