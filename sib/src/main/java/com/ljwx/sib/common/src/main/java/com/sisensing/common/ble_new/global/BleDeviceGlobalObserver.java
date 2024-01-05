package com.sisensing.common.ble_new.global;

import androidx.lifecycle.Observer;

import kotlin.Pair;

public abstract class BleDeviceGlobalObserver implements Observer<Pair<Integer, Boolean>> {

    @Override
    public void onChanged(Pair<Integer, Boolean> data) {
        if (data != null) {
            onChangedCustom(data.getFirst(), data.getSecond());
        }
    }

    public abstract void onChangedCustom(Integer status, Boolean showToast);

}
