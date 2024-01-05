package com.sisensing.common.ble_new

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ThreadUtils
import com.blankj.utilcode.util.TimeUtils
import com.sisensing.common.ble.BleLog
import com.sisensing.common.ble.CgmStatusListener
import com.sisensing.common.ble.v4.GlucoseThreadExecutor
import com.sisensing.common.ble.v4.data.GlucoseDataInfo
import com.sisensing.common.ble.v4.data.SensorDeviceInfo
import com.sisensing.common.ble_new.status.SensorGlucoseStatus
import com.sisensing.common.constants.Constant
import com.sisensing.common.database.AppDatabase
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity
import com.sisensing.common.entity.Device.DeviceRepository
import com.sisensing.common.share.LogUploadModel
import com.sisensing.common.user.UserInfoUtils
import com.sisensing.common.utils.ConfigUtils
import com.sisensing.common.utils.Log
import no.sisense.android.bean.CGMRecordV120
import no.sisense.android.bean.GjCGMRecord

object BleDeviceGlucoseDataUtils {

    private var mStatusListener: CgmStatusListener? = null

    /**
     * 接收到数据第一步
     */
    @JvmStatic
    fun <T> bleLibDataReceive(gjCGMRecord: T) {
        Log.v("蓝牙:jar包回调", "onDataReceive:接收数据:" + TimeUtils.getNowString())
        GlucoseThreadExecutor.getInstance().execute {
            val index = getCurrentIndex(gjCGMRecord)
            if (index == GlucoseDataInfo.mCurrentInsertIndex + 1) {
                //index从小往大
                Log.v("蓝牙:线程池", "蓝牙库血糖数据正常处理:index:$index")
                // 连续index，正常
                dealCompositedData(index, gjCGMRecord)
            } else if (index > GlucoseDataInfo.mCurrentInsertIndex + 1) {
                // 发生index丢失，断开蓝牙
                BleLog.dApp("index大于已插入的+1,则若sibBlue还在连接,则停止连接")
                LogUploadModel.getInstance()
                    .uploadConnectInfo("发生index丢失--mCurrentInsertIndex=" + GlucoseDataInfo.mCurrentInsertIndex + "--------------getIndex=" + index)
                if (BleDeviceConnectUtils.isSibBluetoothConnected()) {
                    BleLibUtils.stopConnect()
                }
            } else {
                Log.d("蓝牙:线程池", "index重复了,重复数据不处理")
                // index重复不处理
                LogUploadModel.getInstance()
                    .uploadConnectInfo("发生index重复，不处理--mCurrentInsertIndex=" + GlucoseDataInfo.mCurrentInsertIndex + "--------------getIndex=" + index)
            }
        }
    }

    private fun <T> dealCompositedData(index: Int, cgmRecord: T) {
        //数据索引
        BleLog.dGlucose("当前血糖index:$index")

        //timeMillis = systemTimeMills + nextCollectionTime * 1000L - numOfUnreceived * 60 * 1000L;
        //timeMillis =当前时间-60*（index+剩余笔数）+60s;
        //  long timeMillis = cgmRecord.getTimeMillis();
        GlucoseDataInfo.mNumUnReceived = getUnreceivedNum(cgmRecord)
        val userId = UserInfoUtils.getUserId()
        val mIAlgorithm = BleDeviceAlgorithmUtils.getCurrentAlgorithm()
        val glucoseEntity = sensorData2AppDBData(cgmRecord)
        judgeSensorState(glucoseEntity, index)
        if (SensorDeviceInfo.notExpiredAndEnable(index)) {
            BleLog.dGlucose("index没有超过14天,也没有异常情况导致断开连接")
            AppDatabase.getInstance().runInTransaction {
                BleLog.dGlucose("将转换后的app可用的血糖数据,存入数据库")
                //app数据库插入血糖数据
                AppDatabase.getInstance().bloodEntityDao.insert(glucoseEntity)
                //保存算法中间变量
                mIAlgorithm.saveAlgorithmContext(userId, SensorDeviceInfo.mDeviceName, index)
                GlucoseDataInfo.mCurrentInsertIndex = glucoseEntity.index
            }
            if (GlucoseDataInfo.needUpdateAgain(index) && SensorDeviceInfo.sensorInfoSure()) {
                BleLog.dGlucose("血糖接收后,设备及血糖满足上传要求,运行上传血糖逻辑")
                //未同步与不是最后一笔的情况下每5笔再上传一次血糖数据，因为设备失效是在20160的下一笔，
                // 而血糖数据的最后一笔是在20160，所以这里不能在20160上传血糖，否则失效了状态无法上传
                BleDeviceServiceUtils.isNeedUploadGlucoseData(
                    SensorDeviceInfo.mDeviceName,
                    SensorDeviceInfo.mDeviceId
                )
            }
        }

        if (SensorDeviceInfo.notException() && mStatusListener != null) {
            //传感器未损坏或过期的情况下将数据发送到首页
            ThreadUtils.runOnUiThread {
                Log.v("蓝牙:线程池", "在ui线程,将接收到的血糖数据更新到首页")
                mStatusListener?.updateData(glucoseEntity)
            }
        }

        SensorDeviceInfo.judgeSensorEnable()
    }

    @JvmStatic
    fun setStatusListener(statusListener: CgmStatusListener) {
        BleLog.dApp("setStatusListener:设置状态监听-" + statusListener.javaClass.simpleName)
        mStatusListener = statusListener
    }

    @JvmStatic
    fun <T> sensorData2AppDBData(cgmRecord: T): BloodGlucoseEntity {
        val userId = UserInfoUtils.getUserId()

        //数据索引
        var index = 0
        //温度
        var temp = 0f
        //电量
        var electric: Long = 0
        //血糖状态
        var status = 0
        //电流值
        var value = 0f
        //未读笔数
        var numUnreceived = 0

        if (cgmRecord is GjCGMRecord) {
            index = (cgmRecord as GjCGMRecord).getIndex()
            temp = (cgmRecord as GjCGMRecord).getTemp() / (if (BleLibUtils.isSkuLib()) 10f else 1f)
            electric = (cgmRecord as GjCGMRecord).getElectric()
            status = (cgmRecord as GjCGMRecord).getStatus()
            value = (cgmRecord as GjCGMRecord).getValue()
            numUnreceived = (cgmRecord as GjCGMRecord).getNumOfUnreceived()
        } else if (cgmRecord is CGMRecordV120) {
            index = (cgmRecord as CGMRecordV120).index
            temp = (cgmRecord as CGMRecordV120).temp / 10f
            electric = (cgmRecord as CGMRecordV120).dump.toLong()
            value = (cgmRecord as CGMRecordV120).current / 10f
            numUnreceived = (cgmRecord as CGMRecordV120).reindex
        }

        val glucoseEntity = BloodGlucoseEntity()

        glucoseEntity.index = index
        glucoseEntity.bleName = SensorDeviceInfo.mDeviceName
        glucoseEntity.macAddress = SensorDeviceInfo.mMacAddress
        glucoseEntity.temperatureValue = temp
        glucoseEntity.currentValue = value
        glucoseEntity.stateValue = status
        glucoseEntity.numOfUnreceived = numUnreceived
        glucoseEntity.electric = electric
        glucoseEntity.userId = userId

        val defaultHigh = ConfigUtils.getInstance().getDefaultHigh(userId)
        val defaultLow = ConfigUtils.getInstance().getDefaultLow(userId)

        val mIAlgorithm = BleDeviceAlgorithmUtils.getCurrentAlgorithm()
        //血糖值
        var glucoseValue: Float =
            mIAlgorithm.loadData(index, value, temp, 0f, defaultLow, defaultHigh)
        //血糖趋势
        val glucoseTrend: Int = mIAlgorithm.getGlucoseTrend()
        //电流报警
        val currentWarning: Int = mIAlgorithm.getCurrentWarning()
        //温度报警
        val tempWarning: Int = mIAlgorithm.getTempWarning()
        //血糖报警
        val cgmWarning: Int = mIAlgorithm.getCgmWarning()

        //对血糖值进行处理，低于2.2的赋值为2.2,高于25的赋值为25
        if (glucoseValue < 2.2) {
            glucoseValue = 2.2f
        }
        if (glucoseValue >= 25) {
            glucoseValue = 25f
        }

        glucoseEntity.glucoseValue = glucoseValue
        glucoseEntity.temperatureWarning = tempWarning
        glucoseEntity.glucoseWarning = cgmWarning
        glucoseEntity.currentWarning = currentWarning
        glucoseEntity.glucoseTrend = glucoseTrend
        glucoseEntity.validIndex = if (index % 5 == 0) index / 5 else 0

        SensorDeviceInfo.sensorIsEdOrEp = false

        setGlucoseDataStatus(glucoseEntity, index, tempWarning)

        if (index == 1) {
            GlucoseDataInfo.mProcessedTimeMill = getFirstIndexMill(index + numUnreceived)
        } else {
            GlucoseDataInfo.mProcessedTimeMill += 60000L
        }
        GlucoseDataInfo.mSyncTimeMill = System.currentTimeMillis()
        glucoseEntity.processedTimeMill = GlucoseDataInfo.mProcessedTimeMill

        return glucoseEntity
    }

    private fun judgeSensorState(glucoseEntity: BloodGlucoseEntity, index: Int) {
        val userId = UserInfoUtils.getUserId()
        //电流报警状态
        val currentWarning: Int = BleDeviceAlgorithmUtils.getCurrentAlgorithm().getCurrentWarning()
        //传感器电流是否正常
        BleDeviceStatusUtils.sensorIsDamagedInOneHour(index, currentWarning, userId)
        //根据时间做判断
        if (GlucoseDataInfo.isFirstData(index)) {
            val timeMills = getFirstIndexMill(index + glucoseEntity.numOfUnreceived)
            DeviceRepository.getInstance().updateDeviceStatusAndFirstMill(
                userId,
                SensorDeviceInfo.mDeviceName,
                3,
                0,
                timeMills
            )
            LogUtils.e("激活时间------------------------$timeMills")
        } else if (GlucoseDataInfo.isExactlyOneHour(index)) {
            //刚好使用一个小时
            LogUploadModel.getInstance()
                .uploadConnectInfo("sensor index=60，" + SensorDeviceInfo.mDeviceName)
            DeviceRepository.getInstance()
                .updateDeviceStatus(userId, SensorDeviceInfo.mDeviceName, 1, 0)
        } else if (GlucoseDataInfo.isExpired(index)) {
            //传感器失效(已停用)断开连接并更新状态为2
            LogUploadModel.getInstance()
                .uploadConnectInfo("sensor valid:" + SensorDeviceInfo.mDeviceName)
            BleDeviceStatusUtils.sensorIsExpire(userId, Constant.SENSOR_INVALID)
        } else {
            setCurrentAlarmStatus(
                glucoseEntity,
                userId,
                currentWarning,
                index,
            )
        }
    }

    /**
     * 设置血糖状态
     */
    fun setGlucoseDataStatus(glucoseEntity: BloodGlucoseEntity, index: Int, errTemperature: Int) {
        if (errTemperature == SensorGlucoseStatus.TEMPERATURE_LOW) {
            //温度过低
            glucoseEntity.alarmStatus = SensorGlucoseStatus.ALARM_TEMPERATURE_LOW
        } else if (errTemperature == SensorGlucoseStatus.TEMPERATURE_HIGH) {
            //温度过高
            glucoseEntity.alarmStatus = SensorGlucoseStatus.ALARM_TEMPERATURE_HIGH
        } else {
            //温度正常
            if (validIndex(index)) {
                if (glucoseEntity.glucoseValue < 2.2f) {
                    //血糖过低
                    glucoseEntity.alarmStatus = SensorGlucoseStatus.ALARM_GLUCOSE_LOW
                } else if (glucoseEntity.glucoseValue > 25f) {
                    //血糖过高
                    glucoseEntity.alarmStatus = SensorGlucoseStatus.ALARM_GLUCOSE_HIGH
                } else {
                    //血糖正常
                    glucoseEntity.alarmStatus = SensorGlucoseStatus.ALARM_NORMAL
                }
            } else {
                glucoseEntity.alarmStatus = SensorGlucoseStatus.ALARM_NORMAL
            }
        }
    }

    @JvmStatic
    fun postDataToUI(mStatusListener: CgmStatusListener, glucoseEntity: BloodGlucoseEntity) {
        if (SensorDeviceInfo.notException() && mStatusListener != null) {
            //传感器未损坏或过期的情况下将数据发送到首页
            ThreadUtils.runOnUiThread {
                if (mStatusListener != null) {
                    Log.v("蓝牙:线程池", "在ui线程,将接收到的血糖数据更新到首页")
                    mStatusListener.updateData(glucoseEntity)
                }
            }
        }
    }

    /**
     * 有效血糖
     */
    fun validIndex(index: Int): Boolean {
        return index % 5 == 0
    }

    /**
     * 倒推第一笔血糖时间
     */
    fun getFirstIndexMill(totalIndex: Int): Long {
        return System.currentTimeMillis() - (60 * 1000L * totalIndex) + (60 * 1000L)
    }

    private fun setCurrentAlarmStatus(
        glucoseEntity: BloodGlucoseEntity,
        userId: String,
        currentWarning: Int,
        index: Int,
    ) {
        //电流异常index
        val acIndex = UserInfoUtils.getAcIndex()
        //电流异常次数
        val acCount = UserInfoUtils.getAcCount()
        if (currentWarning > 0) {
            UserInfoUtils.putAcIndex(index)
            UserInfoUtils.putAcCount(acCount + 1)
            LogUtils.e("acIndex1,acCount1", acIndex, acCount)
            // TODO: 2021/6/1 30分钟的传感器异常,更新alarmStatus为2
            glucoseEntity.alarmStatus = 2
            if (acCount >= 180) {
                //3小时候的传感器异常
                // TODO: 2021/6/1 deviceStatus为4
                //更新本地设备deviceStatus为4 断开传感器
                BleDeviceStatusUtils.sensorIsDamaged(userId)
            }
        } else {
            if (acIndex != 0 && index <= acIndex + 30) {
                //传感器异常且30分钟之内
                UserInfoUtils.putAcCount(acCount + 1)
                // TODO: 2021/6/1 更新alarmStatus为2
                glucoseEntity.alarmStatus = 2
                if (acCount >= 180) {
                    //3小时候的传感器异常
                    // TODO: 2021/6/1
                    //更新本地设备deviceStatus为4
                    BleDeviceStatusUtils.sensorIsDamaged(userId)
                }
            } else {
                //恢复正常
                // TODO: 2021/6/1 判定温度异常
                // TODO: 2021/6/1 判定血糖偏高/偏低 (只能判定有血糖值)
                // TODO: 2021/6/1 更新alarmStatus
                UserInfoUtils.putAcIndex(0)
                UserInfoUtils.putAcCount(0)
            }
        }
    }

    /**
     * 获取当前血糖index
     */
    @JvmStatic
    fun <T> getCurrentIndex(gjCGMRecord: T): Int {
        var index = -1
        if (gjCGMRecord is GjCGMRecord) {
            index = gjCGMRecord.index
        } else if (gjCGMRecord is CGMRecordV120) {
            index = gjCGMRecord.index
        }
        return index
    }

    /**
     * 获取未读笔数
     */
    private fun <T> getUnreceivedNum(gjCGMRecord: T): Int {
        var num = 0
        if (gjCGMRecord is GjCGMRecord) {
            num = gjCGMRecord.getNumOfUnreceived()
        } else if (gjCGMRecord is CGMRecordV120) {
            num = gjCGMRecord.reindex
        }
        return num
    }

}