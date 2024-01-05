package com.ljwx.basemodule.ble;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.sisensing.common.R;
import com.sisensing.common.algorithom.AlgorithmFactory;
import com.sisensing.common.algorithom.IAlgorithm;
import com.sisensing.common.ble.BleLog;
import com.sisensing.common.ble.area.DeviceAreaCodeUtils;
import com.sisensing.common.ble.dialog.SimpleTipsDialog;
import com.sisensing.common.ble.v4.BloodGlucoseEntityUtils;
import com.sisensing.common.ble.v4.GlucoseThreadExecutor;
import com.sisensing.common.ble.v4.data.GlucoseDataInfo;
import com.sisensing.common.ble.v4.data.SensorDeviceInfo;
import com.sisensing.common.ble.v4.extract.LocalBleService5AbstractV3;
import com.sisensing.common.constants.Constant;
import com.sisensing.common.database.AppDatabase;
import com.sisensing.common.database.RoomTask;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceManager;
import com.sisensing.common.entity.Device.DeviceRepository;
import com.sisensing.common.entity.alarm.GlobalLiveData;
import com.sisensing.common.share.LogUploadModel;
import com.sisensing.common.share.LogUploadModel2;
import com.sisensing.common.user.UserInfoUtils;
import com.sisensing.common.utils.BroadcastManager;
import com.sisensing.common.utils.DebugFileUtils;
import com.sisensing.common.utils.LocationUtils;
import com.sisensing.common.utils.Log;

import java.util.HashMap;
import java.util.Map;

import no.sisense.android.api.SisenseBluetooth;
import no.sisense.android.bean.CGMRecord;
import no.sisense.android.bean.CGMRecordV120;
import no.sisense.android.bean.GjCGMRecord;


public class LocalBleServiceV3 extends LocalBleService5AbstractV3 implements IBle {

    private SisenseBluetooth mSiBluetooth;

    private IAlgorithm mIAlgorithm;

    private CgmStatusListener mStatusListener;

    //蓝牙库是否已初始化(由于蓝牙库初始化需要连接权限，故有此标识)
    private boolean bleLibIsInit = false;

    @Override
    public void onCreate() {
        super.onCreate();
        initSiBluetooth();
    }

    /**
     * 初始化远程蓝牙连接
     */
    private void initSiBluetooth() {
        Log.d("蓝牙", "初始化jar包蓝牙");
        mSiBluetooth = SisenseBluetooth.getInstance(this);
//        mSiBluetooth.setAutoConnectFlag(false);
        mSiBluetooth.setOnSibListener(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
            bleInit();
            bleLibIsInit = true;
        }
    }

    @Override
    public <T> void onGJDataRecive(BluetoothDevice bluetoothDevice, T t) {
        if (t instanceof GjCGMRecord) {
            bleLibDataReceive(((GjCGMRecord) t).getIndex(), (GjCGMRecord) t);
        } else if (t instanceof CGMRecordV120) {
            bleLibDataReceive(((CGMRecordV120) t).getIndex(),  t);
        } else {
            BleLog.dGlucose("血糖数据类型不对");
        }
    }

    private <T> void bleLibDataReceive(int index, T gjCGMRecord) {
        Log.v("蓝牙:jar包回调", "onDataReceive:接收数据:" + TimeUtils.getNowString());
        GlucoseThreadExecutor.getInstance().execute(() -> {
            if (index == GlucoseDataInfo.mCurrentInsertIndex + 1) {
                //index从小往大
                Log.v("蓝牙:线程池", "jar包传的血糖数据正常处理:index:" + index);
                // 连续index，正常
                dealCompositedData(index, gjCGMRecord);
            } else if (index > GlucoseDataInfo.mCurrentInsertIndex + 1) {
                // 发生index丢失，断开蓝牙
                BleLog.dApp("index大于已插入的+1,则若sibBlue还在连接,则停止连接");
                LogUploadModel.getInstance().uploadConnectInfo("发生index丢失--mCurrentInsertIndex=" + GlucoseDataInfo.mCurrentInsertIndex + "--------------getIndex=" + index);
                if (isSibBluetoothConnected()) {
                    stopConnect();
                }
            } else {
                Log.d("蓝牙:线程池", "index重复了,重复数据不处理");
                // index重复不处理
                LogUploadModel.getInstance().uploadConnectInfo("发生index重复，不处理--mCurrentInsertIndex=" + GlucoseDataInfo.mCurrentInsertIndex + "--------------getIndex=" + index);
            }
        });

    }

    @Override
    public void isCanReadSugar(BluetoothDevice bluetoothDevice) {
        bleLibCheckPass(bluetoothDevice);
    }

    private void bleLibCheckPass(BluetoothDevice bluetoothDevice) {
        BleLog.dLib("一切准备就绪,isCanReadSugar回调");
        String manu = mSiBluetooth.getAllCharacticOfDevice(bluetoothDevice).getmManufacturer();
        if (!DeviceAreaCodeUtils.isValid(manu)) {
            areaIsValid(manu);
            dealDisconnect();
            BleLog.dLib("不支持的设备制造商-sku:" + manu);
            return;
        }
        deviceConnectable();
        //创建线程池
        GlucoseThreadExecutor.getInstance().createOrRebuildThreadExecutor("onServieDiscovered线程池中还有任务在执行。。。" + System.currentTimeMillis(),
                "onServieDiscovered线程池中任务执行完毕。。。" + System.currentTimeMillis());
        //从数据库获取最后一笔血糖数据
        RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().getLastBloodGlucose(UserInfoUtils.getUserId(), SensorDeviceInfo.mDeviceName), bloodGlucoseEntity -> {
            //本地存储的血糖数据最后一个索引值
            int lastIndex = 0;
            if (ObjectUtils.isNotEmpty(bloodGlucoseEntity)) {
                lastIndex = bloodGlucoseEntity.getIndex();
                Log.d("蓝牙:jar包回调", "onServieDiscovered:从数据库获取最后一笔血糖index:" + lastIndex);
                GlucoseDataInfo.mProcessedTimeMill = bloodGlucoseEntity.getProcessedTimeMill();
            }
            GlucoseDataInfo.mCurrentInsertIndex = lastIndex;
            LogUploadModel.getInstance().uploadConnectInfo("-----------------------本地存储的血糖对应index=" + GlucoseDataInfo.mCurrentInsertIndex);
            Log.d("蓝牙:jar包回调", "onServieDiscovered:从传感器获取index的下一条数据");
            // TODO 不明修改
            mSiBluetooth.getDataSugarFour(bluetoothDevice, ++lastIndex, (int) (System.currentTimeMillis() / 1000), "adcd1234", 0);
        });
    }

    @Override
    public void onConnectionSate(BluetoothDevice bluetoothDevice, String s, int status) {
        CURRENT_CONNECT_STATUS = status;
        BleLog.eLib("连接状态回调," + s + "：" + BleLog.status(status));
        switch (status) {
            case no.sisense.android.api.Constant.STATE_CONNECTED:
                GlucoseDataInfo.mSyncTimeMill = System.currentTimeMillis();
                UserInfoUtils.isDeviceConnected = true;
                SensorDeviceInfo.isAcDis = false;
                SensorDeviceInfo.mBluetoothDevice = bluetoothDevice;
                BleLog.dReconnect("蓝牙连接成功,移除重连handler消息");
                mReconnectionHandler.removeCallbacksAndMessages(null);
//                addDevice(bluetoothDevice);
//                connected();
                break;
            case no.sisense.android.api.Constant.STATE_DISCONNECTED:
                ThreadUtils.runOnUiThread(() -> dealDisconnect());
//                UserInfoUtils.isDeviceConnected = false;
//                if (SensorDeviceInfo.couldReconnection() && UserInfoUtils.isLogin()) {
//                    sendReconnectionMessage();
//                }
//                disConnected(true);
                GlobalLiveData.INSTANCE.getScanCodeFail().postValue(true);
                break;
            case no.sisense.android.api.Constant.STATE_CONNECTING:
            case no.sisense.android.api.Constant.STATE_SCANNING:
                if (BleUtils.getInstance().isBleEnabled()) {
                    if (LocationUtils.isOpenLocation(this) || !LocationUtils.useLocation(true)) {
                        SensorDeviceInfo.isAcDis = false;
                        connecting();
                    } else {
                        BleLog.dLib("onConnectionSate:定位未打开,主动停止连接,发送定位未打开广播");
                        stopConnect();
                        BroadcastManager.getInstance(this).sendBroadcast(Constant.BROADCAST_BLUETOOTH_OPEN_LOCATION_NOT_OPEN);
                    }
                }
                break;
            case no.sisense.android.api.Constant.STATE_AUTHENTICATION_FAIL:
                SimpleTipsDialog.showAuthFail();
                ThreadUtils.runOnUiThread(() -> dealDisconnect());
                GlobalLiveData.INSTANCE.getScanCodeFail().postValue(true);
                break;
            default:
                break;
        }
    }

    /**
     * 蓝牙连接成功后调用新增设备接口
     */
    private void addDevice(BluetoothDevice bluetoothDevice) {
        Log.d("蓝牙:jar包回调", "蓝牙正常");
        if (ObjectUtils.isNotEmpty(bluetoothDevice)) {
            SensorDeviceInfo.addDevice(bluetoothDevice, mIAlgorithm);
            String bluetoothNum = SensorDeviceInfo.mDeviceEntity.getBlueToothNum();
            String algorithmVersion = mIAlgorithm.getAlgorithmVersion();
            if (SensorDeviceInfo.mDeviceEntity != null) {
                //如果是新设备,则插入数据库,并调用新增设备接口
                //否则更新连接时间
                if (!SensorDeviceInfo.isConnectedDeviceAndUpdateTime()) {
                    //没有设备id直接新增设备
                    Log.d("蓝牙:jar包回调", "数据库新增设备,并调用接口,userId:" + SensorDeviceInfo.mDeviceEntity.getUserId()
                            + "-设备名:" + SensorDeviceInfo.mDeviceEntity.getDeviceName());
                    DeviceManager.getInstance().insertDevice(SensorDeviceInfo.mDeviceEntity);
                    LogUtils.i("------------------------>新增设备" + SensorDeviceInfo.mDeviceEntity.getDeviceName());
                    mServiceModel.addDevice(bluetoothNum, SensorDeviceInfo.mMacAddress, SensorDeviceInfo.mDeviceName, algorithmVersion);
                }
            }
        }
    }

    /**
     * deviceName: 不能为空,需要连接的BLE设备名称
     * delayMillis: 不能为空,首次连接或者异常重新连接后,执行扫描的最长时间
     * mCallback: 不能为空,回调接口,连接状态、数据传输、连接异常等信息
     * Action：  不能为空, 2--开始连接；1--停止连接
     *
     * @param
     */
    @Override
    public void startConnect(DeviceEntity deviceEntity) {
        BleLog.dApp("startConnect:开始连接,更新deviceEntity,deviceName:" + deviceEntity.getDeviceName());
        SensorDeviceInfo.isRightNowStopData = false;
        SensorDeviceInfo.mDeviceEntity = deviceEntity;
        SensorDeviceInfo.mDeviceId = deviceEntity.getDeviceId();

        mReconnectMills = mReconnectMillsUnlock;
        if (bleLibIsInit) {
            connectBluetooth();
        } else {
            bleInit();
            new Handler().postDelayed(() -> connectBluetooth(), 1000);
        }
    }

    private void bleInit() {
        BleLog.dApp("初始化蓝牙库,并传入key");
        mSiBluetooth.init();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //1.连接码全匹配 2.过滤写死2
        preferences.edit().putInt("scantypematch", 2).apply();
        mSiBluetooth.v120RegisterKey(Constant.BLE_KEY, Constant.BLE_KEY.length(), getApplicationContext());
    }

    /**
     * 断开蓝牙服务连接释放资源
     */
    @Override
    public void stopConnect() {
        BleLog.eApp("主动断开连接");
        SensorDeviceInfo.isAcDis = true;
//        mSiBluetooth.BleBluthtoothDisConnect(1);
        if (ObjectUtils.isNotEmpty(SensorDeviceInfo.mDeviceName)) {
            if (SensorDeviceInfo.mBluetoothDevice == null) {
                mSiBluetooth.BleBluthtoothDisConnect(null, SensorDeviceInfo.mDeviceName);
            } else {
                mSiBluetooth.BleBluthtoothDisConnect(SensorDeviceInfo.mBluetoothDevice, SensorDeviceInfo.mDeviceName);
            }
        }
    }


    /**
     * 判断蓝牙服务是否处于连接状态
     *
     * @return
     */
    @Override
    public boolean isSibBluetoothConnected() {
        BleLog.dApp("isSibBluetoothConnected:判断是否已连接");
        LogUtils.i("---------" + GlucoseDataInfo.mNumUnReceived + "---------\n" + GlucoseDataInfo.mSyncTimeMill + "---------\n" + (System.currentTimeMillis() - 3 * 60000L));

        DebugFileUtils.setAppendFile("检查蓝牙是否断开：mNumUnReceived=" + GlucoseDataInfo.mNumUnReceived + "-----mSyncTimeMill：" + GlucoseDataInfo.mSyncTimeMill + "-----系统休眠时间：");

        BleLog.dApp("APP自己判断是否断连,isDeviceConnected:" + UserInfoUtils.isDeviceConnected);
        if (!UserInfoUtils.isDeviceConnected || GlucoseDataInfo.dataConnectionException()) {
            BleLog.dApp("APP自己综合判断(包括三分钟未同步数据),判定当前连接状态为:未连接");
            return false;
        }
        if (SensorDeviceInfo.mBluetoothDevice == null || ObjectUtils.isEmpty(SensorDeviceInfo.mDeviceName)) {
            BleLog.dApp("设备或设备名未空,判定当前连接状态为:未连接");
            return false;
        }
        boolean connect = mSiBluetooth.getDeviceStatus(SensorDeviceInfo.mBluetoothDevice, SensorDeviceInfo.mDeviceName);
        BleLog.dApp("APP自己无法判断为未连接,使用蓝牙库自己的方法判断是否连接:"+connect);
        return connect;
    }


    @Override
    public void releaseAlgorithmContext(String deviceName) {
        BleLog.dApp("releaseAlgorithmContext:准备算法?");
        if (mIAlgorithm == null) {
            mIAlgorithm = AlgorithmFactory.createAlgorithm(DeviceManager.getInstance().getDeviceEntity().getAlgorithmVersion());
        }
        if (ObjectUtils.isNotEmpty(deviceName)) {
            mIAlgorithm.releaseAlgorithmContext(UserInfoUtils.getUserId(), deviceName);
        }
        BleLog.dApp("releaseAlgorithmContext:将DeviceName置为空字符串");
        SensorDeviceInfo.mDeviceName = "";

    }

    @Override
    public void setStatusListener(CgmStatusListener statusListener) {
        BleLog.dApp("setStatusListener:设置状态监听-" + statusListener.getClass().getSimpleName());
        mStatusListener = statusListener;
    }

    @Override
    protected void resendReconnection() {
        BleLog.dReconnect("蓝牙状态为ON后,发送重连handler消息,延迟1500毫秒");
        mReconnectionHandler.removeCallbacksAndMessages(null);
        mReconnectionHandler.sendEmptyMessageDelayed(MSG_RECONNECT, 1500);
    }

    /**
     * 连接蓝牙
     */
    @Override
    public void connectBluetooth() {
        BleLog.dApp("开始运行蓝牙连接流程");
        DeviceEntity deviceEntity = SensorDeviceInfo.mDeviceEntity;
        LogUploadModel.getInstance().uploadConnectInfo("触发连接" + System.currentTimeMillis() + "mCurrentDeviceName=" + SensorDeviceInfo.mDeviceName + "------deviceName=" + deviceEntity.getDeviceName());
        LogUtils.i("-----------------------触发连接" + System.currentTimeMillis() + "mCurrentDeviceName=" + SensorDeviceInfo.mDeviceName + "------deviceName=" + deviceEntity.getDeviceName());

        String linkCode = deviceEntity.getBlueToothNum();
        String matchStr = linkCode.substring(0, 4);
        String deviceName = deviceEntity.getDeviceName();

        GlucoseThreadExecutor.getInstance().createOrRebuildThreadExecutor("connectBluetooth线程池中还有任务在执行。。。" + System.currentTimeMillis(),
                "connectBluetooth线程池中任务执行完毕。。。" + System.currentTimeMillis());

        //避免重复调用算法初始化代码
        if (SensorDeviceInfo.newSensor(deviceName)) {
            BleLog.dApp("新设备连接,deviceName为空或不同,初始化算法");
            LogUtils.e("-----------------------初始化算法");

            mIAlgorithm = AlgorithmFactory.createAlgorithm(deviceEntity.getAlgorithmVersion());
            if (mIAlgorithm == null) {
                disConnected(false);
                ToastUtils.showShort(R.string.please_upgrade_version);
                return;
            }

            SensorDeviceInfo.mDeviceName = deviceName;
            BleLog.dApp("新设备名:" + deviceName);

            LogUploadModel.getInstance().uploadConnectInfo("初始化算法：--------->算法版本=" + mIAlgorithm.getAlgorithmVersion() + "------连接码=" + linkCode);
            int result = mIAlgorithm.verifyLinkCode(linkCode);
            if (result != 1) {
                BleLog.dInterrupt("算法校验连接码不通过");
                ToastUtils.showShort(getString(R.string.common_connection_code_error));
                disConnected(false);
                return;
            }
            mIAlgorithm.initAlgorithmContext(deviceEntity);

        }
        LogUploadModel.getInstance().uploadConnectInfo("BLe connect start,App version name:" + AppUtils.getAppVersionName());
        BleLog.dApp("全部通过,准备调用蓝牙库连接蓝牙,当前蓝牙连接状态:"+CURRENT_CONNECT_STATUS);
        if (!BleUtils.getInstance().isBleEnabled()){
            BleUtils.getInstance().openBle();
            return;
        }
        if (CURRENT_CONNECT_STATUS == no.sisense.android.api.Constant.STATE_DISCONNECTED) {
            BleLog.eApp("当前状态为未连接,直接执行蓝牙库连接蓝牙");
            mSiBluetooth.BleBluthtoothConnectStart(matchStr, 1, 1);
        } else {
            //每次连接之前判断是否正在连接或者扫描，如果正在连接先主动断开一次，
            BleLog.eApp("蓝牙状态为非断开,先断开,再执行连接");
            stopConnect();
            new Handler().postDelayed(() -> mSiBluetooth.BleBluthtoothConnectStart(matchStr, 1, 1), 1000);
        }
    }

    private byte times = 0;

    private <T> void dealCompositedData(int index, T cgmRecord) {
        //数据索引
        BleLog.dGlucose("当前血糖index:" + index);

        //timeMillis = systemTimeMills + nextCollectionTime * 1000L - numOfUnreceived * 60 * 1000L;
        //timeMillis =当前时间-60*（index+剩余笔数）+60s;
        //  long timeMillis = cgmRecord.getTimeMillis();
        int numUnreceived = 0;
        if (cgmRecord instanceof GjCGMRecord) {
            numUnreceived = ((GjCGMRecord) cgmRecord).getNumOfUnreceived();
        } else if (cgmRecord instanceof CGMRecordV120) {
            numUnreceived = ((CGMRecordV120) cgmRecord).getReindex();
        }
        long timeMillis = System.currentTimeMillis() - 60 * 1000L * (index + numUnreceived) + 60 * 1000L;

        if (times < 30) {
            Log.d("蓝牙:传感器数据", "处理原始数据,最多打印30条,还剩多少数据:" + numUnreceived);
            times += 1;
        }
        GlucoseDataInfo.mNumUnReceived = numUnreceived;

        String userId = UserInfoUtils.getUserId();

        BloodGlucoseEntity glucoseEntity = BloodGlucoseEntityUtils.cgmRecordTransAppEntity(mIAlgorithm, cgmRecord, timeMillis, true);

        judgeSensorState(glucoseEntity, index, timeMillis);

        if (SensorDeviceInfo.notExpiredAndEnable(index)) {
            BleLog.dGlucose("index没有超过14天,也没有异常情况导致断开连接");
            AppDatabase.getInstance().runInTransaction(new Runnable() {
                @Override
                public void run() {
                    BleLog.dGlucose("将转换后的app可用的血糖数据,存入数据库");
                    //app数据库插入血糖数据
                    AppDatabase.getInstance().getBloodEntityDao().insert(glucoseEntity);
                    //保存算法中间变量
                    mIAlgorithm.saveAlgorithmContext(userId, SensorDeviceInfo.mDeviceName, index);
                    GlucoseDataInfo.mCurrentInsertIndex = glucoseEntity.getIndex();
                }
            });
            if (GlucoseDataInfo.needUpdateAgain(index) && SensorDeviceInfo.sensorInfoSure()) {
                BleLog.dGlucose("血糖接收后,设备及血糖满足上传要求,运行上传血糖逻辑");
                //未同步与不是最后一笔的情况下每5笔再上传一次血糖数据，因为设备失效是在20160的下一笔，
                // 而血糖数据的最后一笔是在20160，所以这里不能在20160上传血糖，否则失效了状态无法上传
                mServiceModel.getCurrentIndex(SensorDeviceInfo.mDeviceName, SensorDeviceInfo.mDeviceId, SensorDeviceInfo.mDeviceEntity.getAlgorithmVersion());
            }
        }

        if (SensorDeviceInfo.notException() && mStatusListener != null) {
            //传感器未损坏或过期的情况下将数据发送到首页
            ThreadUtils.runOnUiThread(() -> {
                if (mStatusListener != null) {
                    Log.v("蓝牙:线程池", "在ui线程,将接收到的血糖数据更新到首页");
                    mStatusListener.updateData(glucoseEntity);
                }
            });
        }

        SensorDeviceInfo.judgeSensorEnable();
    }

    private void judgeSensorState(BloodGlucoseEntity glucoseEntity, int index, long timeMills) {

        String userId = UserInfoUtils.getUserId();
        //电流报警状态
        int currentWarning = mIAlgorithm.getCurrentWarning();
        //传感器电流是否正常
        sensorIsDamagedInOneHour(index, currentWarning, userId);
        //根据时间做判断
        if (GlucoseDataInfo.isFirstData(index)) {
            DeviceRepository.getInstance().updateDeviceStatusAndFirstMill(userId, SensorDeviceInfo.mDeviceName, 3, 0, timeMills);
            LogUtils.e("激活时间------------------------" + timeMills);
        } else if (GlucoseDataInfo.isExactlyOneHour(index)) {
            //刚好使用一个小时
            LogUploadModel.getInstance().uploadConnectInfo("sensor index=60，" + SensorDeviceInfo.mDeviceName);
            DeviceRepository.getInstance().updateDeviceStatus(userId, SensorDeviceInfo.mDeviceName, 1, 0);
        } else if (GlucoseDataInfo.isExpired(index)) {
            //传感器失效(已停用)断开连接并更新状态为2
            LogUploadModel.getInstance().uploadConnectInfo("sensor valid:" + SensorDeviceInfo.mDeviceName);
            sensorIsExpire(userId, Constant.SENSOR_INVALID, index, GlucoseDataInfo.mNumUnReceived);
        } else {
            //正常时间使用中 TODO 这里还不是很明白
            setCurrentAlarmStatus(glucoseEntity, userId, SensorDeviceInfo.mDeviceName, currentWarning, index, GlucoseDataInfo.mNumUnReceived);
        }
    }

    @Override
    public void readCharacteristicWithBattery(BluetoothDevice bluetoothDevice, String s, String s1, String s2, String s3, String s4, String s5, String s6) {
        Map<String, String> characteristicMap = new HashMap<>();
        String bleProtocolVersion = null;
        if (!TextUtils.isEmpty(s1)) {
            //设备制造商
            characteristicMap.put("manufacturer", s1);
            BleLog.dLib("设备制造商:" + s1);
        }
        if (!TextUtils.isEmpty(s2)) {
            //型号，系列
            characteristicMap.put("model", s2);
        }
        if (!TextUtils.isEmpty(s3)) {
            //序列号
            characteristicMap.put("serial", s3);
        }
        if (!TextUtils.isEmpty(s4)) {
            //硬件版本号
            characteristicMap.put("hardware", s4);
        }
        if (!TextUtils.isEmpty(s5)) {
            //固件版本号
            characteristicMap.put("firmware", s5);
        }
        if (!TextUtils.isEmpty(s6)) {
            //软件版本号
            characteristicMap.put("software", s6);

            if (s6.contains("_")) {
                bleProtocolVersion = s6.split("_")[1];
            }
        }
        LogUtils.d("蓝牙协议", s + "," + bleProtocolVersion);
        //预先保存这些信息,实际连接不一定成功
        //如果当前设备名中包含有当前连接码的前四位才给赋值
        if (SensorDeviceInfo.mDeviceEntity != null && ObjectUtils.isNotEmpty(s) && ObjectUtils.isNotEmpty(SensorDeviceInfo.mDeviceEntity.getBlueToothNum())
                && s.contains(SensorDeviceInfo.mDeviceEntity.getBlueToothNum().substring(0, 4))) {
            SensorDeviceInfo.mDeviceName = s;
            SensorDeviceInfo.mDeviceEntity.setDeviceName(SensorDeviceInfo.mDeviceName);
            SensorDeviceInfo.mMacAddress = bluetoothDevice.getAddress();
            SensorDeviceInfo.mDeviceEntity.setMacAddress(SensorDeviceInfo.mMacAddress);
            String algorithmVersion = mIAlgorithm.getAlgorithmVersion();
            SensorDeviceInfo.mDeviceEntity.setConnectMill(System.currentTimeMillis());
            SensorDeviceInfo.mDeviceEntity.setAlgorithmVersion(algorithmVersion);
            String characteristic = GsonUtils.toJson(characteristicMap);
            SensorDeviceInfo.mDeviceEntity.setCharacteristic(characteristic);
        }
    }

    private void deviceConnectable() {
        BleLog.dLib("所有程序正常,走正常连接逻辑");
        if (SensorDeviceInfo.mDeviceEntity != null) {
            //校验全部通过,可以保存
            DeviceManager.getInstance().setDeviceEntity(SensorDeviceInfo.mDeviceEntity);
        }

        if (SensorDeviceInfo.mDeviceEntity != null) {
            if (ObjectUtils.isEmpty(SensorDeviceInfo.mDeviceId)) {
                //没有设备id直接新增设备
                String bluetoothNum = SensorDeviceInfo.mDeviceEntity.getBlueToothNum();
                DeviceManager.getInstance().insertDevice(SensorDeviceInfo.mDeviceEntity);
                LogUtils.e("------------------------>新增设备" + SensorDeviceInfo.mDeviceEntity.getDeviceName());
                mServiceModel.addDevice(bluetoothNum, SensorDeviceInfo.mMacAddress, SensorDeviceInfo.mDeviceName, mIAlgorithm.getAlgorithmVersion());
            } else {
                //更新设备连接时间
                DeviceRepository.getInstance().updateConnectMillAndCharacteristic(UserInfoUtils.getUserId(), SensorDeviceInfo.mDeviceName, System.currentTimeMillis(), SensorDeviceInfo.mDeviceEntity.getCharacteristic());
                LogUtils.e("------------------------>更新设备时间" + SensorDeviceInfo.mDeviceEntity.getDeviceName());
            }
        }

        connected();

        //蓝牙版本号和产品线
        String bleSdkVersion = mSiBluetooth.getSDKVersionMessage();
        String productLine = SisenseBluetooth.getProductLine();
        LogUploadModel.getInstance().uploadConnectInfo("Ble SDK Version:" + bleSdkVersion + ",Product Line:" + productLine + ",DeviceName:" + SensorDeviceInfo.mDeviceName);
    }

    @Override
    public <T> void CGMSystemMessage(BluetoothDevice bluetoothDevice, int i, T t) {
        BleLog.dLib("接收到发射板系统信息," + getDeviceName(bluetoothDevice));
//        if (ObjectUtils.isNotEmpty(userId) && ObjectUtils.isNotEmpty(mDeviceId) && ObjectUtils.isNotEmpty(mDeviceName)) {
//            if (t instanceof DeviceResetResult) {
//                String mmkvName = userId + mDeviceName;
//                DeviceResetResult deviceResetResult = (DeviceResetResult) t;
//                long resetTimes = deviceResetResult.getU8reset_times();
//                long localResetTimes = UserInfoUtils.getDeviceRestartCount(mmkvName);
//                if (resetTimes != 0 && localResetTimes != resetTimes) {
//                    mServiceModel.updateDeviceRestartCount(mmkvName, mDeviceId, resetTimes);
//                }
//            }
//        }

    }

    @Override
    public void scanError(int i) {
        BleLog.dLib("scanError:" + i);
        LogUploadModel.getInstance().uploadConnectInfo("Ble Scan Error: " + i);
        ThreadUtils.runOnUiThread(() -> ToastUtils.showShort(getString(R.string.personalcenter_sensor_connect_fail)));
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        //重启服务
        ThreadUtils.runOnUiThread(() -> {
            BleLog.eLib("蓝牙库服务被销毁,重新初始化蓝牙库服务");
            CURRENT_CONNECT_STATUS = no.sisense.android.api.Constant.STATE_DISCONNECTED;
            bleInit();
            dealDisconnect();
            LogUploadModel.getInstance().uploadConnectInfo("Ble service disconnected:" + SensorDeviceInfo.mDeviceName);
        });
    }

    /**
     * 处理断开后的逻辑
     */
    private void dealDisconnect() {
        // 检查 WakeLock 是否已经被获取
        BleLog.dApp("isDeviceConnected赋值为false");
        UserInfoUtils.isDeviceConnected = false;
        disConnected(true);
        BleLog.dReconnect("连接断开后的处理,发送延迟消息重连,延迟:"+(mReconnectMills/1000), true);
        mReconnectionHandler.removeCallbacksAndMessages(null);
        mReconnectionHandler.sendEmptyMessageDelayed(MSG_RECONNECT, mReconnectMills);
    }

    /**
     * @return 获取蓝牙连接状态
     */
    public int getConnectStatus() {
        return CURRENT_CONNECT_STATUS;
    }

    @Override
    public void onDestroy() {
        BleLog.eApp("APP蓝牙Service被销毁");
        super.onDestroy();
        mSiBluetooth.removeSibListener();
    }
}
