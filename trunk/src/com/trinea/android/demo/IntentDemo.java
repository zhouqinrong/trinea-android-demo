/*
 * Copyright 2012 Trinea.com All right reserved. This software is the
 * confidential and proprietary information of Trinea.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Trinea.com.
 */
package com.trinea.android.demo;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Intent Demo
 * 
 * @author Trinea 2012-9-20 上午09:28:36
 */
public class IntentDemo extends Activity {

    private String[] mStrings = {"调用程序发送短信", "发送简单邮件", "发送复杂邮件", "打电话", "查看联系人", "拍照"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_list);

        LinkedList<String> mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);

        ListView demoListView = (ListView)findViewById(R.id.simpleListView);
        demoListView.setAdapter(adapter);
        demoListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Intent it = new Intent(Intent.ACTION_VIEW);
                    // it.putExtra("address", "15800000000");
                    // it.putExtra("sms_body", "The SMS text");
                    // it.setType("vnd.android-dir/mms-sms");
                    // startActivity(it);
                    Uri uri = Uri.parse("smsto:15800000000");
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    i.putExtra("sms_body", "The SMS text");
                    startActivity(i);
                } else if (position == 1) {
                    Uri email = Uri.parse("mailto:abc@126.com;def@126.com");
                    Intent i = new Intent(Intent.ACTION_SENDTO, email);
                    startActivity(i);
                } else if (position == 2) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    String[] tos = {"abc@126.com"};
                    String[] ccs = {"def@126.com"};
                    i.putExtra(Intent.EXTRA_EMAIL, tos);
                    i.putExtra(Intent.EXTRA_CC, ccs);
                    i.putExtra(Intent.EXTRA_SUBJECT, "主题");
                    i.putExtra(Intent.EXTRA_TEXT, "正文");
                    i.setType("message/rfc822");
                    startActivity(i);
                    // startActivity(Intent.createChooser(i, "选择程序发送邮件"));
                } else if (position == 3) {
                    Uri dial = Uri.parse("tel:15800000000");
                    Intent i = new Intent(Intent.ACTION_DIAL, dial);
                    startActivity(i);
                } else if (position == 4) {
                    Intent i = new Intent();
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    i.setType("vnd.android.cursor.item/phone");
                    startActivityForResult(i, 1);
                    // Uri contact = Uri.parse("content://contacts/people");
                    // startActivityForResult(new Intent(Intent.ACTION_PICK, contact), 1);
                } else if (position == 5) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                                        + "Trinea" + File.separator + "AndroidDemo" + File.separator;
                    String filePath = folderPath + System.currentTimeMillis() + ".jpg";
                    new File(folderPath).mkdirs();
                    File camerFile = new File(filePath);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(camerFile));
                    startActivityForResult(i, 1);
                    // Intent i = new Intent(Intent.ACTION_CAMERA_BUTTON);
                    // sendBroadcast(i);
                }
            }
        });
    }
}
