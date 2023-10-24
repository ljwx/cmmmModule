package com.ljwx.sib.test;

import androidx.databinding.ViewDataBinding;

import com.ljwx.baseapp.vm.BaseAndroidViewModel;
import com.ljwx.baseapp.vm.model.BaseDataRepository;
import com.ljwx.sib.activity.BaseSibMVVMActivity;

public abstract class BaseMvvmV2Activity <V extends ViewDataBinding, VM extends BaseAndroidViewModel<BaseDataRepository<Object>>> extends BaseSibMVVMActivity<V, VM> {
}
