package com.ljwx.basemodule.ble;//package com.sisensing.common.ble;
//
//import android.bluetooth.BluetoothDevice;
//
//import com.blankj.utilcode.util.LogUtils;
//import com.blankj.utilcode.util.ObjectUtils;
//import com.blankj.utilcode.util.ThreadUtils;
//import com.blankj.utilcode.util.TimeUtils;
//import com.blankj.utilcode.util.ToastUtils;
//import com.sisensing.common.R;
//import com.sisensing.common.algorithom.AlgorithmFactory;
//import com.sisensing.common.algorithom.IAlgorithm;
//import com.sisensing.common.ble.v4.extract.LocalBleService5Abstract;
//import com.sisensing.common.ble.v4.data.GlucoseDataInfo;
//import com.sisensing.common.ble.v4.GlucoseThreadExecutor;
//import com.sisensing.common.ble.v4.data.SensorDeviceInfo;
//import com.sisensing.common.constants.Constant;
//import com.sisensing.common.constants.DailyTrendConst;
//import com.sisensing.common.database.AppDatabase;
//import com.sisensing.common.database.RoomTask;
//import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
//import com.sisensing.common.ble.v4.BloodGlucoseEntityUtils;
//import com.sisensing.common.entity.Device.DeviceEntity;
//import com.sisensing.common.entity.Device.DeviceManager;
//import com.sisensing.common.entity.Device.DeviceRepository;
//import com.sisensing.common.share.LogUploadModel;
//import com.sisensing.common.user.UserInfoUtils;
//import com.sisensing.common.utils.BroadcastManager;
//import com.sisensing.common.utils.DebugFileUtils;
//import com.sisensing.common.utils.LocationUtils;
//import com.sisensing.common.utils.Log;
//
//import no.sisense.android.api.SisenseBluetooth;
//import no.sisense.android.bean.GjCGMRecord;
//
//
//public abstract class LocalBleServiceV2 extends LocalBleService5Abstract implements IBle {
//
//    private SisenseBluetooth mSiBluetooth;
//
//    private IAlgorithm mIAlgorithm;
//
//    private CgmStatusListener mStatusListener;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        initSiBluetooth();
//    }
//
//    /**
//     * 初始化远程蓝牙连接
//     */
//    private void initSiBluetooth() {
//        Log.d("蓝牙", "初始化jar包蓝牙");
//        mSiBluetooth = SisenseBluetooth.getInstance(this);
//        mSiBluetooth.setAutoConnectFlag(false);
//        mSiBluetooth.setOnSibListener(this);
//    }
//
//    @Override
//    public void onDataRecive(GjCGMRecord gjCGMRecord) {
//        Log.v("蓝牙:jar包回调", "onDataReceive:接收数据:" + TimeUtils.getNowString());
//        GlucoseThreadExecutor.getInstance().execute(() -> {
//            int index = gjCGMRecord.getIndex();
//            if (index == GlucoseDataInfo.mCurrentInsertIndex + 1) {
//                //index从小往大
//                Log.v("蓝牙:线程池", "jar包传的血糖数据正常处理:index:"+index);
//                // 连续index，正常
//                dealCompositedData(gjCGMRecord);
//            } else if (index > GlucoseDataInfo.mCurrentInsertIndex + 1) {
//                // 发生index丢失，断开蓝牙
//                Log.d("蓝牙:线程池", "index大于已插入的,则若sibBlue还在连接,则停止连接");
//                LogUploadModel.getInstance().uploadConnectInfo("发生index丢失--mCurrentInsertIndex=" + GlucoseDataInfo.mCurrentInsertIndex + "--------------getIndex=" + gjCGMRecord.getIndex());
//                if (isSibBluetoothConnected()) {
//                    stopConnect();
//                }
//            } else {
//                Log.d("蓝牙:线程池", "index重复了,重复数据不处理");
//                // index重复不处理
//                LogUploadModel.getInstance().uploadConnectInfo("发生index重复，不处理--mCurrentInsertIndex=" + GlucoseDataInfo.mCurrentInsertIndex + "--------------getIndex=" + gjCGMRecord.getIndex());
//            }
//        });
//
//    }
//
//
//    @Override
//    public void onServieDiscovered() {
//        Log.d("蓝牙:jar包回调", "onServieDiscovered:服务被发现?");
//        LogUtils.i("onServieDiscovered：");
//        //创建线程池
//        GlucoseThreadExecutor.getInstance().createOrRebuildThreadExecutor("onServieDiscovered线程池中还有任务在执行。。。" + System.currentTimeMillis(),
//                "onServieDiscovered线程池中任务执行完毕。。。" + System.currentTimeMillis());
//        //从数据库获取最后一笔血糖数据
//        RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().getLastBloodGlucose(UserInfoUtils.getUserId(), SensorDeviceInfo.mDeviceName), bloodGlucoseEntity -> {
//            //本地存储的血糖数据最后一个索引值
//            int lastIndex = 0;
//            if (ObjectUtils.isNotEmpty(bloodGlucoseEntity)) {
//                lastIndex = bloodGlucoseEntity.getIndex();
//                Log.d("蓝牙:jar包回调", "onServieDiscovered:从数据库获取最后一笔血糖index:" + lastIndex);
//                GlucoseDataInfo.mProcessedTimeMill = bloodGlucoseEntity.getProcessedTimeMill();
//            }
//            GlucoseDataInfo.mCurrentInsertIndex = lastIndex;
//            LogUploadModel.getInstance().uploadConnectInfo("-----------------------本地存储的血糖对应index=" + GlucoseDataInfo.mCurrentInsertIndex);
//            Log.d("蓝牙:jar包回调", "onServieDiscovered:从传感器获取index的下一条数据");
//            mSiBluetooth.getDataSugar(++lastIndex);
//        });
//    }
//
//
//    @Override
//    public void onConnectionSate(BluetoothDevice bluetoothDevice, int status) {
//        Log.d("蓝牙:jar包回调", "onConnectionSate:状态改变");
//        switch (status) {
//            case SisenseBluetooth.STATE_CONNECTED:
//                Log.d("蓝牙:jar包回调", "onConnectionSate:连接成功");
//                UserInfoUtils.isDeviceConnected = true;
//                SensorDeviceInfo.isAcDis = false;
//                addDevice(bluetoothDevice);
//                connected();
//                break;
//            case SisenseBluetooth.STATE_DISCONNECTED:
//                Log.d("蓝牙:jar包回调", "onConnectionSate:连接断开");
//                UserInfoUtils.isDeviceConnected = false;
//                if (SensorDeviceInfo.couldReconnection() && UserInfoUtils.isLogin()) {
//                    sendReconnectionMessage();
//                }
//                disConnected(true);
//                break;
//            case SisenseBluetooth.STATE_CONNECTING:
//                Log.d("蓝牙:jar包回调", "onConnectionSate:连接中");
//                if (BleUtils.getInstance().isBleEnabled()) {
//                    if (LocationUtils.isOpenLocation(this)) {
//                        Log.d("蓝牙:jar包回调", "onConnectionSate:定位已打开,正在连接");
//                        connecting();
//                    } else {
//                        Log.d("蓝牙:jar包回调", "onConnectionSate:定位未打开,停止连接");
//                        stopConnect();
//                        BroadcastManager.getInstance(this).sendBroadcast(Constant.BROADCAST_BLUETOOTH_OPEN_LOCATION_NOT_OPEN);
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 蓝牙连接成功后调用新增设备接口
//     */
//    private void addDevice(BluetoothDevice bluetoothDevice) {
//        Log.d("蓝牙:jar包回调", "蓝牙正常");
//        if (ObjectUtils.isNotEmpty(bluetoothDevice)) {
//            SensorDeviceInfo.addDevice(bluetoothDevice, mIAlgorithm);
//            String bluetoothNum = SensorDeviceInfo.mDeviceEntity.getBlueToothNum();
//            String algorithmVersion = mIAlgorithm.getAlgorithmVersion();
//            if (SensorDeviceInfo.mDeviceEntity != null) {
//                //如果是新设备,则插入数据库,并调用新增设备接口
//                //否则更新连接时间
//                if (!SensorDeviceInfo.isConnectedDeviceAndUpdateTime()) {
//                    //没有设备id直接新增设备
//                    Log.d("蓝牙:jar包回调", "数据库新增设备,并调用接口,userId:" + SensorDeviceInfo.mDeviceEntity.getUserId()
//                            + "-设备名:" + SensorDeviceInfo.mDeviceEntity.getDeviceName());
//                    DeviceManager.getInstance().insertDevice(SensorDeviceInfo.mDeviceEntity);
//                    LogUtils.i("------------------------>新增设备" + SensorDeviceInfo.mDeviceEntity.getDeviceName());
//                    mServiceModel.addDevice(bluetoothNum, SensorDeviceInfo.mMacAddress, SensorDeviceInfo.mDeviceName, algorithmVersion);
//                }
//            }
//        }
//    }
//
//    /**
//     * deviceName: 不能为空,需要连接的BLE设备名称
//     * delayMillis: 不能为空,首次连接或者异常重新连接后,执行扫描的最长时间
//     * mCallback: 不能为空,回调接口,连接状态、数据传输、连接异常等信息
//     * Action：  不能为空, 2--开始连接；1--停止连接
//     *
//     * @param
//     */
//    @Override
//    public void startConnect(DeviceEntity deviceEntity) {
//        Log.d("蓝牙:app自己方法", "startConnect:开始连接,更新deviceEntity,deviceId:" + deviceEntity.getDeviceId());
//        SensorDeviceInfo.isRightNowStopData = false;
//        SensorDeviceInfo.mDeviceEntity = deviceEntity;
//        SensorDeviceInfo.mDeviceId = deviceEntity.getDeviceId();
//
//        mReconnectMills = 1500L;
//
//        if (!BleUtils.getInstance().isBleEnabled()) {
//            Log.d("蓝牙:app自己方法", "startConnect:蓝牙不可用,去打开蓝牙");
//            BleUtils.getInstance().openBle();
//        } else {
//            Log.d("蓝牙:app自己方法", "startConnect:蓝牙可用,去连接蓝牙");
//            connectBluetooth();
//        }
//    }
//
//    /**
//     * 断开蓝牙服务连接释放资源
//     */
//    @Override
//    public void stopConnect() {
//        Log.d("蓝牙:app自己方法", "stopConnect:调用siBlue不连接");
//        SensorDeviceInfo.isAcDis = true;
//        mSiBluetooth.BleBluthtoothDisConnect(1);
//    }
//
//
//    /**
//     * 判断蓝牙服务是否处于连接状态
//     *
//     * @return
//     */
//    @Override
//    public boolean isSibBluetoothConnected() {
//        Log.d("蓝牙:app自己方法", "isSibBluetoothConnected:判断是否已连接");
//        LogUtils.i("---------" + GlucoseDataInfo.mNumUnReceived + "---------\n" + GlucoseDataInfo.mSyncTimeMill + "---------\n" + (System.currentTimeMillis() - 3 * 60000L));
//
//        DebugFileUtils.setAppendFile("检查蓝牙是否断开：mNumUnReceived=" + GlucoseDataInfo.mNumUnReceived + "-----mSyncTimeMill：" + GlucoseDataInfo.mSyncTimeMill + "-----系统休眠时间：");
//
//        if (!UserInfoUtils.isDeviceConnected || GlucoseDataInfo.dataConnectionException()) {
//            Log.d("蓝牙:app自己方法", "isSibBluetoothConnected:连接标志false,或者三分钟没有数据传输,都算断开");
//            return false;
//        }
//        return mSiBluetooth.getIsConnected();
//    }
//
//
//    @Override
//    public void releaseAlgorithmContext(String deviceName) {
//        Log.d("蓝牙:app自己方法", "releaseAlgorithmContext:准备算法?");
//        if (mIAlgorithm == null) {
//            mIAlgorithm = AlgorithmFactory.createAlgorithm(DeviceManager.getInstance().getDeviceEntity().getAlgorithmVersion());
//        }
//        if (ObjectUtils.isNotEmpty(deviceName)) {
//            mIAlgorithm.releaseAlgorithmContext(UserInfoUtils.getUserId(), deviceName);
//        }
//        Log.d("蓝牙:app自己方法", "releaseAlgorithmContext:将DeviceName置为空");
//        SensorDeviceInfo.mDeviceName = "";
//
//    }
//
//    @Override
//    public void setStatusListener(CgmStatusListener statusListener) {
//        Log.d("蓝牙:app自己方法", "setStatusListener:设置状态监听");
//        mStatusListener = statusListener;
//    }
//
//    /**
//     * 连接蓝牙
//     */
//    @Override
//    public void connectBluetooth() {
//        DeviceEntity deviceEntity = SensorDeviceInfo.mDeviceEntity;
//        LogUploadModel.getInstance().uploadConnectInfo("触发连接" + System.currentTimeMillis() + "mCurrentDeviceName=" + SensorDeviceInfo.mDeviceName + "------deviceName=" + deviceEntity.getDeviceName());
//        LogUtils.e("-----------------------触发连接" + System.currentTimeMillis() + "mCurrentDeviceName=" + SensorDeviceInfo.mDeviceName + "------deviceName=" + deviceEntity.getDeviceName());
//
//        String linkCode = deviceEntity.getBlueToothNum();
//        String matchStr = linkCode.substring(0, 4);
//        String deviceName = deviceEntity.getDeviceName();
//
//        GlucoseThreadExecutor.getInstance().createOrRebuildThreadExecutor("connectBluetooth线程池中还有任务在执行。。。" + System.currentTimeMillis(),
//                "connectBluetooth线程池中任务执行完毕。。。" + System.currentTimeMillis());
//
//        //避免重复调用算法初始化代码
//        if (SensorDeviceInfo.newSensor(deviceName)) {
//            Log.d("蓝牙:连接蓝牙", "新设备连接,deviceName不同:初始化算法");
//            LogUtils.e("-----------------------初始化算法");
//
//            mIAlgorithm = AlgorithmFactory.createAlgorithm(deviceEntity.getAlgorithmVersion());
//            if (mIAlgorithm == null) {
//                disConnected(false);
//                ToastUtils.showShort(R.string.please_upgrade_version);
//                return;
//            }
//
//            SensorDeviceInfo.mDeviceName = deviceName;
//            Log.d("蓝牙:连接蓝牙", "新设备名:" + deviceName);
//
//            LogUploadModel.getInstance().uploadConnectInfo("初始化算法：--------->算法版本=" + mIAlgorithm.getAlgorithmVersion() + "------连接码=" + linkCode);
//            int result = mIAlgorithm.verifyLinkCode(linkCode);
//            if (result != 1) {
//                ToastUtils.showShort(getString(R.string.common_connection_code_error));
//                disConnected(false);
//                return;
//            }
//            mIAlgorithm.initAlgorithmContext(deviceEntity);
//
//        }
//        Log.d("蓝牙:jar包", "开始连接蓝牙");
//        mSiBluetooth.BleBluthtoothConnectStart(matchStr, 60 * 1000, null, 2);
//    }
//
//    private byte times = 0;
//
//    private void dealCompositedData(GjCGMRecord cgmRecord) {
//        //数据索引
//        int index = cgmRecord.getIndex();
//
//        //timeMillis = systemTimeMills + nextCollectionTime * 1000L - numOfUnreceived * 60 * 1000L;
//        //timeMillis =当前时间-60*（index+剩余笔数）+60s;
//        //  long timeMillis = cgmRecord.getTimeMillis();
//        int numUnreceived = cgmRecord.getNumOfUnreceived();
//        long timeMillis = System.currentTimeMillis() - 60 * 1000L * (index + numUnreceived) + 60 * 1000L;
//
//        if (times < 30) {
//            Log.d("蓝牙:传感器数据", "处理原始数据,最多打印30条,还剩多少数据:" + numUnreceived);
//            times += 1;
//        }
//        GlucoseDataInfo.mNumUnReceived = numUnreceived;
//
//        String userId = UserInfoUtils.getUserId();
//
//        BloodGlucoseEntity glucoseEntity = BloodGlucoseEntityUtils.cgmRecordTransAppEntity(mIAlgorithm, cgmRecord, timeMillis);
//
//        judgeSensorState(glucoseEntity, index, timeMillis);
//
//        if (SensorDeviceInfo.notExpiredAndEnable(index)) {
//            Log.v("蓝牙:线程池", "index没有超过14天,也没有异常情况导致断开连接");
//            AppDatabase.getInstance().runInTransaction(new Runnable() {
//                @Override
//                public void run() {
//                    Log.v("蓝牙:线程池", "将转换后的app可用的血糖数据,存入数据库");
//                    //app数据库插入血糖数据
//                    AppDatabase.getInstance().getBloodEntityDao().insert(glucoseEntity);
//                    //保存算法中间变量
//                    mIAlgorithm.saveAlgorithmContext(userId, SensorDeviceInfo.mDeviceName, index);
//                    GlucoseDataInfo.mCurrentInsertIndex = glucoseEntity.getIndex();
//                }
//            });
//            if (GlucoseDataInfo.needUpdateAgain(index) && SensorDeviceInfo.sensorInfoSure()) {
//                //未同步与不是最后一笔的情况下每5笔再上传一次血糖数据，因为设备失效是在20160的下一笔，
//                // 而血糖数据的最后一笔是在20160，所以这里不能在20160上传血糖，否则失效了状态无法上传
//                mServiceModel.getCurrentIndex(SensorDeviceInfo.mDeviceName, SensorDeviceInfo.mDeviceId, SensorDeviceInfo.mDeviceEntity.getAlgorithmVersion());
//            }
//        }
//
//        if (SensorDeviceInfo.notException() && mStatusListener != null) {
//            //传感器未损坏或过期的情况下将数据发送到首页
//            ThreadUtils.runOnUiThread(() -> {
//                if (mStatusListener != null) {
//                    Log.v("蓝牙:线程池", "在ui线程,将接收到的血糖数据更新到首页");
//                    mStatusListener.updateData(glucoseEntity);
//                }
//            });
//        }
//        SensorDeviceInfo.judgeSensorEnable();
//    }
//
//    private void judgeSensorState(BloodGlucoseEntity glucoseEntity, int index, long timeMills) {
//
//        String userId = UserInfoUtils.getUserId();
//        //电流报警状态
//        int currentWarning = mIAlgorithm.getCurrentWarning();
//        //传感器电流是否正常
//        sensorIsDamagedInOneHour(index, currentWarning, userId);
//        //根据时间做判断
//        if (GlucoseDataInfo.isFirstData(index)) {
//            DeviceRepository.getInstance().updateDeviceStatusAndFirstMill(userId, SensorDeviceInfo.mDeviceName, 3, 0, timeMills);
//            LogUtils.e("激活时间------------------------" + timeMills);
//        } else if (GlucoseDataInfo.isExactlyOneHour(index)) {
//            //刚好使用一个小时
//            DeviceRepository.getInstance().updateDeviceStatus(userId, SensorDeviceInfo.mDeviceName, 1, 0);
//        } else if (GlucoseDataInfo.isExpired(index)) {
//            //传感器失效(已停用)断开连接并更新状态为2
//            sensorIsExpire(userId, Constant.SENSOR_INVALID, index, GlucoseDataInfo.mNumUnReceived);
//        } else {
//            //正常时间使用中 TODO 这里还不是很明白
//            setCurrentAlarmStatus(glucoseEntity, userId, SensorDeviceInfo.mDeviceName, currentWarning, index, GlucoseDataInfo.mNumUnReceived);
//        }
//    }
//
//}
