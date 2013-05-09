/*
 * Copyright 2012 Trinea.cn All right reserved. This software is the
 * confidential and proprietary information of Trinea.cn ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Trinea.cn.
 */
package cn.trinea.android.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.trinea.android.demo.utils.AppUtils;

/**
 * 使用sms manager发短信，接收短信的例子见{@link MyBroadcastReceiver}
 * 
 * @author Trinea 2012-9-20
 */
public class SmsManagerDemo extends Activity {

    private final static String SEND_ACTION      = "send";
    private final static String DELIVERED_ACTION = "delivered";

    private EditText            smsTextTv;
    private EditText            smsReceiverTv;
    private Button              sendBtn;

    private Button              trineaInfoTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_manager_demo);

        AppUtils.initTrineaInfo(this, trineaInfoTv, getClass());

        smsTextTv = (EditText)findViewById(R.id.smsText);
        smsReceiverTv = (EditText)findViewById(R.id.smsReceiver);
        smsReceiverTv.setText("10086");
        smsTextTv.setText("hello");
        sendBtn = (Button)findViewById(R.id.sendSms);
        sendBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sendBtn.setText("发送中");
                sendBtn.setClickable(false);
                sendSms(smsReceiverTv.getText().toString(), smsTextTv.getText().toString());
            }
        });

        ActionBar bar = getActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);
    }

    private void sendSms(String receiver, String text) {
        SmsManager s = SmsManager.getDefault();
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SEND_ACTION),
                                                          PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                                                               new Intent(DELIVERED_ACTION),
                                                               PendingIntent.FLAG_CANCEL_CURRENT);
        // 发送完成
        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                sendBtn.setText("发送");
                sendBtn.setClickable(true);

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Send Success!", Toast.LENGTH_SHORT)
                             .show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(),
                                       "Send Failed because generic failure cause.",
                                       Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(),
                                       "Send Failed because service is currently unavailable.",
                                       Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Send Failed because no pdu provided.",
                                       Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(),
                                       "Send Failed because radio was explicitly turned off.",
                                       Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getBaseContext(), "Send Failed.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SEND_ACTION));

        // 对方接受完成
        registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Delivered Success!", Toast.LENGTH_SHORT)
                             .show();
                        break;
                    default:
                        Toast.makeText(getBaseContext(), "Delivered Failed!", Toast.LENGTH_SHORT)
                             .show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED_ACTION));

        // 发送短信，sentPI和deliveredPI将分别在短信发送成功和对方接受成功时被广播
        s.sendTextMessage(receiver, null, text, sentPI, deliveredPI);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }
}
