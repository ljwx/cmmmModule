package com.sisensing.common.ble;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class LocalBleServiceProxy extends LocalBleServiceV3{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    public class LocalBinder extends Binder {
        public LocalBleServiceProxy getService() {
            return LocalBleServiceProxy.this;
        }
    }

}
