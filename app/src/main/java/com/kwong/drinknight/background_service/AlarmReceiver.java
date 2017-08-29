package com.kwong.drinknight.background_service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.kwong.drinknight.home_page.MainActivity;
import com.kwong.drinknight.R;

import static com.kwong.drinknight.home_page.MainActivity.sendRequestWithOkHttp;
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //设置通知内容并在onReceive()这个函数执行时开启
        Intent i = new Intent(context,MainActivity.class);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pi = PendingIntent.getActivity(context,0,i,0);
        Notification builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("您有一段时间没有喝水啦！")
                .setContentIntent(pi)
                .setContentTitle("该喝水了")
                .setAutoCancel(true)
                .build();
        manager.notify(1,builder);
        Log.d("AlarmReceiver","onReceive is executed");
        sendRequestWithOkHttp();
        //再次开启NotifyService这个服务，从而可以
        Intent in = new Intent(context, NotifyService.class);
        context.startService(in);
    }


}
