package cn.trinea.android.demo;

import java.util.Arrays;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import cn.trinea.android.demo.utils.AppUtils;

/**
 * demo list 列表
 * 
 * @author Trinea 2012-6-17 下午06:14:57
 */
public class DemoList extends Activity {

    public static final String    TAG      = "DemoList";

    private static final String[] mStrings = { "BorderScrollViewDemo", "DownloadManager Demo",
            "SearchView Demo", "ViewPager Multi Fragment Demo\r\n(ViewPager一屏多Fragment)",
            "Gallery Demo", "ViewPager Demo", "Service Demo", "BroadcastReceiver Demo" };

    private static final int      total    = mStrings.length - 1;

    private Button                trineaInfoTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_list);

        AppUtils.initTrineaInfo(this, trineaInfoTv, getClass());
        LinkedList<String> mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                                                this,
                                                                android.R.layout.simple_list_item_1,
                                                                mListItems);

        ListView demoListView = (ListView)findViewById(R.id.simpleListView);
        demoListView.setAdapter(adapter);
        demoListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == total - 7) {
                    Intent intent = new Intent(DemoList.this, BorderScrollViewDemo.class);
                    startActivity(intent);
                } else if (position == total - 6) {
                    Intent intent = new Intent(DemoList.this, DownloadManagerDemo.class);
                    startActivity(intent);
                } else if (position == total - 5) {
                    Intent intent = new Intent(DemoList.this, SearchViewDemo.class);
                    startActivity(intent);
                } else if (position == total - 4) {
                    Intent intent = new Intent(DemoList.this, ViewPagerMulTiFragmentDemo.class);
                    startActivity(intent);
                } else if (position == total - 3) {
                    Intent intent = new Intent(DemoList.this, GalleryDemo.class);
                    startActivity(intent);
                } else if (position == total - 2) {
                    Intent intent = new Intent(DemoList.this, ViewPagerDemo.class);
                    startActivity(intent);
                } else if (position == total - 1) {
                    Intent intent = new Intent(DemoList.this, ServiceDemo.class);
                    startActivity(intent);
                } else if (position == total) {
                    Intent intent = new Intent(DemoList.this, BroadcastReceiverDemo.class);
                    startActivity(intent);
                }
            }
        });
    }
}
