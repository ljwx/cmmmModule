package com.sisensing.common.ble.v4.data;

import com.sisensing.common.ble.BleLog;
import com.sisensing.common.utils.Log;

public class GlucoseDataInfo {

    //当前插入数据库的血糖index标识
    public static int mCurrentInsertIndex;

    //最新血糖时间戳
    public static long mProcessedTimeMill;

    //血糖同步时间戳，以此通过血糖同步时间差判断蓝牙是否短卡（针对蓝牙被kill拿不到回调的解决办法）
    public static long mSyncTimeMill;

    public static int mNumUnReceived = 0;

    public static boolean needUpdateAgain(int index) {
        //未同步与不是最后一笔的情况下每5笔再上传一次血糖数据，因为设备失效是在20160的下一笔，
        // 而血糖数据的最后一笔是在20160，所以这里不能在20160上传血糖，否则失效了状态无法上传
        boolean update = index != 20160 && mNumUnReceived <= 1 && index % 5 == 0;
        BleLog.dGlucose("当前血糖是否满足上传:" + update+ "--index!=20160--unreceived<=1--index%5");
        return update;
    }

    public static boolean isFirstData(int index) {
        return index == 1;
    }

    public static boolean isExactlyOneHour(int index) {
        return index == 60;
    }

    public static boolean isExpired(int index) {
        return index > 20160;
    }

    public static boolean dataConnectionException() {
        boolean diff = mSyncTimeMill < System.currentTimeMillis() - 3 * 60000L;
        BleLog.dApp("数据连接是否异常,未读:"+mNumUnReceived+",同步时间:"+mSyncTimeMill+",时差范围是否三分钟内:"+diff);
        if (mNumUnReceived < 2 && mSyncTimeMill > 0 && mSyncTimeMill < System.currentTimeMillis() - 3 * 60000L) {
            BleLog.dApp("数据不正常,可判定为未连接");
            return true;
        }
        return false;
    }

}
