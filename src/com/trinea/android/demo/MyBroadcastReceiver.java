/*
 * Copyright 2012 Trinea.com All right reserved. This software is the
 * confidential and proprietary information of Trinea.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Trinea.com.
 */
package com.trinea.android.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * BroadcastReceiver Demo，以接收短信为例
 * 
 * @author Trinea 2012-9-20 下午03:05:14
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // String msg = intent.getStringExtra("msg");
        // Toast.makeText(context, "广播已经收到，内容为：" + msg, Toast.LENGTH_SHORT).show();
        receiveSms(context, intent);
    }

    /**
     * 获取接收到的短信内容显示
     */
    public void receiveSms(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        SmsMessage[] msgs;
        StringBuilder s = new StringBuilder();
        if (b != null) {
            Object[] pdus = (Object[])b.get("pdus");
            if (pdus != null && pdus.length > 0) {
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    s.append(msgs[i].getOriginatingAddress()).append(":\n             ").append(msgs[i].getMessageBody().toString());
                }
            }
        }
        Toast.makeText(context, s.toString(), Toast.LENGTH_SHORT).show();
    }
}
