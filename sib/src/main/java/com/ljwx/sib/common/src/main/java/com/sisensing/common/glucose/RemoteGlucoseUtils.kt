package com.sisensing.common.glucose

import com.blankj.utilcode.util.TimeUtils
import com.sisensing.common.base.ResponseListener
import com.sisensing.common.database.AppDatabase
import com.sisensing.common.database.RoomResponseListener
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity
import com.sisensing.common.entity.Device.DeviceGlucoseEntity
import com.sisensing.common.entity.Device.DeviceManager
import com.sisensing.common.entity.Device.RemoteGlucoseBean
import com.sisensing.common.user.UserInfoUtils
import com.sisensing.common.utils.Log
import com.sisensing.common.utils.TimeSelectUtils
import com.sisensing.common.utils.isDigit
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer

object RemoteGlucoseUtils {

    fun getStartTime(endTime: Long, days: Int): Long {
        TimeUtils.getNowDate()
        val minute = 60 * 1000L
        val rangeTime = minute * (60 * 24) * days
//        return endTime - rangeTime
        return TimeSelectUtils.getReversedDate(TimeUtils.millis2Date(endTime),days).time
    }



}