package cn.trinea.android.demo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.demo.utils.AppUtils;

import com.trinea.android.common.view.BorderScrollView;
import com.trinea.android.common.view.BorderScrollView.OnBorderListener;

/**
 * BorderScrollViewDemo
 * 
 * @author Trinea 2013-5-27
 */
public class BorderScrollViewDemo extends Activity {

    private BorderScrollView borderScrollView;
    private TextView         textView1;
    private TextView         textView2;

    private Button           trineaInfoTv;

    private Context          context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.border_scroll_view_demo);

        context = getApplicationContext();
        AppUtils.initTrineaInfo(this, trineaInfoTv, getClass());

        ActionBar bar = getActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);

        borderScrollView = (BorderScrollView)findViewById(R.id.scroll_view);
        borderScrollView.setOnBorderListener(new OnBorderListener() {

            @Override
            public void onTop() {
                // may be done multi times, u should control it
                Toast.makeText(context, "has reached top", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBottom() {
                // may be done multi times, u should control it
                Toast.makeText(context, "has reached bottom", Toast.LENGTH_SHORT).show();
            }
        });
        textView1 = (TextView)findViewById(R.id.text1);
        textView2 = (TextView)findViewById(R.id.text2);

        Display display = getWindowManager().getDefaultDisplay();
        textView1.setHeight(display.getHeight() / 2);
        textView2.setHeight(display.getHeight() / 2);
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
