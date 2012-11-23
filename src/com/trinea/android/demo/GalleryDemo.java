package com.trinea.android.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.Toast;

import com.trinea.android.demo.adapter.ImageListAdapter;

public class GalleryDemo extends Activity {

    private Gallery imageGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_demo);

        imageGallery = (Gallery)findViewById(R.id.app_app_image_gallery);
        imageGallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "aaaaa", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(context, ImageViewActivity.class);
                // intent.putExtra(IntentParaConstants.IMAGE_URL, currentApp.getImageUrlList().get(position));
                // intent.putStringArrayListExtra(IntentParaConstants.IMAGE_URLS,
                // (ArrayList<String>)currentApp.getImageUrlList());
                // startActivity(intent);
            }
        });
        ImageListAdapter adapter = new ImageListAdapter(getApplicationContext());
        List<Integer> idList = new ArrayList<Integer>();
        idList.add(R.drawable.jpg1);
        idList.add(R.drawable.jpg2);
        idList.add(R.drawable.jpg3);
        idList.add(R.drawable.jpg4);
        adapter.setImageList(idList);
        imageGallery.setAdapter(adapter);
    }
}
