package com.sisensing.common.utils;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * ProjectName: CGM_C
 * Package: com.sisensing.common.utils
 * Author: f.deng
 * CreateDate: 2021/7/21 15:12
 * Description:
 */
public class SibActivityManager {

    private static SibActivityManager sInstance = new SibActivityManager();

    private WeakReference<Activity> sCurrentActivityWeakRef;

    private SibActivityManager() {
    }

    public static SibActivityManager getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity a = null;
        if (sCurrentActivityWeakRef != null) {
            a = sCurrentActivityWeakRef.get();
        }
        return a;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<>(activity);
    }
}
