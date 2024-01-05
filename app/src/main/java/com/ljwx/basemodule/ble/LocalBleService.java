package com.ljwx.basemodule.ble;//package com.sisensing.common.ble;
//
//import android.app.Notification;
//import android.app.Service;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Binder;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Looper;
//import android.os.Message;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.blankj.utilcode.util.LogUtils;
//import com.blankj.utilcode.util.ObjectUtils;
//import com.blankj.utilcode.util.ScreenUtils;
//import com.blankj.utilcode.util.ThreadUtils;
//import com.blankj.utilcode.util.ToastUtils;
//import com.sisensing.common.R;
//import com.sisensing.common.algorithom.AlgorithmFactory;
//import com.sisensing.common.algorithom.IAlgorithm;
//import com.sisensing.common.constants.Constant;
//import com.sisensing.common.database.AppDatabase;
//import com.sisensing.common.database.RoomTask;
//import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
//import com.sisensing.common.entity.Device.DeviceEntity;
//import com.sisensing.common.entity.Device.DeviceManager;
//import com.sisensing.common.entity.Device.DeviceRepository;
//import com.sisensing.common.notification.NotificationClickReceiver;
//import com.sisensing.common.notification.NotificationConfig;
//import com.sisensing.common.notification.NotificationUtils;
//import com.sisensing.common.share.LogUploadModel;
//import com.sisensing.common.user.UserInfoUtils;
//import com.sisensing.common.utils.BroadcastManager;
//import com.sisensing.common.utils.ConfigUtils;
//import com.sisensing.common.utils.DebugFileUtils;
//import com.sisensing.common.utils.LocationUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//import no.nordicsemi.android.support.v18.scanner.ScanResult;
//import no.sisense.android.api.CGMService;
//import no.sisense.android.api.SisenseBluetooth;
//import no.sisense.android.bean.CGMRecord;
//import no.sisense.android.bean.GjCGMRecord;
//import no.sisense.android.callback.ConnectListener;
//
//
//public class LocalBleService extends Service implements ConnectListener, IBle {
//
//    private BluetoothServiceModel mServiceModel;
//    //传感器是否过期或者损坏
//    private boolean sensorIsEdOrEp = false;
//    //ble设备地址
//    private String mMacAddress;
//    //ble设备名称
//    private String mDeviceName;
//
//    private DeviceEntity mDeviceEntity;
//
//    private SisenseBluetooth mSiBluetooth;
//
//    //创建单线程化线程池
//    private ExecutorService mSingleThreadExecutor;
//
//    private IAlgorithm mIAlgorithm;
//
//    //是否立刻终止发送数据(主要是为了解决当异常情况下断开设备连接需要即时中断数据发送到主界面)
//    private boolean isRightNowStopData = false;
//
//    private List<CgmConnectListener> mConnectListener = new ArrayList<>();
//
//    private CgmStatusListener mStatusListener;
//
//    //当前插入数据库的血糖index标识
//    private int mCurrentInsertIndex;
//
//    private String mDeviceId;
//    //最新血糖时间戳
//    private long mProcessedTimeMill;
//    //血糖同步时间戳，以此通过血糖同步时间差判断蓝牙是否短卡（针对蓝牙被kill拿不到回调的解决办法）
//    private long mSyncTimeMill;
//
//
//    private long mReconnectMills = 1500L;
//    private int mNumUnReceived = 0;
//
//    private static final int MSG_RECONNECT = 1000;
//
//    //是否主动断开连接
//    private boolean isAcDis = false;
//
//
//    @Override
//    public void onCreate() {
//        init();
//    }
//
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }
//
//    private void init() {
//
//        initSiBluetooth();
//
//        initNotification();
//
//        Log.d("蓝牙", "创建蓝牙服务model");
//        mServiceModel = new BluetoothServiceModel();
//
//        IntentFilter bleEnableFilter = new IntentFilter();
//        bleEnableFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        registerReceiver(blueToothStateReceiver, bleEnableFilter);
//
//        //app启动后就开始接收此广播来调用一些需要启动就要调的接口
//        BroadcastManager.getInstance(this).addAction(Constant.BROADCAST_INIT_DEVICE, new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d("蓝牙", "收到app启动广播");
//                Log.d("蓝牙", "从数据库通过用户id查询设备信息");
//                DeviceEntity entity = DeviceManager.getInstance().getDeviceEntity();
//                if (entity != null && ObjectUtils.isNotEmpty(entity.getDeviceName()) && ObjectUtils.isNotEmpty(entity.getDeviceId())) {
//                    Log.d("蓝牙", "设备名,设备id都不为空,则上传行为数据,上传血糖数据");
//                    String deviceName = entity.getDeviceName();
//                    String deviceId = entity.getDeviceId();
//                    mServiceModel.getClockInData(deviceId);
//                    mServiceModel.getCurrentIndex(deviceName, deviceId, entity.getAlgorithmVersion());
//                }
//
//            }
//        });
//
//    }
//
//    private Handler mHandler = new Handler(Looper.myLooper()) {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//
//            switch (msg.what) {
//                case 1000:
//                    Log.d("蓝牙", "handle收到message,马上重连");
//                    disConnectReConnect();
//                    break;
//            }
//        }
//    };
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return new LocalBinder();
//    }
//
//
//    public class LocalBinder extends Binder {
//        public LocalBleService getService() {
//            return LocalBleService.this;
//        }
//    }
//
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
//    /**
//     * 初始化通知服务
//     */
//    private void initNotification() {
//        Log.d("蓝牙", "初始化前台通知:Continuous Glucose Monitoring System");
//        //启用前台服务，提升优先级
//        Intent intent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
//        intent.setAction(NotificationClickReceiver.CLICK_NOTIFICATION);
//        Notification notification = NotificationUtils.createNotification(this, getString(NotificationConfig.TITLE), getString(NotificationConfig.CONTENT), NotificationConfig.DEF_ICONS, intent);
//        startForeground(NotificationConfig.FOREGROUD_NOTIFICATION_ID, notification);
//
//    }
//
//
//    /**
//     * 蓝牙开关广播监听
//     */
//    private final BroadcastReceiver blueToothStateReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
//                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 1000);
//                switch (state) {
//                    case BluetoothAdapter.STATE_OFF://蓝牙已关闭
//                        LogUtils.e("蓝牙关闭");
//                        Log.d("蓝牙:系统广播", "蓝牙关闭");
//                        UserInfoUtils.isDeviceConnected = false;
//                        DebugFileUtils.setAppendFile("监测到蓝牙关闭：bleUtils is enable" + BleUtils.getInstance().isBleEnabled() + "-----屏幕是否关闭：" + ScreenUtils.isScreenLock() + "-----系统休眠时间：" + ScreenUtils.getSleepDuration());
//                        if (ObjectUtils.isEmpty(mDeviceName)) {
//                            Log.d("蓝牙:系统广播", "设备名为空:去开启蓝牙");
//                            BleUtils.getInstance().openBle();
//                        } else {
//                            Log.d("蓝牙:系统广播", "设备名不为空:通知监听者");
//                            disConnected(true);
//                        }
//                        break;
//                    case BluetoothAdapter.STATE_ON://蓝牙已开启
//                        Log.d("蓝牙:系统广播", "蓝牙开启");
//                        if (mDeviceEntity != null && !sensorIsEdOrEp) {
//                            Log.d("蓝牙:系统广播", "已经连接过,且设备未过期:去连接设备");
//                            connectBluetooth(mDeviceEntity);
//                        }
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_ON://蓝牙正在打开
//                        Log.d("蓝牙:系统广播", "蓝牙正在打开");
//                        LogUtils.e("蓝牙正在打开");
//                        break;
//                    case BluetoothAdapter.STATE_TURNING_OFF://蓝牙正在关闭
//                        Log.d("蓝牙:系统广播", "蓝牙正在关闭");
//                        LogUtils.e("蓝牙正在关闭");
//                        break;
//                    default:
//                        Log.d("蓝牙:系统广播", "蓝牙未知状态");
//                        Log.e("BlueToothError", "蓝牙状态未知");
//                }
//            }
//        }
//    };
//
//
//    private void disConnected(boolean showToast) {
//        Log.d("蓝牙", "各种原因,通知监听者未连接");
//        for (CgmConnectListener connectListener : mConnectListener) {
//            connectListener.disConnected(showToast);
//        }
//    }
//
//
//    private void connecting() {
//        Log.d("蓝牙", "各种原因,通知监听者连接中");
//        for (CgmConnectListener connectListener : mConnectListener) {
//            connectListener.connecting();
//        }
//    }
//
//    private void connected() {
//        Log.d("蓝牙", "各种原因,通知监听者连接成功");
//        for (CgmConnectListener connectListener : mConnectListener) {
//            connectListener.connected();
//        }
//    }
//
//
//    @Override
//    public void onScanFailed(int i, String s) {
//        Log.d("蓝牙:jar包回调", "onScanFailed:扫描失败");
//        LogUploadModel.getInstance().uploadConnectInfo("onScanFailed:i=" + i + ",s=" + s);
//    }
//
//    @Override
//    public void onServiceBound(CGMService.CGMSBinder cgmsBinder) {
//        Log.d("蓝牙:jar包回调", "onServiceBound:");
//        LogUtils.e("onServiceBound：" + cgmsBinder.getDeviceName() + "------------>" + cgmsBinder.getDeviceAddress());
//    }
//
//    @Override
//    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//        Log.d("蓝牙:jar包回调", "onServiceConnected:服务已连接");
//        LogUtils.e("onServiceConnected：");
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName componentName) {
//        Log.d("蓝牙:jar包回调", "onServiceDisconnected:服务断开");
//        LogUtils.e("onServiceDisconnected：" + componentName);
//    }
//
//    @Override
//    public void onDataRecive(ArrayList<GjCGMRecord> arrayList) {
//
//    }
//
//    @Override
//    public void onDataRecive(GjCGMRecord gjCGMRecord) {
//        Log.d("蓝牙:jar包回调", "onDataReceive:接收数据index:"+ gjCGMRecord.index);
//        mSingleThreadExecutor.execute(new HandleCgmDataTask(gjCGMRecord));
//    }
//
//
//    @Override
//    public void onDataRecive(byte[] bytes) {
//
//    }
//
//    @Override
//    public void onServieDiscovered() {
//
//        Log.d("蓝牙:jar包回调", "onServieDiscovered:服务被发现?");
//        LogUtils.e("onServieDiscovered：");
//
//        createOrRebuildThreadExecutor("onServieDiscovered线程池中还有任务在执行。。。" + System.currentTimeMillis(),
//                "onServieDiscovered线程池中任务执行完毕。。。" + System.currentTimeMillis());
//
//        RoomTask.singleTask(AppDatabase.getInstance().getBloodEntityDao().getLastBloodGlucose(UserInfoUtils.getUserId(), mDeviceName), bloodGlucoseEntity -> {
//            //本地存储的血糖数据最后一个索引值
//            int lastIndex = 0;
//            if (ObjectUtils.isNotEmpty(bloodGlucoseEntity)) {
//                lastIndex = bloodGlucoseEntity.getIndex();
//                mProcessedTimeMill = bloodGlucoseEntity.getProcessedTimeMill();
//            }
//            mCurrentInsertIndex = lastIndex;
//
//            LogUploadModel.getInstance().uploadConnectInfo("-----------------------本地存储的血糖对应index=" + mCurrentInsertIndex);
//
//            mSiBluetooth.getDataSugar(++lastIndex);
//        });
//    }
//
//    @Override
//    public void onBroadcatNewCGMValue(BluetoothDevice bluetoothDevice, CGMRecord cgmRecord) {
//        LogUtils.e("onBroadcatNewCGMValue：" + bluetoothDevice.getName() + "---------------->" + bluetoothDevice.getAddress());
//    }
//
//    @Override
//    public void onBroadcastError(BluetoothDevice bluetoothDevice, String s, int i) {
//        LogUtils.e("onBroadcastError：" + bluetoothDevice.getName() + "---------------->" + bluetoothDevice.getAddress() + "--------->" + s);
//        LogUploadModel.getInstance().uploadConnectInfo("onBroadcastError:i=" + i + ",s=" + s);
//    }
//
//    @Override
//    public void onConnectionSate(BluetoothDevice bluetoothDevice, int status) {
//        Log.d("蓝牙:jar包回调", "onConnectionSate:状态改变");
//
//        switch (status) {
//            case SisenseBluetooth.STATE_CONNECTED:
//                Log.d("蓝牙:jar包回调", "onConnectionSate:连接成功");
//                UserInfoUtils.isDeviceConnected = true;
//                isAcDis = false;
//                addDevice(bluetoothDevice);
//                connected();
//
//                break;
//
//            case SisenseBluetooth.STATE_DISCONNECTED:
//                Log.d("蓝牙:jar包回调", "onConnectionSate:连接断开");
//                UserInfoUtils.isDeviceConnected = false;
//                if (!isAcDis && !sensorIsEdOrEp && UserInfoUtils.isLogin() && ObjectUtils.isNotEmpty(mDeviceName) && !isRightNowStopData) {
//                    Log.d("蓝牙:jar包回调", "不是主动断开连接,传感器正常,用户已登录,设备不为空,不立刻中止发送数据,发送handle消息,准备重连");
//                    mHandler.sendEmptyMessageDelayed(MSG_RECONNECT, mReconnectMills);
//                }
//                disConnected(true);
//
//                break;
//
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
//
//                }
//                break;
//
//            default:
//                break;
//        }
//
//    }
//
//    @Override
//    public void onConnectLog(String s) {
//        LogUtils.e("onConnectLog：" + s);
//
//        LogUploadModel.getInstance().uploadConnectInfo(s);
//
//    }
//
//    @Override
//    public void onScanMatchDevice(ScanResult scanResult) {
//
//    }
//
//
//    private void disConnectReConnect() {
//        Log.d("蓝牙", "发起重连");
//        connectBluetooth(mDeviceEntity);
//        if (ScreenUtils.isScreenLock()) {
//            mReconnectMills += 5 * 60000;
//        } else {
//            mReconnectMills = 1500L;
//        }
//
//        DebugFileUtils.setAppendFile("触发重连" + "-----屏幕是否关闭：" + ScreenUtils.isScreenLock() + "-----重连间隔：" + mReconnectMills);
//    }
//
//    /**
//     * 蓝牙连接成功后调用新增设备接口
//     */
//    private void addDevice(BluetoothDevice bluetoothDevice) {
//        Log.d("蓝牙:jar包回调", "蓝牙正常,添加设备");
//        if (ObjectUtils.isNotEmpty(bluetoothDevice)) {
//
//            mMacAddress = bluetoothDevice.getAddress();
//            mDeviceName = bluetoothDevice.getName();
//            Log.d("蓝牙:jar包回调", "蓝牙正常,添加设备" + mDeviceName);
//
//            String bluetoothNum = mDeviceEntity.getBlueToothNum();
//            String algorithmVersion = mIAlgorithm.getAlgorithmVersion();
//
//            mDeviceEntity.setAlgorithmVersion(algorithmVersion);
//            mDeviceEntity.setMacAddress(mMacAddress);
//            mDeviceEntity.setDeviceName(mDeviceName);
//
//            mDeviceEntity.setConnectMill(System.currentTimeMillis());
//
//            DeviceManager.getInstance().setDeviceEntity(mDeviceEntity);
//
//            if (mDeviceEntity != null) {
//                if (ObjectUtils.isEmpty(mDeviceId)) {
//                    //没有设备id直接新增设备
//                    Log.d("蓝牙:jar包回调", "数据库新增设备,并调用接口");
//                    DeviceManager.getInstance().insertDevice(mDeviceEntity);
//                    LogUtils.e("------------------------>新增设备" + mDeviceEntity.getDeviceName());
//                    mServiceModel.addDevice(bluetoothNum, mMacAddress, mDeviceName, algorithmVersion);
//                } else {
//                    //更新设备连接时间
//                    Log.d("蓝牙:jar包回调", "旧设备,数据库更新设备");
//                    DeviceRepository.getInstance().updateConnectMill(mDeviceName, System.currentTimeMillis());
//                    LogUtils.e("------------------------>更新设备时间" + mDeviceEntity.getDeviceName());
//                }
//            }
//        }
//    }
//
//    /**
//     * 创建或重建线程池
//     *
//     * @param log1 日志信息1
//     * @param log2 日志信息2
//     */
//    private void createOrRebuildThreadExecutor(String log1, String log2) {
//        // shutdownNow()后使用线程池状态来控制插入，还是存在漏洞插入，还是采用等待线程结束更为保险
//        if (mSingleThreadExecutor != null) {
//            mSingleThreadExecutor.shutdown();
//            while (!mSingleThreadExecutor.isTerminated()) {
//                LogUtils.e(log1);
//                LogUploadModel.getInstance().uploadConnectInfo(log1);
//                try {
//                    mSingleThreadExecutor.awaitTermination(5, TimeUnit.SECONDS);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            LogUtils.e(log2);
//            LogUploadModel.getInstance().uploadConnectInfo(log2);
//        }
//        mSingleThreadExecutor = Executors.newSingleThreadExecutor();
//    }
//
//
//    private class HandleCgmDataTask implements Runnable {
//
//        private final GjCGMRecord mGjCGMRecord;
//
//        public HandleCgmDataTask(GjCGMRecord gjCGMRecord) {
//            mGjCGMRecord = gjCGMRecord;
//        }
//
//        @Override
//        public void run() {
//            Log.d("蓝牙:线程池", "开跑");
//            if (mGjCGMRecord.getIndex() == mCurrentInsertIndex + 1) {
//                Log.d("蓝牙:线程池", "jar包传的血糖数据刚好大于已插入的血糖数据");
//                // 连续index，正常
//                dealCompositedData(mGjCGMRecord);
//            } else if (mGjCGMRecord.getIndex() > mCurrentInsertIndex + 1) {
//                // 发生index丢失，断开蓝牙
//                Log.d("蓝牙:线程池", "index大于已插入的,则若sibBlue还在连接,则停止连接");
//                LogUploadModel.getInstance().uploadConnectInfo("发生index丢失--mCurrentInsertIndex=" + mCurrentInsertIndex + "--------------getIndex=" + mGjCGMRecord.getIndex());
//                if (isSibBluetoothConnected()) {
//                    stopConnect();
//                }
//            } else {
//                Log.d("蓝牙:线程池", "index重复了,重复数据不处理");
//                // index重复不处理
//                LogUploadModel.getInstance().uploadConnectInfo("发生index重复，不处理--mCurrentInsertIndex=" + mCurrentInsertIndex + "--------------getIndex=" + mGjCGMRecord.getIndex());
//            }
//        }
//    }
//
//    @Override
//    public void updateDevice(DeviceEntity deviceEntity) {
//        Log.d("蓝牙:app自己方法", "updateDevice:更新deviceEntity");
//        mDeviceEntity = deviceEntity;
//        mDeviceId = mDeviceEntity.getDeviceId();
//        DeviceManager.getInstance().setDeviceEntity(mDeviceEntity);
//        LogUtils.e("------------------------>updateDevice:" + deviceEntity.getDeviceName());
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
//        Log.d("蓝牙:app自己方法", "startConnect:开始连接,更新deviceEntity");
//        isRightNowStopData = false;
//        mDeviceEntity = deviceEntity;
//        mDeviceId = mDeviceEntity.getDeviceId();
//
//        mReconnectMills = 1500L;
//
//        if (!BleUtils.getInstance().isBleEnabled()) {
//            Log.d("蓝牙:app自己方法", "startConnect:蓝牙不可用,去打开蓝牙");
//            BleUtils.getInstance().openBle();
//        } else {
//            Log.d("蓝牙:app自己方法", "startConnect:蓝牙可用,去连接蓝牙");
//            connectBluetooth(mDeviceEntity);
//        }
//    }
//
//    /**
//     * 断开蓝牙服务连接释放资源
//     */
//    @Override
//    public void stopConnect() {
//        Log.d("蓝牙:app自己方法", "stopConnect:调用siBlue不连接");
//        isAcDis = true;
//        mSiBluetooth.BleBluthtoothDisConnect(1);
//    }
//
//
//    @Override
//    public void onDestroy() {
//        Log.d("蓝牙", "服务马上被销毁onDestroy,停止连接,取消蓝牙状态的监听");
//        stopConnect();
//        unregisterReceiver(blueToothStateReceiver);
//        BroadcastManager.getInstance(this).destroy(Constant.BROADCAST_INIT_DEVICE);
//        super.onDestroy();
//    }
//
//    /**
//     * 判断蓝牙服务是否处于连接状态
//     *
//     * @return
//     */
//    @Override
//    public boolean isSibBluetoothConnected() {
//        Log.d("蓝牙:app自己方法", "isSibBluetoothConnected:判断是否已连接");
//        LogUtils.e("---------" + mNumUnReceived + "---------\n" + mSyncTimeMill + "---------\n" + (System.currentTimeMillis() - 3 * 60000L));
//
//        DebugFileUtils.setAppendFile("检查蓝牙是否断开：mNumUnReceived=" + mNumUnReceived + "-----mSyncTimeMill：" + mSyncTimeMill + "-----系统休眠时间：");
//
//        if (!UserInfoUtils.isDeviceConnected) {
//            Log.d("蓝牙:app自己方法", "isSibBluetoothConnected:蓝牙状态就是失败");
//            return false;
//        }
//
//        if (mNumUnReceived < 2 && mSyncTimeMill > 0 && mSyncTimeMill < System.currentTimeMillis() - 3 * 60000L) {
//            Log.d("蓝牙:app自己方法", "isSibBluetoothConnected:三分钟没有数据传输就算断开连接");
//            return false;
//        }
//
//        return mSiBluetooth.getIsConnected();
//    }
//
//
//    @Override
//    public void releaseAlgorithmContext(String deviceName) {
//        Log.d("蓝牙:app自己方法", "releaseAlgorithmContext:准备算法?");
//        if (mIAlgorithm == null) {
//
//            mIAlgorithm = AlgorithmFactory.createAlgorithm(DeviceManager.getInstance().getDeviceEntity().getAlgorithmVersion());
//        }
//
//        if (ObjectUtils.isNotEmpty(deviceName)) {
//            mIAlgorithm.releaseAlgorithmContext(UserInfoUtils.getUserId(), deviceName);
//        }
//        Log.d("蓝牙:app自己方法", "releaseAlgorithmContext:将DeviceName置为空");
//        mDeviceName = "";
//
//    }
//
//
//    @Override
//    public void addConnectListener(CgmConnectListener connectListener) {
//        Log.d("蓝牙:app自己方法", "addConnectListener:添加监听者");
//        mConnectListener.add(connectListener);
//    }
//
//
//    @Override
//    public void removeConnectListener(CgmConnectListener connectListener) {
//        Log.d("蓝牙:app自己方法", "removeConnectListener:移除监听者");
//        mConnectListener.remove(connectListener);
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
//     *
//     * @param deviceEntity
//     */
//    private void connectBluetooth(DeviceEntity deviceEntity) {
//        LogUploadModel.getInstance().uploadConnectInfo("触发连接" + System.currentTimeMillis() + "mCurrentDeviceName=" + mDeviceName + "------deviceName=" + deviceEntity.getDeviceName());
//        LogUtils.e("-----------------------触发连接" + System.currentTimeMillis() + "mCurrentDeviceName=" + mDeviceName + "------deviceName=" + deviceEntity.getDeviceName());
//
//        String linkCode = deviceEntity.getBlueToothNum();
//        String matchStr = linkCode.substring(0, 4);
//        String deviceName = deviceEntity.getDeviceName();
//
//        createOrRebuildThreadExecutor("connectBluetooth线程池中还有任务在执行。。。" + System.currentTimeMillis(),
//                "connectBluetooth线程池中任务执行完毕。。。" + System.currentTimeMillis());
//
//        //避免重复调用算法初始化代码
//        if (ObjectUtils.isEmpty(mDeviceName) || !mDeviceName.equals(deviceName)) {
//
//            Log.d("蓝牙:连接蓝牙", "新设备连接,deviceName不同:初始化算法");
//            LogUtils.e("-----------------------初始化算法");
//
//            mIAlgorithm = AlgorithmFactory.createAlgorithm(deviceEntity.getAlgorithmVersion());
//
//            if (mIAlgorithm == null) {
//                disConnected(false);
//                ToastUtils.showShort(R.string.please_upgrade_version);
//                return;
//            }
//
//            mDeviceName = deviceName;
//            Log.d("蓝牙:连接蓝牙", "新设备名:"+deviceName);
//
//            LogUploadModel.getInstance().uploadConnectInfo("初始化算法：--------->算法版本=" + mIAlgorithm.getAlgorithmVersion() + "------连接码=" + linkCode);
//            int result = mIAlgorithm.verifyLinkCode(linkCode);
//            if (result != 1) {
//                ToastUtils.showShort(getString(R.string.common_connection_code_error));
//                disConnected(false);
//                return;
//            }
//
//            mIAlgorithm.initAlgorithmContext(deviceEntity);
//
//        }
//        Log.d("蓝牙:jar包", "开始连接蓝牙");
//        mSiBluetooth.BleBluthtoothConnectStart(matchStr, 60 * 1000, null, 2);
//    }
//
//
//    private void dealCompositedData(GjCGMRecord cgmRecord) {
//        Log.d("蓝牙:传感器数据", "处理传感器传过来的原始数据:GjCGMRecord");
//        //数据索引
//        int index = cgmRecord.getIndex();
//        //温度
//        float temp = cgmRecord.getTemp();
//        //电量
//        long electric = cgmRecord.getElectric();
//        //血糖状态
//        int status = cgmRecord.getStatus();
//        //电流值
//        float value = cgmRecord.getValue();
//        //timeMillis = systemTimeMills + nextCollectionTime * 1000L - numOfUnreceived * 60 * 1000L;
//        //timeMillis =当前时间-60*（index+剩余笔数）+60s;
//        //  long timeMillis = cgmRecord.getTimeMillis();
//
//        int numUnreceived = cgmRecord.getNumOfUnreceived();
//
//        long timeMillis = System.currentTimeMillis() - 60 * 1000L * (index + numUnreceived) + 60 * 1000L;
//
//        Log.d("蓝牙:传感器数据", "传感器还剩多少数据:"+numUnreceived);
//        mNumUnReceived = numUnreceived;
//
//        String userId = UserInfoUtils.getUserId();
//
//        BloodGlucoseEntity entity = loadGlucoseData(userId, mDeviceName, mMacAddress, numUnreceived,
//                temp, value, status, index, timeMillis, electric);
//
//
//        if (index <= 20160 && !isRightNowStopData) {
//            Log.d("蓝牙:线程池", "index没有超过14天,也没有异常情况导致断开连接");
//            AppDatabase.getInstance().runInTransaction(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("蓝牙:线程池", "将转换后的app可用的血糖数据,存入数据库");
//                    AppDatabase.getInstance().getBloodEntityDao().insert(entity);
//                    mIAlgorithm.saveAlgorithmContext(userId, mDeviceName, index);
//                    mCurrentInsertIndex = entity.getIndex();
//                }
//            });
//            if (index != 20160 && numUnreceived <= 1 && index % 5 == 0) {
//                //未同步与不是最后一笔的情况下每5笔再上传一次血糖数据，因为设备失效是在20160的下一笔，而血糖数据的最后一笔是在20160，所以这里不能在20160上传血糖，否则失效了状态无法上传
//                if (mDeviceEntity != null && ObjectUtils.isNotEmpty(mDeviceName) && ObjectUtils.isNotEmpty(mDeviceId)) {
//                    mServiceModel.getCurrentIndex(mDeviceName, mDeviceId, mDeviceEntity.getAlgorithmVersion());
//                }
//            }
//        }
//
//        if (!sensorIsEdOrEp && mStatusListener != null && !isRightNowStopData) {
//            //传感器未损坏或过期的情况下将数据发送到首页
//            ThreadUtils.runOnUiThread(() -> {
//                if (mStatusListener != null) {
//                    Log.d("蓝牙:线程池", "在ui线程,将接收到的血糖数据更新到首页");
//                    mStatusListener.updateData(entity);
//
//                }
//            });
//        }
//
//        if (sensorIsEdOrEp){
//            isRightNowStopData = true;
//        }
//    }
//
//    private synchronized BloodGlucoseEntity loadGlucoseData(String userId, String deviceName, String macAddress, int numOfUnreceived,
//                                                            float temperatureValue, float currentValue, int stateValue, int index, long timeMills, long electric) {
//        BloodGlucoseEntity glucoseEntity = new BloodGlucoseEntity();
//
//
//        glucoseEntity.setIndex(index);
//        glucoseEntity.setBleName(deviceName);
//        glucoseEntity.setMacAddress(macAddress);
//        glucoseEntity.setTemperatureValue(temperatureValue);
//        glucoseEntity.setCurrentValue(currentValue);
//        glucoseEntity.setStateValue(stateValue);
//        glucoseEntity.setNumOfUnreceived(numOfUnreceived);
//        glucoseEntity.setElectric(electric);
//        glucoseEntity.setUserId(userId);
//
//        float defaultHigh = ConfigUtils.getInstance().getDefaultHigh(userId);
//        float defaultLow = ConfigUtils.getInstance().getDefaultLow(userId);
//
//        //血糖值
//        float glucoseValue = mIAlgorithm.loadData(index, currentValue, temperatureValue, 0, defaultLow, defaultHigh);
//        //血糖趋势
//        int glucoseTrend = mIAlgorithm.getGlucoseTrend();
//        //电流报警
//        int currentWarning = mIAlgorithm.getCurrentWarning();
//        //温度报警
//        int tempWarning = mIAlgorithm.getTempWarning();
//        //血糖报警
//        int cgmWarning = mIAlgorithm.getCgmWarning();
//
//        glucoseEntity.setGlucoseValue(glucoseValue <= 0 ? 0 : glucoseValue);
//        glucoseEntity.setTemperatureWarning(tempWarning);
//        glucoseEntity.setGlucoseWarning(cgmWarning);
//        glucoseEntity.setCurrentWarning(currentWarning);
//        glucoseEntity.setGlucoseTrend(glucoseTrend);
//        glucoseEntity.setValidIndex(index % 5 == 0 ? index / 5 : 0);
//
//        sensorIsEdOrEp = false;
//
//
//        setTemperature(glucoseEntity, index, tempWarning);
//
//        if (index < 60) {
//            //一个小时内(传感器初始化) 更新状态为3
//            if (index == 1) {
//                DeviceRepository.getInstance().updateDeviceStatusAndFirstMill(userId, mDeviceName, 3, 0, timeMills);
//                LogUtils.e("激活时间------------------------" + timeMills);
//            }
//            if (currentWarning > 0) {
//                //一个小时内的传感器异常(传感器损坏) 断开连接并更新状态为4
//                setSensorEdOrEp(userId, 4, Constant.SENSOR_EXCEPTION, index, numOfUnreceived);
//            }
//        } else if (index == 60) {
//            //使用中
//            DeviceRepository.getInstance().updateDeviceStatus(userId, mDeviceName, 1, 0);
//        } else if (index > 20160) {
//            //传感器失效(已停用)断开连接并更新状态为2
//            setSensorEdOrEp(userId, 2, Constant.SENSOR_INVALID, index, numOfUnreceived);
//        } else {
//            setCurrentAlarmStatus(glucoseEntity, userId, mDeviceName, currentWarning, index, numOfUnreceived);
//        }
//        if (index == 1) {
//            mProcessedTimeMill = timeMills;
//        } else {
//            mProcessedTimeMill += 60000L;
//        }
//        mSyncTimeMill = System.currentTimeMillis();
//        glucoseEntity.setProcessedTimeMill(mProcessedTimeMill);
//
//        return glucoseEntity;
//    }
//
//    private void setCurrentAlarmStatus(BloodGlucoseEntity glucoseEntity, String userId, String deviceName, int currentWarning, int index, int numOfUnreceived) {
//        //用用户id+设备名作为存储名
//        String mmkvName = userId + deviceName;
//        //电流异常index
//        int acIndex = UserInfoUtils.getAcIndex();
//        //电流异常次数
//        int acCount = UserInfoUtils.getAcCount();
//
//        if (currentWarning > 0) {
//            UserInfoUtils.putAcIndex(index);
//            UserInfoUtils.putAcCount(acCount + 1);
//            LogUtils.e("acIndex1,acCount1", acIndex, acCount);
//            // TODO: 2021/6/1 30分钟的传感器异常,更新alarmStatus为2
//            glucoseEntity.setAlarmStatus(2);
//            if (acCount >= 180) {
//                //3小时候的传感器异常
//                // TODO: 2021/6/1 deviceStatus为4
//                //更新本地设备deviceStatus为4 断开传感器
//                setSensorEdOrEp(userId, 4, Constant.SENSOR_EXCEPTION, index, numOfUnreceived);
//            }
//        } else {
//            if (acIndex != 0 && index <= acIndex + 30) {
//                //传感器异常且30分钟之内
//                UserInfoUtils.putAcCount(acCount + 1);
//                // TODO: 2021/6/1 更新alarmStatus为2
//                glucoseEntity.setAlarmStatus(2);
//                if (acCount >= 180) {
//                    //3小时候的传感器异常
//                    // TODO: 2021/6/1
//                    //更新本地设备deviceStatus为4
//                    setSensorEdOrEp(userId, 4, Constant.SENSOR_EXCEPTION, index, numOfUnreceived);
//                }
//            } else {
//                //恢复正常
//                // TODO: 2021/6/1 判定温度异常
//                // TODO: 2021/6/1 判定血糖偏高/偏低 (只能判定有血糖值)
//                // TODO: 2021/6/1 更新alarmStatus
//                UserInfoUtils.putAcIndex(0);
//                UserInfoUtils.putAcCount(0);
//            }
//        }
//    }
//
//    private void setTemperature(BloodGlucoseEntity glucoseEntity, int index, int errTemperature) {
//        if (errTemperature == 1) {
//            //温度过低
//            glucoseEntity.setAlarmStatus(4);
//        } else if (errTemperature == 2) {
//            //温度过高
//            glucoseEntity.setAlarmStatus(3);
//        } else {
//            //温度正常
//            if (index % 5 == 0) {
//                if (glucoseEntity.getGlucoseValue() < 2.2f) {
//                    //血糖过低
//                    glucoseEntity.setAlarmStatus(6);
//                } else if (glucoseEntity.getGlucoseValue() > 25f) {
//                    //血糖过高
//                    glucoseEntity.setAlarmStatus(5);
//                } else {
//                    //血糖正常
//                    glucoseEntity.setAlarmStatus(1);
//                }
//            } else {
//                glucoseEntity.setAlarmStatus(1);
//            }
//        }
//    }
//
//    private void setSensorEdOrEp(String userId, int deviceStatus, String extra, int index, int numOfUnreceived) {
//        sensorIsEdOrEp = true;
//
//        if (isRightNowStopData) return;
//
//        DeviceRepository.getInstance().updateDeviceStatus(userId, mDeviceName, deviceStatus, 0);
//        BroadcastManager.getInstance(this).sendBroadcast(Constant.SENSOR_EXCEPTION_AND_INVALID_BROAD_CAST, extra);
//
//        stopConnect();
//        if (mDeviceEntity != null && ObjectUtils.isNotEmpty(mDeviceName) && ObjectUtils.isNotEmpty(mDeviceId)) {
//            mServiceModel.getCurrentIndex(mDeviceName, mDeviceId, mDeviceEntity.getAlgorithmVersion());
//        }
//    }
//
//
//}
