package com.sisensing.common.notification;


import com.sisensing.common.R;

/**
 * 进程保活需要配置的属性
 */
public class NotificationConfig {

    public static final int FOREGROUD_NOTIFICATION_ID = 8888;

    /**
     * 进程开启的广播
     */
    public static final String PROCESS_ALIVE_ACTION = "PROCESS_ALIVE_ACTION";
    public static final String PROCESS_STOP_ACTION = "PROCESS_STOP_ACTION";
    public static ForegroundNotification foregroundNotification = null;


    /**
     * 广播通知的 action
     */
    public static String NOTIFICATION_ACTION = "NOTIFICATION_ACTION";

    public static int TITLE = R.string.app_name;
    public static int CONTENT = R.string.launcher_bs_monitoring_system;
    public static int DEF_ICONS = R.mipmap.ic_launcher_cgm;

}
