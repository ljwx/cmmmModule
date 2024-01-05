package com.sisensing.common.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions3.RxPermissions;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.utils
 * Author: f.deng
 * CreateDate: 2021/7/3 14:25
 * Description:
 */
public class RxPermissionUtils {


    public static RxPermissions getRxPermission(FragmentActivity activity) {

        return new RxPermissions(activity);

    }

    public static RxPermissions getRxPermission(Fragment fragment) {

        return new RxPermissions(fragment);

    }
}
