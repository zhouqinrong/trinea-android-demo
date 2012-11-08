/*
 * Copyright 2012 Trinea.com All right reserved. This software is the
 * confidential and proprietary information of Trinea.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Trinea.com.
 */
package com.trinea.android.demo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Notification Demo
 * 
 * @author Trinea 2012-9-20 下午02:39:53
 */
public class NotificationDemo extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_demo);

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = android.R.drawable.stat_notify_chat;
        long when = System.currentTimeMillis() + 2000;
        Notification n = new Notification(icon, "通知栏demo提醒", when);
        n.defaults = Notification.DEFAULT_SOUND;
        // n.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        n.flags |= Notification.FLAG_NO_CLEAR;
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        Intent openintent = new Intent(this, SmsManagerDemo.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, openintent, PendingIntent.FLAG_CANCEL_CURRENT);
        n.setLatestEventInfo(this, "通知栏demo提醒title", "点击打开发送短信页面", pi);
        nm.notify(0, n);
    }
}
