package com.sisensing.common.livedata;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


/**
 * @author y.xie
 * @date 2021/4/27 14:54
 * @desc
 */
public class CommonLiveData extends MutableLiveData {
    private CommonLiveData() {
    }

    private static class Holder {
        public static final CommonLiveData INSTANCE = new CommonLiveData();
    }

    public static CommonLiveData getInstance() {
        return Holder.INSTANCE;
    }

    //MutableLiveData在LiveData基础上暴露两个设值接口
    public class MutableLiveData<T> extends LiveData<T> {
        @Override
        public void postValue(T value) {
            super.postValue(value);
        }

        @Override
        public void setValue(T value) {
            super.setValue(value);
        }
    }
}
