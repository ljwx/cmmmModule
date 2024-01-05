package com.sisensing.common.router;

/**
 * 用于组件开发中，ARouter单Activity跳转的统一路径注册
 * 在这里注册添加路由路径，需要清楚的写好注释，标明功能界面
 */

public class RouterActivityPath {

    public static final String PERMISSION_ALARM = "/alarm/permission";

    /**
     * 启动业务组件
     */
    public static class Launcher {

        private static final String LAUNCHER = "/launch";
        //启动页
        public static final String PAGE_LAUCHER = LAUNCHER + "/launch";
        //引导页
        public static final String PAGE_GUIDE = LAUNCHER + "/guide";
        //主界面
        public static final String PAGE_MAIN = LAUNCHER + "/main";
        //连接码输入界面
        public static final String PAGE_LINK_CODE_INPUT = LAUNCHER + "/link/code/input";
        //机型兼容界面
        public static final String PAGE_DEVICE_COMPATIBLE = LAUNCHER + "/device/compatible";
        //首次启动的引导页
        public static final String PAGE_GUIDE_PAGES = LAUNCHER + "/guidepages";
    }

    /**
     * 扫描二维码组件
     */
    public static class Scan {

        private static final String SCAN = "/scan";

        public static final String PAGER_SCAN = SCAN + "/main";

        public static final String PAGER_SCAN_PROMPT = SCAN + "/main/prompt";

        public static final String PAGER_SCAN_COMPAT = SCAN + "/guide";

    }

    /**
     * webview组件
     */
    public static class Web {

        private static final String WEB = "/web";

        public static final String PAGER_WEB = WEB + "/main";

    }

    /**
     * 登陆组件
     */
    public static class Login {
        public static final String LOGIN = "/login";
        //登陆页面路径
        public static final String PAGE_LOGIN = LOGIN + "/main";
        //注册界面路径
        public static final String PAGE_REGISTER = LOGIN + "/register/main";
        //修改密码界面路径
        public static final String PAGE_MODIFY_PASSWORD = LOGIN + "/modify/password/main";
        //绑定手机号界面路径
        public static final String PAGE_BIND_PHONE = LOGIN + "/bind/phone/main";
        //设置密码界面
        public static final String PAGE_SET_PWD = LOGIN + "/set/pwd";
    }


    /**
     * 血糖监测
     */
    public static class BsMonitoring {
        public static final String BSM = "/bsm";
        //饮食记录activity路径
        public static final String PAGE_DIET_RECORD = BSM + "/diet/record";
        //运动记录activity路径
        public static final String PAGE_MOTION_RECORD = BSM + "/motion/record";
        //胰岛素
        public static final String PAGE_INSULIN_RECORD = BSM + "/insulin/record";
        //用药记录
        public static final String PAGE_MEDICATION_RECORD = BSM + "/medication/record";
        //指血记录
        public static final String PAGE_FINGER_BLOOD_RECORD = BSM + "/finger/blood/record";
        //睡眠记录
        public static final String PAGE_SLEEP_RECORD = BSM + "/sleep/record";
        //身体状况记录
        public static final String PAGE_HEALTH_STATUS_RECORD = BSM + "/health/status/record";
        //更换设备
        public static final String PAGE_CHANGE_DEVICE = BSM + "/change/device";
        //横屏显示图表
        public static final String PAGE_CHART_EXTEND = BSM + "/chart/extend";
        //用药和胰岛素
        public static final String PAGE_MD_AND_INSULIN = BSM + "/md/and/insulin";
        //药物选择
        public static final String PAGE_DRUG_SELECTION = BSM + "/drug/selection";
        //药物搜索
        public static final String PAGE_DRUG_SEARCH = BSM + "/drug/search";
    }

    /**
     * 日趋势图(血糖分析)
     */
    public static class DailyTrendChart {
        public static final String DTC = "/dtc";
        //多日血糖全屏显示
        public static final String PAGE_MULTI_DAY_STACK_FULLSCREEN = "/multi/day/stack/fullscreen";

        public static final String REPORT_DATA_ERROR = "/report/error";
    }

    /**
     * 个人中心
     */
    public static class PersonalCenter {
        public static final String PERSONAL_CENTER = "/personal/center";
        //我的报告
        public static final String PAGE_MY_REPORT = PERSONAL_CENTER + "/my/report";
        //我的医生
        public static final String PAGE_MY_DOCTOR = PERSONAL_CENTER + "/my/doctor";
        //商城
        public static final String PAGE_SHOPPING_MALL = PERSONAL_CENTER + "/shopping/mall";
        //健康档案
        public static final String PAGE_HEALTH_RECORDS = PERSONAL_CENTER + "/health/record";
        //血糖参数设置
        public static final String PAGE_BS_PARAM_SETTING = PERSONAL_CENTER + "/bs/param/setting";
        //生活习惯
        public static final String PAGE_LIVING_HABITS = PERSONAL_CENTER + "/living/habits";
        //我的传感器
        public static final String PAGE_MY_SENSORS = PERSONAL_CENTER + "/my/sensors";
        //亲友列表
        public static final String PAGE_MY_FOLLOW = PERSONAL_CENTER + "/my/follow";
        //报警事件
        public static final String PAGE_ALARM_EVENTS = PERSONAL_CENTER + "/alarm/events";
        //生活事件
        public static final String PAGE_LIFE_EVENTS = PERSONAL_CENTER + "/life/events";
        //在线客服
        public static final String PAGE_ONLINE_SERVICE = PERSONAL_CENTER + "/online/service";
        //各种事件记录
        public static final String PAGE_EVENT_RECORD = PERSONAL_CENTER + "/events/record";
        //闹钟提醒
        public static final String PAGE_ALARM_CLOCK_REMINDER = PERSONAL_CENTER + "/alarm/clock/reminder";
        //我的设备
        public static final String PAGE_MY_DEVICE = PERSONAL_CENTER + "/my/device";
        //更换头像
        public static final String PAGE_CHANGE_AVATAR = PERSONAL_CENTER + "/change/avatar";
        //更换头像完成
        public static final String PAGE_CHANGE_AVATAR_FINISH = PERSONAL_CENTER + "/change/avatar/finish";
        //查看血糖数据
        public static final String PAGE_VIEW_BLOOD_GLUCOSE_DATA = PERSONAL_CENTER + "/view/blood/glucose/data";
        //新增闹钟
        public static final String PAGE_ADD_ALARM_CLOCK = PERSONAL_CENTER + "/add/alarm/clock";
        //查看报告
        public static final String PAGE_VIEW_REPORT = PERSONAL_CENTER + "/view/report";
        //闹钟显示
        public static final String PAGE_CLOCK_ALARM = PERSONAL_CENTER + "/clock/alarm";
        //生活事件
        public static final String PAGE_LIFE_EVENTS_NEW = PERSONAL_CENTER + "/life/events/new";
        //个人信息填充
        public static final String PAGE_FILL_PERSONAL_INFO = PERSONAL_CENTER + "/fill/personal/info";
        //传感器连接失败
        public static final String PAGE_SENSOR_CONNECT_FAIL = PERSONAL_CENTER + "/sensor/connect/fail";
        //声音设置
        public static final String PAGE_ALARM_VOICE_SETTING = PERSONAL_CENTER + "/alarm/voice/setting";
        //选择国家
        public static final String PAGE_SELECT_COUNTRY = PERSONAL_CENTER + "/select/country";
        //选择语言
        public static final String PAGE_SELECT_LANGUAGE = PERSONAL_CENTER + "/select/language";
        //血糖分享
        public static final String PAGE_REMOTE_MONITORING = PERSONAL_CENTER + "/remote/monitoring";
        //血糖分享邀请
        public static final String PAGE_INVITE_FOLLOWER = PERSONAL_CENTER + "/invite/follower";
        //血糖分享报警设置
        public static final String PAGE_BS_PARAM_FOLLOWER_SETTING = PERSONAL_CENTER + "/bs/param/follower/setting";
        //血糖分享详情
        public static final String PAGE_BS_PARAM_SHARER_BS_MONITOR = PERSONAL_CENTER + "/bs/param/sharer/bsMonitor";

        public static final String PAGE_SETTING_ACCOUNT_REMOVE = PERSONAL_CENTER + "/setting/accountRemove";

        public static final String PAGE_EDIT_PERSONAL_INFO = PERSONAL_CENTER + "/edit/info";
        //远程监测主页
        public static final String REMOTE_MONITORING_MAIN = PERSONAL_CENTER + "/remote_monitoring/main";
        //远程监测机构列表页
        public static final String REMOTE_MONITORING_INSTITUTION = PERSONAL_CENTER + "/remote/institution";
        //远程机构邀请记录
        public static final String REMOTE_INSTITUTION_RECORDS = PERSONAL_CENTER + "/remote/institution_record";

        public static final String SETTINGS_LEGAL_DOCUMENTS = PERSONAL_CENTER + "/legal_documents";

        //血糖报警单项详情页
        public static final String GLUCOSE_ALARM_SETTINGS_ITEM = PERSONAL_CENTER + "/settings_item";
        //血糖报警,值修改页
        public static final String GLUCOSE_ALARM_SETTINGS_VALUE = PERSONAL_CENTER + "/settings_value";

        public static final String HELP_MAIN = PERSONAL_CENTER + "/help_main";
        public static final String HELP_APPLY_SENSOR = PERSONAL_CENTER + "/help_apply_sensor";
    }
}
