package cn.trinea.android.demo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import cn.trinea.android.demo.adapter.ImageListAdapter;

/**
 * GalleryDemo
 * 
 * @author Trinea 2013-5-9
 */
public class GalleryDemo extends BaseActivity {

    private Gallery imageGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.gallery_demo);

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

}
