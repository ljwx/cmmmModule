package com.ljwx.baseservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat

class BaseForegroundService :Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return Binder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mBuilder = NotificationCompat.Builder(getApplicationContext()).setAutoCancel(true);// 点击后让通知将消失
        mBuilder.setContentText("测试");
        mBuilder.setContentTitle("测试");
//        mBuilder.setSmallIcon(R.mipmap.app);
        mBuilder.setWhen(System.currentTimeMillis());//通知产生的时间，会在通知信息里显示
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);//设置该通知优先级
        mBuilder.setOngoing(false);//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
        mBuilder.setDefaults(Notification.DEFAULT_ALL);//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val manager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelId = "channelId"
            val channel =  NotificationChannel(channelId, "test", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mBuilder.setContentIntent(null);
        startForeground(222, mBuilder.build());
        return super.onStartCommand(intent, flags, startId)
    }

}