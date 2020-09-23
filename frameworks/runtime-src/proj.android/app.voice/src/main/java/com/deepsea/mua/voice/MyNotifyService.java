package com.deepsea.mua.voice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import com.deepsea.mua.stub.utils.NotificationUtils;
import static com.umeng.socialize.utils.DeviceConfigInternal.context;

public class MyNotifyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //设置点击跳转

        String id = "com.hehegame.chess";
        String name = "hehegame";
        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setSound(null, null);
            mChannel.enableLights(true);
            mChannel.setLightColor(getApplication().getResources().getColor(R.color.gray));
            mChannel.setShowBadge(true);
            mChannel.setDescription(getApplication().getString(R.string.app_name));
            // 设置显示模式
            mChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(getBaseContext())
                    .setContentTitle(getBaseContext().getResources().getString(R.string.app_name))
                    .setAutoCancel(false)// 设置这个标志当用户单击面板就可以让通知将自动取消
                    .setOngoing(true)// true，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                    .setChannelId(id)
                    .setSmallIcon(R.drawable.ssdk_logo)
                    .setContentTitle(name)
                    .setContentText(name)
                    .build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplication())
                    .setContentTitle(getApplication().getResources().getString(R.string.app_name))
                    .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                    .setAutoCancel(false)// 设置这个标志当用户单击面板就可以让通知将自动取消
                    .setOngoing(true)// true，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,
                    // 因此占用设备(如一个文件下载,同步操作,主动网络连接)
                    .setSmallIcon(R.drawable.ssdk_logo);
            notification = notificationBuilder.build();

        }
        startForeground(1001, notification);

    }
}
