package com.sisensing.common.ble_new

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ObjectUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.sisensing.common.R
import com.sisensing.common.ble.BleLog
import com.sisensing.common.ble.BleUtils
import com.sisensing.common.ble.v4.AppBleStateBroadcastReceiver
import com.sisensing.common.ble.v4.GlucoseThreadExecutor
import com.sisensing.common.ble.v4.data.GlucoseDataInfo
import com.sisensing.common.ble.v4.data.SensorDeviceInfo
import com.sisensing.common.ble_new.global.BleDeviceStatusGlobal
import com.sisensing.common.entity.Device.DeviceManager
import com.sisensing.common.entity.Device.DeviceRepository
import com.sisensing.common.share.LogUploadModel
import com.sisensing.common.user.UserInfoUtils
import com.sisensing.common.utils.DebugFileUtils
import com.sisensing.common.utils.Log
import no.sisense.android.api.SisenseBluetooth

object BleDeviceConnectUtils {

    private const val MSG_RECONNECT = 1000
    private var lastDeviceConnectStatus = no.sisense.android.api.Constant.STATE_DISCONNECTED
    private var mReconnectMillsUnlock: Long = 1500
    private var mReconnectMillsLock = 5 * 60000L
    private var mReconnectMills = mReconnectMillsUnlock

    /**
     * 蓝牙开关广播监听
     */
    private val blueToothStateReceiver: BroadcastReceiver =
        object : AppBleStateBroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                super.onReceive(context, intent)
                val action = intent.action
                if (BluetoothAdapter.ACTION_STATE_CHANGED == action) {
                    val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 1000)
                    when (state) {
                        BluetoothAdapter.STATE_OFF -> {
                            Log.d("蓝牙:系统广播", "蓝牙关闭")
                            if (!stateOffAndCouldOpen()) {
                                Log.d("蓝牙:系统广播", "设备名不为空:通知监听者断开连接")
                                BleDeviceStatusGlobal.changeConnectStatus(
                                    ConstBleDeviceStatus.DEVICE_DISCONNECT,
                                    true
                                )
                            }
                        }

                        BluetoothAdapter.STATE_ON -> {
                            Log.d("蓝牙:系统广播", "蓝牙开启")
                            BleLog.dReconnect("蓝牙状态改变为开启,发起重连")
                            resendReconnection()
                        }

                        else -> {
                            Log.d("蓝牙:系统广播", "蓝牙未知状态")
                            Log.e("BlueToothError", "蓝牙状态未知")
                        }
                    }
                }
            }
        }

    private val mReconnectionHandler: Handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                MSG_RECONNECT -> {
                    LogUtils.iTag("执行到重连，当前状态：", lastDeviceConnectStatus)
                    BleLog.dReconnect("handler接收到重连消息,下面判断是否真的需要重连", true)
                    if (libIsActive()) {
                        BleLog.dReconnect("当前连接状态为连接中,或扫描中,或已连接,不执行重连")
                        return
                    }
                    val isReConnect = (BleUtils.getInstance().isBleEnabled
                            && !isSibBluetoothConnected()
                            && !SensorDeviceInfo.isAcDis
                            && !SensorDeviceInfo.sensorIsEdOrEp
                            && !SensorDeviceInfo.isRightNowStopData
                            && UserInfoUtils.isLogin()) && SensorDeviceInfo.mDeviceEntity != null && ObjectUtils.isNotEmpty(
                        SensorDeviceInfo.mDeviceName
                    )
                    BleLog.dReconnect(
                        "蓝牙是否可用:" + BleUtils.getInstance().isBleEnabled
                                + ",sib是否连接状态:" + isSibBluetoothConnected()
                                + ",是否主动断开:" + SensorDeviceInfo.isAcDis
                                + ",设备是否正常:" + SensorDeviceInfo.sensorIsEdOrEp
                                + ",是否终止发送数据:" + SensorDeviceInfo.isRightNowStopData
                                + ",是否登录:" + UserInfoUtils.isLogin()
                                + ",设备entity是否为空:" + (SensorDeviceInfo.mDeviceEntity != null)
                                + ",设备名称是否为空:" + ObjectUtils.isNotEmpty(SensorDeviceInfo.mDeviceName)
                    )
                    if (isReConnect) {
                        val time = TimeUtils.millis2String(System.currentTimeMillis())
                        BleLog.dReconnect("触发重连,当前重连时间:" + time, true)
                        prepareDataAndLibConnectDevice()
                        if (ScreenUtils.isScreenLock()) {
                            BleLog.dApp("当前为锁屏状态,下次重试,延迟:${mReconnectMillsLock / 1000}" + "秒")
                            mReconnectMills = mReconnectMillsLock
                        } else {
                            BleLog.dApp("当前未锁屏,下次重试,延迟:${mReconnectMillsUnlock / 1000}" + "秒")
                            mReconnectMills = mReconnectMillsUnlock
                        }
                        DebugFileUtils.setAppendFile("触发重连" + "-----屏幕是否关闭：" + ScreenUtils.isScreenLock() + "-----重连间隔：" + mReconnectMills)
                    } else {
                        BleLog.dReconnect("不满足条件,不执行重连", true)
                    }
                }
            }
        }
    }

    @JvmStatic
    fun registerBleStateBroadcast(context: Context) {
        val bleEnableFilter = IntentFilter()
        bleEnableFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        context.registerReceiver(blueToothStateReceiver, bleEnableFilter)
    }

    @JvmStatic
    fun unregisterBleStateBroadcast(context: Context) {
        context.unregisterReceiver(blueToothStateReceiver)
    }

    @JvmStatic
    fun resendReconnection() {
        BleLog.dReconnect("蓝牙状态为ON后,发送重连handler消息,延迟1500毫秒")
        mReconnectionHandler.removeCallbacksAndMessages(null)
        mReconnectionHandler.sendEmptyMessageDelayed(MSG_RECONNECT, 1500)
    }

    private fun stateOffAndCouldOpen(): Boolean {
        BleLog.dApp("蓝牙关闭,isDeviceConnected赋值为false")
        UserInfoUtils.isDeviceConnected = false
        DebugFileUtils.setAppendFile("监测到蓝牙关闭：bleUtils is enable" + BleUtils.getInstance().isBleEnabled + "-----屏幕是否关闭：" + ScreenUtils.isScreenLock() + "-----系统休眠时间：" + ScreenUtils.getSleepDuration())
        if (ObjectUtils.isEmpty(SensorDeviceInfo.mDeviceName)) {
            Log.d("蓝牙:系统广播", "设备名为空:去开启蓝牙")
            if (PermissionUtils.isGranted(Manifest.permission.BLUETOOTH_CONNECT)) {
                BleUtils.getInstance().openBle()
            }
            return true
        }
        return false
    }

    fun changeConnectStatus(status: Int) {
        lastDeviceConnectStatus = status
    }

    fun getCurrentConnectStatus(): Int {
        return lastDeviceConnectStatus
    }

    fun libIsActive(): Boolean {
        return lastDeviceConnectStatus == no.sisense.android.api.Constant.STATE_CONNECTING
                || lastDeviceConnectStatus == no.sisense.android.api.Constant.STATE_SCANNING
                || lastDeviceConnectStatus == no.sisense.android.api.Constant.STATE_CONNECTED
    }

    /**
     * 执行连接设备逻辑
     */
    @JvmStatic
    fun prepareDataAndLibConnectDevice() {
        BleLog.dApp("开始运行蓝牙连接流程")
        val deviceEntity = SensorDeviceInfo.mDeviceEntity
        LogUploadModel.getInstance()
            .uploadConnectInfo("触发连接" + System.currentTimeMillis() + "mCurrentDeviceName=" + SensorDeviceInfo.mDeviceName + "------deviceName=" + deviceEntity.deviceName)
        LogUtils.i("-----------------------触发连接" + System.currentTimeMillis() + "mCurrentDeviceName=" + SensorDeviceInfo.mDeviceName + "------deviceName=" + deviceEntity.deviceName)
        val linkCode = deviceEntity.blueToothNum
        val matchStr = linkCode.substring(0, 4)
        val deviceName = deviceEntity.deviceName
        GlucoseThreadExecutor.getInstance().createOrRebuildThreadExecutor(
            "connectBluetooth线程池中还有任务在执行。。。" + System.currentTimeMillis(),
            "connectBluetooth线程池中任务执行完毕。。。" + System.currentTimeMillis()
        )

        //避免重复调用算法初始化代码
        if (SensorDeviceInfo.newSensor(deviceName)) {
            BleLog.dApp("新设备连接,deviceName为空或不同,初始化算法")
            LogUtils.e("-----------------------初始化算法")
            val mIAlgorithm = BleDeviceAlgorithmUtils.createAlgorithm(deviceEntity.algorithmVersion)
            if (mIAlgorithm == null) {
                BleDeviceStatusGlobal.changeConnectStatus(
                    ConstBleDeviceStatus.DEVICE_DISCONNECT,
                    false
                )
                ToastUtils.showShort(R.string.please_upgrade_version)
                return
            }
            SensorDeviceInfo.mDeviceName = deviceName
            BleLog.dApp("新设备名:$deviceName")
            LogUploadModel.getInstance()
                .uploadConnectInfo("初始化算法：--------->算法版本=" + mIAlgorithm.getAlgorithmVersion() + "------连接码=" + linkCode)
            val result: Int = mIAlgorithm.verifyLinkCode(linkCode)
            if (result != 1) {
                BleLog.dInterrupt("算法校验连接码不通过")
                ToastUtils.showShort(R.string.common_connection_code_error)
                BleDeviceStatusGlobal.changeConnectStatus(
                    ConstBleDeviceStatus.DEVICE_DISCONNECT,
                    false
                )
                return
            }
            mIAlgorithm.initAlgorithmContext(deviceEntity)
        }
        LogUploadModel.getInstance()
            .uploadConnectInfo("BLe connect start,App version name:" + AppUtils.getAppVersionName())
        BleLog.dApp("全部通过,准备调用蓝牙库连接蓝牙,当前蓝牙连接状态:$lastDeviceConnectStatus")
        if (!BleUtils.getInstance().isBleEnabled) {
            BleUtils.getInstance().openBle()
            return
        }
        if (lastDeviceConnectStatus == no.sisense.android.api.Constant.STATE_DISCONNECTED) {
            BleLog.eApp("当前状态为未连接,直接执行蓝牙库连接蓝牙")
            BleLibUtils.connectDevice(matchStr)
        } else {
            //每次连接之前判断是否正在连接或者扫描，如果正在连接先主动断开一次，
            BleLog.eApp("蓝牙状态为非断开,先断开,再执行连接")
            BleLibUtils.stopConnect()
            Handler().postDelayed({ BleLibUtils.connectDevice(matchStr) }, 1000)
        }
    }

    /**
     * 判断蓝牙服务是否处于连接状态
     *
     * @return
     */
    @JvmStatic
    fun isSibBluetoothConnected(): Boolean {
        BleLog.dApp("isSibBluetoothConnected:判断是否已连接")
        DebugFileUtils.setAppendFile("检查蓝牙是否断开：mNumUnReceived=" + GlucoseDataInfo.mNumUnReceived + "-----mSyncTimeMill：" + GlucoseDataInfo.mSyncTimeMill + "-----系统休眠时间：")
        BleLog.dApp("APP自己判断是否断连,isDeviceConnected:" + UserInfoUtils.isDeviceConnected)
        if (!UserInfoUtils.isDeviceConnected || GlucoseDataInfo.dataConnectionException()) {
            BleLog.dApp("APP自己综合判断(包括三分钟未同步数据),判定当前连接状态为:未连接")
            return false
        }
        if (SensorDeviceInfo.mBluetoothDevice == null || ObjectUtils.isEmpty(SensorDeviceInfo.mDeviceName)) {
            BleLog.dApp("设备或设备名未空,判定当前连接状态为:未连接")
            return false
        }
        val connect = BleLibUtils.isConnect()
        BleLog.dApp("APP自己无法判断为未连接,使用蓝牙库自己的方法判断是否连接:$connect")
        return connect
    }

    /**
     * 断开后的重连逻辑 原dealDisconnect()方法
     */
    @JvmStatic
    fun disconnectAndReconnect() {
        BleLog.dApp("isDeviceConnected赋值为false");
        UserInfoUtils.isDeviceConnected = false
        BleDeviceStatusGlobal.changeConnectStatus(ConstBleDeviceStatus.DEVICE_DISCONNECT, true)
        BleLog.dReconnect("连接断开后,发送延迟消息重连,延迟:" + (mReconnectMills / 1000), true)
        mReconnectionHandler.removeCallbacksAndMessages(null);
        mReconnectionHandler.sendEmptyMessageDelayed(MSG_RECONNECT, mReconnectMills);
    }

    @JvmStatic
    fun deviceConnectSuccess() {
        BleLog.dLib("所有程序正常,走正常连接逻辑")
        if (SensorDeviceInfo.mDeviceEntity != null) {
            //校验全部通过,可以保存
            DeviceManager.getInstance().deviceEntity = SensorDeviceInfo.mDeviceEntity
        }

        if (SensorDeviceInfo.mDeviceEntity != null) {
            if (ObjectUtils.isEmpty(SensorDeviceInfo.mDeviceId)) {
                //没有设备id直接新增设备
                val bluetoothNum = SensorDeviceInfo.mDeviceEntity.blueToothNum
                DeviceManager.getInstance().insertDevice(SensorDeviceInfo.mDeviceEntity)
                LogUtils.e("------------------------>新增设备" + SensorDeviceInfo.mDeviceEntity.deviceName)
                BleDeviceServiceUtils.updateDeviceInfo2Server(
                    bluetoothNum,
                    SensorDeviceInfo.mMacAddress,
                    SensorDeviceInfo.mDeviceName,
                    BleDeviceAlgorithmUtils.getCurrentAlgorithm().algorithmVersion
                )
            } else {
                //更新设备连接时间
                DeviceRepository.getInstance().updateConnectMillAndCharacteristic(
                    UserInfoUtils.getUserId(),
                    SensorDeviceInfo.mDeviceName,
                    System.currentTimeMillis(),
                    SensorDeviceInfo.mDeviceEntity.characteristic
                )
                LogUtils.e("------------------------>更新设备时间" + SensorDeviceInfo.mDeviceEntity.deviceName)
            }
        }
        BleDeviceStatusGlobal.changeConnectStatus(ConstBleDeviceStatus.DEVICE_CONNECTED)
        //蓝牙版本号和产品线
        val bleSdkVersion: String = BleLibUtils.getSDKVersionMessage()
        val productLine = SisenseBluetooth.getProductLine()
        LogUploadModel.getInstance()
            .uploadConnectInfo("Ble SDK Version:" + bleSdkVersion + ",Product Line:" + productLine + ",DeviceName:" + SensorDeviceInfo.mDeviceName)
    }

    @JvmStatic
    fun stopReconnect() {
        BleLog.dReconnect("蓝牙连接成功,移除重连handler消息")
        mReconnectionHandler.removeCallbacksAndMessages(null)
    }

}