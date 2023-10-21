package com.ljwx.basemodule.java;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ljwx.baseactivity.BaseMVVMActivity;
import com.ljwx.baseapp.vm.empty.EmptyViewModel;
import com.ljwx.basemodule.databinding.ActivityMainBinding;

public class TesJava extends BaseMVVMActivity<ActivityMainBinding, EmptyViewModel> {

    public TesJava(int layoutResID) {
        super(layoutResID);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        observe(new MutableLiveData<String>(), new Observer<String>() {
            @Override
            public void onChanged(String o) {

            }
        });
    }
}
