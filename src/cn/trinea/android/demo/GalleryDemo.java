package cn.trinea.android.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Gallery;
import cn.trinea.android.demo.adapter.ImageListAdapter;
import cn.trinea.android.demo.utils.AppUtils;

/**
 * GalleryDemo
 * 
 * @author Trinea 2013-5-9
 */
public class GalleryDemo extends Activity {

    private Gallery imageGallery;

    private Button  trineaInfoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_demo);

        AppUtils.initTrineaInfo(this, trineaInfoTv, getClass());

        ActionBar bar = getActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);

        imageGallery = (Gallery)findViewById(R.id.app_app_image_gallery);
        imageGallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        ImageListAdapter adapter = new ImageListAdapter(getApplicationContext());
        List<Integer> idList = new ArrayList<Integer>();
        idList.add(R.drawable.image1);
        idList.add(R.drawable.image2);
        idList.add(R.drawable.image3);
        adapter.setImageList(idList);
        imageGallery.setAdapter(adapter);
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
