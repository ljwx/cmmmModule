package com.sisensing.common.router;

/**
 * 用于组件开发中，ARouter多Fragment跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 */

public class RouterFragmentPath {

    /**
     * 血糖监测
     */
    public static class BsMonitoring{
        public static final String BSM = "/bsm";
        //主页血糖监测fragment路径
        public static final String PAGE_BSM = BSM+"/Bsm";
    }

    /**
     * 每日血糖
     */
    public static class DailyBloodSugar{
        public static final String DBS = "/dbs";
        //主页每日血糖fragment路径
        public static final String PAGE_DBS = DBS+"/Dbs";
    }

    /**
     * 日趋势图
     */
    public static class DailyTrendChart{
        public static final String DTC = "/dtc";
        //主页日趋势图fragment路径
        public static final String PAGE_DTC = DTC+"/Dtc";
    }

    /**
     * 个人中心
     */
    public static class PersonalCenter{
        public static final String PSC = "/psc";
        //主页个人中心fragment路径
        public static final String PAGE_PSC = PSC+"/Psc";
    }

    public static class Login{
        public static final String LOGIN = "/login";

        public static final String PAGE_REGISTER_POLICY = LOGIN + "/register/policy";

        public static final String PAGE_REGISTER = LOGIN + "/register";

    }
}
