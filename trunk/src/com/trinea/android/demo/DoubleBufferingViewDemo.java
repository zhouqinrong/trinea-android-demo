package com.trinea.android.demo;

import android.app.Activity;
import android.os.Bundle;

public class DoubleBufferingViewDemo extends Activity {

    DoubleBufferingView view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new DoubleBufferingView(this);
        setContentView(view);
    }
}
