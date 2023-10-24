package com.ljwx.baseapp.vm.empty;

import android.app.Application;

import androidx.annotation.NonNull;

import com.ljwx.baseapp.vm.BaseAndroidViewModel;

public class Test extends BaseAndroidViewModel {

    public Test(@NonNull Application application) {
        super(application);
    }

    @Override
    public Object createRepository() {
        getApplication();
        return null;
    }
}
