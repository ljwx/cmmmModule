package com.sisensing.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class  SPUtil {

    private final String TAG_LANGUAGE = "language_select";
    private final String TAG_SYSTEM_LANGUAGE = "system_language";
    private static volatile SPUtil instance;

    private final SharedPreferences mSharedPreferences;

    private Locale systemCurrentLocal = Locale.ENGLISH;


    public SPUtil(Context context) {
        String SP_NAME = "language_setting";
        mSharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }


    public void saveLanguage(int select) {
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putInt(TAG_LANGUAGE, select);
        edit.apply();
    }

    /**
     * 获取语言种类
     *
     * @param defaultType 默认类型
     * @return 有值正常返回,没有则返回默认值
     */
    public int getLanguageType(int defaultType) {
        return mSharedPreferences.getInt(TAG_LANGUAGE, defaultType);
    }


    public Locale getSystemCurrentLocal() {
        return systemCurrentLocal;
    }

    public void setSystemCurrentLocal(Locale local) {
        systemCurrentLocal = local;
    }

    public static SPUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (SPUtil.class) {
                if (instance == null) {
                    instance = new SPUtil(context);
                }
            }
        }
        return instance;
    }
}
