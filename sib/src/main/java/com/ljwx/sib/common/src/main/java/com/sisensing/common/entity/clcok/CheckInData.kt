package com.sisensing.common.entity.clcok

import androidx.databinding.ObservableField
import com.blankj.utilcode.util.TimeUtils
import com.sisensing.common.constants.Constant
import com.sisensing.common.entity.actionRecord.ActionRecordEnum

/**
 * 打卡页面数据
 */
class CheckInData(private val type: Int) {

    companion object {
        val CHECKIN_MEAL = ActionRecordEnum.FOOD.type
        val CHECKIN_SPORTS = ActionRecordEnum.SPORTS.type
        val CHECKIN_MEDICATIONS = ActionRecordEnum.MEDICATIONS.type
        val CHECKID_FINGER_BLOOD = ActionRecordEnum.FINGER_BLOOD.type
        val CHECKIN_SLEEP = ActionRecordEnum.SLEEP.type
        val CHECKIN_INSULIN = ActionRecordEnum.INSULIN.type
        val CHECKIN_MOOD = ActionRecordEnum.PHYSICAL_STATE.type
    }

    var startTime = ObservableField<String>()
    val startTimeMill: Long
        get() {
            if (startTime.get().isNullOrEmpty()) {
                return 0
            }
            return TimeUtils.string2Millis(startTime.get(), Constant.NORMAL_TIME_FORMAT_)
        }

    var startTimeNecessary = true

    val endTime = ObservableField<String>()
    val endTimeMill: Long
        get() {
            if (endTime.get().isNullOrEmpty()) {
                return 0
            }
            return TimeUtils.string2Millis(endTime.get(), Constant.NORMAL_TIME_FORMAT_)
        }
    var endTimeNecessary = false
    val endTimeVisible = ObservableField(true)

    var timeDuration = 0L

    //饮食
    val firstInputText = ObservableField<String>()
    var firstInputVisible = ObservableField(true)
    val secondInputText = ObservableField<String>()
    var secondInputVisible = ObservableField(false)

    //图片链接(1.添加事件:key:本地文件路径 2.更新事件:远程url和本地文件路径, url)
    private val pictures = LinkedHashMap<String, String>()
    val picturesVisible = ObservableField(false)

    init {
        changeType(type)
    }

    fun changeType(type: Int) {
        when (type) {
            CHECKIN_MEAL -> {
                endTimeVisible.set(false)
                firstInputVisible.set(true)
                secondInputVisible.set(true)
                picturesVisible.set(true)
            }

            CHECKIN_SPORTS -> {
                endTimeVisible.set(true)
                firstInputVisible.set(true)
                secondInputVisible.set(false)
            }

            CHECKIN_SLEEP -> {
                endTimeVisible.set(true)
                firstInputVisible.set(false)
                secondInputVisible.set(false)
            }
            //指血
            CHECKID_FINGER_BLOOD -> {
                endTimeVisible.set(false)
                firstInputVisible.set(true)
                secondInputVisible.set(true)
            }

            CHECKIN_MEDICATIONS -> {
                endTimeVisible.set(false)
                firstInputVisible.set(true)
                secondInputVisible.set(true)
            }

            CHECKIN_INSULIN -> {
                endTimeVisible.set(false)
                firstInputVisible.set(true)
                secondInputVisible.set(true)
            }

            CHECKIN_MOOD -> {
                endTimeVisible.set(false)
                firstInputVisible.set(true)
                secondInputVisible.set(false)
            }
        }
    }

    fun isExistPicture(key: String) = pictures[key] != null

    fun getAllPictures(): Map<String, String> = pictures

    fun deletePicture(key: String) = pictures.remove(key)

    fun addPicture(key: String, url: String) = pictures.put(key, url)

//    fun addServerPictures(urls: List<String>) {
//        urls.forEach {
//            pictures[it] = it
//        }
//    }

}