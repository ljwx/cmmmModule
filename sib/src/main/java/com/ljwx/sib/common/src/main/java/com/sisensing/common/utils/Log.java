package com.sisensing.common.utils;

public class Log {

    public static void v(String tag, String info) {
        Throwable throwable = new Throwable();
        String className = throwable.getStackTrace()[1].getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        StackTraceElement[] elements = throwable.getStackTrace();
        String methodName = elements[1].getMethodName();
        String pre = "类:" + className + "-方法:" + methodName + ": ";
        android.util.Log.v(tag, pre + info);
    }

    public static void d(String tag, String info) {
        Throwable throwable = new Throwable();
        String className = throwable.getStackTrace()[1].getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        StackTraceElement[] elements = throwable.getStackTrace();
        String methodName = elements[1].getMethodName();
        String pre = "类:" + className + "-方法:" + methodName + ": ";
        android.util.Log.d(tag, pre + info);
    }

    public static void e(String tag, String content) {
        android.util.Log.e(tag, content);
    }

}
