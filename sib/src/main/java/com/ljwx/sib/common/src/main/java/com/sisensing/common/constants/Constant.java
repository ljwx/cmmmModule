package com.sisensing.common.constants;

import java.util.Locale;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.constants
 * @Author: f.deng
 * @CreateDate: 2021/3/1 14:32
 * @Description:
 */
public final class Constant {
    //正式与其他环境切换(true:正式环境 false:其他环境)
    public static final boolean isPro = false;
    //网络响应码-登录过期
    public static final int NETWORK_RESPONSE_LOGIN_EXPIRED = 401;
    public static final String DEVICE_CODE = "deviceCode";
    //修改用户昵称
    public static final String MODIFY_USER_NAME = "modify_user_name";
    //sp保存其他信息的文件名
    public static final String SP_CGM_C_OTHER = "sp_cgm_c_other";

    public static final String NORMAL_TIME_FORMAT_ = "yyyy/MM/dd HH:mm";
    public static final String NORMAL_TIME_FORMAT1_ = "dd/MM/yyyy HH:mm";
    public static final String NORMAL_DATE = "yyyy-MM-dd";
    public static final String NORMAL_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String NORMAL_TIME_HOUR_AND_MINUTE = "HH:mm";
    public static final String NORMAL_TIME_YEARS_MONTH_DAY = "yyyy/MM/dd";
    public static final String NORMAL_TIME_MONTH_DAY_HOUR_MINUTE = "MM-dd HH:mm";
    public static final String NORMAL_TIME_DAY_MONTH_HOUR_MINUTE = "MM/dd HH:mm";
    public static final String NORMAL_DAY_MONTH = "dd/MM";
    //微信app_id
    public static final String WECHAT_APP_ID = "wx098d493727337ee6";
    //硅基动感小程序原始id(gh_86e20e631f30预生产环境)
    public static final String WECHAT_APPLETS_PRE_ID = "gh_86e20e631f30";
    //硅基动感小程序原始id（gh_47c2757e0901生产环境）
    public static final String WECHAT_APPLETS_PRO_ID = "gh_47c2757e0901";
    //微信secret
    public static final String WECHAT_SECRET = "5e79ffb42f46968421ed9cb721fe1d41";
    //微信登录广播
    public static final String WECHAT_LOGIN_BROADCAST = "wechat_login_broadcast";
    //用户换取 access_token 的 code key
    public static final String GET_WECHAT_TOKEN_CODE = "get_wechat_token_code";
    //更换设备的广播
    public static final String CHANGE_DEVICE_BROADCAST = "change_device_broadcast";
    //绑定新设备
    public static final String BIND_NEW_DEVICE = "bind_new_device";
    //身高体重正则
    public static final String HEIGHT_AND_WEIGHT_PATTERN = "^([1-9]\\d{0,2})(\\.\\d{1,2})?$";

    public static final boolean ANTI_ALIAS = true;

    public static final int DEFAULT_SIZE = 150;
    public static final int DEFAULT_START_ANGLE = 270;
    public static final int DEFAULT_SWEEP_ANGLE = 360;

    public static final int DEFAULT_ANIM_TIME = 1000;

    public static final int DEFAULT_MAX_VALUE = 100;
    public static final int DEFAULT_VALUE = 0;

    public static final int DEFAULT_HINT_SIZE = 15;
    public static final int DEFAULT_UNIT_SIZE = 30;
    public static final int DEFAULT_VALUE_SIZE = 15;

    public static final int DEFAULT_ARC_WIDTH = 15;

    public static final int DEFAULT_WAVE_HEIGHT = 40;
    //个人信息变更
    public static final String BROADCAST_PERSONAL_INFO_CHANGE = "broadcast_personal_info_change";
    //是否使用帮助界面跳转过来
    public static final String IS_USE_HELPER_JUMP = "is_use_helper_jump";
    //血糖信息是否变化广播的action
    public static final String BS_INFO_CHANGE = "bs_unit_is_change";
    //血檀分析顶部时间段广播的action
    public static final String BS_ANALYSIS_BROAD_CAST = "bs_analysis_broad_cast";
    //传感器异常与失效的广播action
    public static final String SENSOR_EXCEPTION_AND_INVALID_BROAD_CAST = "sensor_exception_and_invalid_broad_cast";
    //传感器异常的广播的key
    public static final String SENSOR_EXCEPTION = "sensor_exception";
    //传感器失效的广播的key
    public static final String SENSOR_INVALID = "sensor_invalid";
    //中国区的手机号校验
    public static final String CHINA_MOBILE = "^0?1[3-9]{1}\\d{9}$";
    //传感器初始化是否弹出提醒弹框的sp key
    public static final String SENSOR_INIT_REMINDER_POP = "sensor_init_reminder_pop";
    //设备初始化后的广播
    public static final String BROADCAST_INIT_DEVICE = "broadcast_init_device";
    //连接码或扫码界面连接成功的广播
    public static final String LINK_OR_SCAN_ACTIVITY_CONNECT_SUCCESS_BROADCAST = "link_or_scan_activity_connect_success_broadcast";
    //蓝牙设备适配的机型
    public static String[] deviceArray = {"ANA-AN00", "PEHM00", "LRA-AL00", "TAS-AL00"};
    //是否首次打开app
    public static final String IS_FIRST_OPEN_APP = "is_first_open_app";
    //打卡广播
    public static final String CLOCK_IN_BROAD_CAST = "clock_in_broad_cast";
    //七鱼appKey
    public static final String QI_YU_CUSTOM_SERVICE_APP_KEY = "e79a7006ad6e6c49a36cbd905ee6bf96";
    //用户历史用药选择key
    public static final String DRUG_HIS_SELECTION_MED = "drug_his_selection_med";
    //用户历史胰岛素选择key
    public static final String DRUG_HIS_SELECTION_INSULIN = "drug_his_selection_insulin";
    //用户历史药物搜索key
    public static final String DRUG_HIS_SEARCH_MED = "drug_his_search_med";
    //用户历史胰岛素搜索key
    public static final String DRUG_HIS_SEARCH_INSULIN = "drug_his_search_insulin";
    //药物选择后发送广播的key
    public static final String DRUG_SELECTION_BROAD_CAST = "drug_selection_broad_cast";
    //首页是否首次弹出隐私政策弹框sp key
    public static final String LAUNCHER_PAGE_FIRST_OPEN_PRIVACY_POP = "launcher_page_first_open_privacy_pop";
    //扫码识别的授权请求前缀
    public static final String SCAN_RESULT_AUTHORIZATION_REQUEST_PREFIX = "(SI:Outpatient:User)::";
    //扫码识别的门诊连接二维码前缀
    public static final String SCAN_RESULT_OUTPATIENT_CONNECTION_QR_CODE_PREFIX = "(SI:Outpatient:Conn)::";
    //蓝牙开关打开位置未打开的广播
    public static final String BROADCAST_BLUETOOTH_OPEN_LOCATION_NOT_OPEN = "broadcast_bluetooth_open_location_not_open";
    //FCM token 刷新的action
    public static final String BROADCAST_REFRESH_FCM_TOKEN = "broadcast_refresh_fcm_token";
      //获取处理关注邀请的列表
    public static final String BROADCAST_REFRESH_TO_DO_FOLLOWER_LIST = "broadcast_refresh_to_do_follower_list";
    public static final String BROADCAST_REFRESH_TO_DO_FOLLOWER_INSTITUTION = "broadcast_refresh_to_do_follower_institution";
    //切换单位显示的广播
    public static final String BROADCAST_REFRESH_UNIT = "broadcast_refresh_unit";
    //邮箱正则表达式
    //public static final String EMAIL_FORMAT = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final String EMAIL_FORMAT = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";

    //语言选择
    public  static final String TAG_LANGUAGE = "language_select";
   //语言选择
    public  static  String CURRENT_LANGUAGE = "en_us";
    public  static  boolean IS_APP_IN_BACKGROUND = false;

    //默认语言
    public static Locale systemCurrentLocal = Locale.ENGLISH;

    /**
     * mmol/L、mg/dL相互转换的单位
     */
    public static final float BS_UNIT_CHANGE_NUMBER = 18.016f;

    /**
     * 血糖单位
     */
    public static final String BS_UNIT_MMOL = "mmol/L";
    public static final String BS_UNIT_MG = "mg/dL";

    /**
     * 上限的最低范围
     */
    //public static final float UPPER_LOWER = 10;
    /**
     * 上限的最高范围
     */
    //public static final float UPPER_UPPER = 20;
    /**
     * 下限的最低范围
     */
    //public static final float LOWER_LOWER = 1.5f;
    /**
     * 下限的最高范围
     */
    //public static final float LOWER_UPPER = 10;

    /**
     * 默认上限
     */
    public static final float BS_UPPER_DEFAULT = 7.8f;
    public static final float BS_UPPER_TYPE_1_2 =  10.0f;

    public static final float BS_UPPER_TYPE_GESTATION =  7.8F;

    public static final float BS_UPPER_TYPE_ELDER_HIGH =  10.0F;

    /**
     * 默认下限
     */
    public static final float BS_LOWER_DEFAULT = 3.9f;

    public static final float BS_LOWER_GESTATION = 3.5f;

    public static final float BS_LOWER_ELDER_HIGH = 3.9f;

    /**
     * 血糖最大值
     */
    public static final float BS_MAX = 25;

    /**
     * 血糖最小值
     */
    public static final float BS_MIN = 2.2f;

    //扫码识别的DM码前缀
    public static final String SCAN_RESULT_DM_CODE = "0697283164";
    //8位连接码最后一位是p-z则是B端连接码
    public static final String SCAN_RESULT_P_TO_Z = "pqrstuvwxyzPQRSTUVWXYZ";

    /**
     * 血糖笔数最大值
     */
    public static int BS_INDEX_MAX = 20160;

    //连接之前的设备的广播
    public static final String BROADCAST_CONNECT_BEFORE_DEVICE = "broadcast_connect_before_device";

    public static final String ACTIVITY_COULD_BACK = "activity_could_back";

    public static final String DEVICE_DAMAGE_OR_FAILURE_KEY= "device_damage_or_failure_key";
    public static final String BLE_KEY_OLD = "03AF4CB3721D0C94F8B4B2375A8752D5CBE7A17814B502D9132489C0BFDFC99F0CAC670E8CBB085AF1E78AA083D3CD";
    public static final String BLE_KEY = "56CE249349040C94F8B4B2375A8752D5CBE7A17814B502D9132489C0BFDFC99F0CAC670E8CBB085AF1C780B3D282E3";

    //当前app对应的协议版本
    public static String PRIVACY_VERSION = "v2";
    public static String ARGUMENT_VERSION = "v2";
}
