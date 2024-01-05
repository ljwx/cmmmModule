package com.sisensing.common.ble.v4.extract;

abstract public class LocalBleService3ReconnectionV4 extends LocalBleService2ModelV4 {


    protected long mReconnectMillsUnlock = 1500;
    protected long mReconnectMillsLock = 5 * 60000;
    protected long mReconnectMills = mReconnectMillsUnlock;
    //
    private final int MSG_RECONNECT_OLD = 100000;
    protected static final int MSG_RECONNECT = 1000;

    protected int retryUploadLogTimes = 0;

    //当前连接状态,给个默认值为断开连接
    protected int CURRENT_CONNECT_STATUS = no.sisense.android.api.Constant.STATE_DISCONNECTED;


}
