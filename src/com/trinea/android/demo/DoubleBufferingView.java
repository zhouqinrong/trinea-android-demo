/*
 * Copyright 2012 Trinea.com All right reserved. This software is the
 * confidential and proprietary information of Trinea.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Trinea.com.
 */
package com.trinea.android.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Paint;
import android.view.View;

/**
 * 双缓冲机制 Demo
 * 
 * @author Trinea 2012-10-17 下午03:00:36
 */
public class DoubleBufferingView extends View {

    public DoubleBufferingView(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap cache = Bitmap.createBitmap(480, 800, Config.ARGB_8888);
        Canvas mCanvas = new Canvas(cache);
        mCanvas.drawBitmap((BitmapDrawable)getResources().getDrawable(R.drawable.renren)).get , 0, 0, new Paint());
    }
}
