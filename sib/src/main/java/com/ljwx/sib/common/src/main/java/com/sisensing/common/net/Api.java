package com.sisensing.common.net;

import com.blankj.utilcode.util.LanguageUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.ljwx.baseswitchenv.AppEnvItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author y.xie
 * @date 2021/3/4 15:12
 * @desc
 */
public class Api {
    //正式环境基础域名
//    public static final String PRODUCE_BASE_URL = "https://api.sisensing.com/";
    //预生产
    public static final String PRE_PRODUCE_BASE_URL= "https://cgm-ce-pre.sisensing.com/";

    //开发自己测试
    public static final String DEV_BASE_URL = "http://192.168.100.119:32081";
    //海外域名
    public static final String EU_BASE_URL = "https://cgm-ce.sisensing.com";
    //给测试
    public static final String TEST_BASE_URL = "http://192.168.100.126:32081";
    public static final String TEST_BASE_URL_V2 = "https://api-test-v2.sisensing.com";
    //服务政策
    public static final String AGREEMENT_URL = "https://protocol.sisensing.com/ce_sijoy_service.html";
    //隐私政策
    public static final String PRIVACY_POLICY_URL = "https://protocol.sisensing.com/ce_sijoy_privacy.html";

    public static List<AppEnvItem> getEnvs() {
        List<AppEnvItem> list = new ArrayList<>();
        list.add(new AppEnvItem("开发环境", DEV_BASE_URL));
        list.add(new AppEnvItem("测试环境", TEST_BASE_URL));
        list.add(new AppEnvItem("测试V2环境", TEST_BASE_URL_V2));
        list.add(new AppEnvItem("预生产环境", PRE_PRODUCE_BASE_URL));
        list.add(new AppEnvItem("生产环境", EU_BASE_URL));
        return list;
    }

    public static String getCurrentEnvName() {
        String name = "";
        for (AppEnvItem env : getEnvs()) {
            if (env.getHost().equals(getCacheHost(""))) {
                name = env.getTitle();
            }
        }
        return name;
    }

    public static String getCacheHost(String defaultHost) {
        return SPUtils.getInstance().getString("host_cache", defaultHost);
    }

    public static void setCacheHost(String host) {
        if (ObjectUtils.isNotEmpty(host)) {
            SPUtils.getInstance().put("host_cache", host);
        }
    }

    public static String getAgreementUrl(){
        Locale local ;
        if (LanguageUtils.isAppliedLanguage()) {
            //设置了语言
            local =     LanguageUtils.getAppliedLanguage();
        } else {
            local=  LanguageUtils.getSystemLanguage();
        }
        String lang = local.getLanguage();

        if (!lang.isEmpty()) {
            if (lang.endsWith("en")) {
                //中文
                return "https://protocol.sisensing.com/ce_sijoy/service_EN.html";
            }
            if (lang.endsWith("fr")) {

                return "https://protocol.sisensing.com/ce_sijoy/service_FR.html";
            }
            if (lang.endsWith("de")) {

                return "https://protocol.sisensing.com/ce_sijoy/service_DE.html";
            }
            if (lang.endsWith("it")) {

                return "https://protocol.sisensing.com/ce_sijoy/service_IT.html";
            }

            if (lang.endsWith("ru")) {
                //中文
                return "https://protocol.sisensing.com/ce_sijoy/service_RU.html";
            }
        }
        return "https://protocol.sisensing.com/ce_sijoy/service_EN.html";
    }

    public static String getPrivacyPolicyUrl(){
        Locale local ;
        if (LanguageUtils.isAppliedLanguage()) {
            //设置了语言
            local =     LanguageUtils.getAppliedLanguage();
        } else {
            local=  LanguageUtils.getSystemLanguage();
        }
        String lang = local.getLanguage();

        if (!lang.isEmpty()) {
            if (lang.endsWith("en")) {
                //中文
                return "https://protocol.sisensing.com/ce_sijoy/privacy_EN.html";
            }
            if (lang.endsWith("fr")) {

                return "https://protocol.sisensing.com/ce_sijoy/privacy_FR.html";
            }
            if (lang.endsWith("de")) {

                return "https://protocol.sisensing.com/ce_sijoy/privacy_DE.html";
            }
            if (lang.endsWith("it")) {

                return "https://protocol.sisensing.com/ce_sijoy/privacy_IT.html";
            }

            if (lang.endsWith("ru")) {
                //中文
                return "https://protocol.sisensing.com/ce_sijoy/privacy_RU.html";
            }
        }
        return "https://protocol.sisensing.com/ce_sijoy/privacy_EN.html";
    }


}
