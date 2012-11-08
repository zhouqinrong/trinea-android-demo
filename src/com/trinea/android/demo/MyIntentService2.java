package com.trinea.android.demo;

import android.app.IntentService;
import android.content.Intent;

public class MyIntentService2 extends IntentService {

    public MyIntentService2(){
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            System.out.println("IntentService2 Begin Sleep. " + "Thread name: " + Thread.currentThread().getName()
                               + ", Thread Id: " + Thread.currentThread().getId());
            Thread.sleep(3000);
            System.out.println("IntentService2 End. ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
