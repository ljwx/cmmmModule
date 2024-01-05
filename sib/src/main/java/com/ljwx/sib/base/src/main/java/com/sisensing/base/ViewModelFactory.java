package com.ljwx.sib.base.src.main.java.com.sisensing.base;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory {

    private static ViewModelProvider mFragmentProvider;
    private static ViewModelProvider mActivityProvider;
    private static ViewModelProvider mApplicationProvider;


    public static <T extends ViewModel> T getFragmentScopeViewModel(@NonNull Class<T> modelClass, Fragment fragment) {
        if (mFragmentProvider == null) {
            mFragmentProvider = new ViewModelProvider(fragment);
        }
        return mFragmentProvider.get(modelClass);
    }

    public static <T extends ViewModel> T getActivityScopeViewModel(@NonNull Class<T> modelClass, AppCompatActivity activity) {
        if (mActivityProvider == null) {
            mActivityProvider = new ViewModelProvider(activity);
        }
        return mActivityProvider.get(modelClass);
    }

    public static <T extends ViewModel> T getApplicationScopeViewModel(@NonNull Class<T> modelClass) {
        if (mApplicationProvider == null) {
            mApplicationProvider = new ViewModelProvider((BaseApplication) BaseApplication.getInstance());
        }
        return mApplicationProvider.get(modelClass);
    }


}
