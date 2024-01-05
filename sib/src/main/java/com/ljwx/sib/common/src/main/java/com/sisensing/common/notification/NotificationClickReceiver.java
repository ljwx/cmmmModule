package com.sisensing.common.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public final class NotificationClickReceiver extends BroadcastReceiver {

    public final static String CLICK_NOTIFICATION = "CLICK_NOTIFICATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(NotificationClickReceiver.CLICK_NOTIFICATION)) {
            if (NotificationConfig.foregroundNotification != null) {
                if (NotificationConfig.foregroundNotification.getForegroundNotificationClickListener() != null) {
                    NotificationConfig.foregroundNotification.getForegroundNotificationClickListener().foregroundNotificationClick(context, intent);
                }
            }
        }
    }
}
