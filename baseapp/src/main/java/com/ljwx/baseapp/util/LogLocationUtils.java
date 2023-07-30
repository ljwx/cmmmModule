package com.ljwx.baseapp.util;

import android.util.Log;

public class LogLocationUtils {

    public static void d(String tag, String info) {
        Throwable throwable = new Throwable();
        String className = throwable.getStackTrace()[1].getClassName();
        className = className.substring(className.lastIndexOf(".")+1);
        StackTraceElement[] elements = throwable.getStackTrace();
        String methodName = elements[1].getMethodName();
        String pre = "类:" + className + "-方法:" + methodName + ": ";
        Log.d(tag, pre + info);
    }

}
