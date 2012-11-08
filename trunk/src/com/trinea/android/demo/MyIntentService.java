package com.trinea.android.demo;

import android.app.IntentService;
import android.content.Intent;

public class MyIntentService extends IntentService {

    public MyIntentService(){
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            System.out.println("IntentService1 Begin Sleep. " + "Thread name: " + Thread.currentThread().getName()
                               + ", Thread Id: " + Thread.currentThread().getId());
            Thread.sleep(3000);
            System.out.println("IntentService1 End. ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
