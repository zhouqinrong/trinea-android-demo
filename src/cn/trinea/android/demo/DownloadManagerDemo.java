package cn.trinea.android.demo;

import java.io.File;
import java.text.DecimalFormat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.trinea.android.demo.utils.AppUtils;
import cn.trinea.android.demo.utils.DownloadManagerPro;

/**
 * DownloadManagerDemo
 * 
 * @author Trinea 2013-5-9
 */
public class DownloadManagerDemo extends Activity {

    public static final String     DOWNLOAD_FOLDER_NAME = File.separator + "Trinea"
                                                          + File.separator;
    public static final String     DOWNLOAD_FILE_NAME   = "MeLiShuo.apk";

    // public static final String APK_URL =
    // "http://img.meilishuo.net/css/images/AndroidShare/Meilishuo_3.6.1_10006.apk";
    public static final String     APK_URL              = "http://gdown.baidu.com/data/wisegame/283ccd89b3eb717c/wojiaoMT.apk";

    private Button                 downloadButton;
    private ProgressBar            downloadProgress;
    private TextView               downloadTip;
    private TextView               downloadSize;
    private TextView               downloadPrecent;
    private Button                 trineaInfoTv;

    private DownloadManager        downloadManager;
    private DownloadManagerPro     downloadManagerPro;
    private long                   downloadId           = 0;

    private Context                context;
    private MyHandler              handler;

    private DownloadChangeObserver downloadObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_manager_demo);

        AppUtils.initTrineaInfo(this, trineaInfoTv, getClass());

        context = getApplicationContext();
        handler = new MyHandler();
        downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
        downloadManagerPro = new DownloadManagerPro(downloadManager);

        initView();
        initData();

        /** register download success broadcast **/
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void initView() {
        ActionBar bar = getActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP);

        downloadButton = (Button)findViewById(R.id.download_button);
        downloadProgress = (ProgressBar)findViewById(R.id.download_progress);
        downloadTip = (TextView)findViewById(R.id.download_tip);
        downloadTip.setText(getString(R.string.tip_download_file) + DOWNLOAD_FOLDER_NAME);
        downloadSize = (TextView)findViewById(R.id.download_size);
        downloadPrecent = (TextView)findViewById(R.id.download_precent);
    }

    private void initData() {
        downloadObserver = new DownloadChangeObserver();

        /**
         * get download id from preferences.<br/>
         * if download id bigger than 0, means it has been downloaded, then query status and show right text;
         */
        downloadId = PreferencesUtils.getLongPreferences(context,
                                                         PreferencesUtils.KEY_NAME_DOWNLOAD_ID);
        if (downloadId > 0) {
            int status = downloadManagerPro.getStatusById(downloadId);
            if (status == DownloadManager.STATUS_PAUSED || status == DownloadManager.STATUS_PENDING
                || status == DownloadManager.STATUS_RUNNING) {
                downloadButton.setText(getString(R.string.app_status_downloading));
            } else if (status == DownloadManager.STATUS_FAILED) {
                downloadButton.setText(getString(R.string.app_status_download_fail));
            } else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                downloadButton.setText(getString(R.string.app_status_downloaded));
            } else {
                downloadButton.setText(getString(R.string.app_status_download));
            }
            updateCurrentBytes();
        } else {
            downloadButton.setText(getString(R.string.app_status_download));
        }

        downloadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                int status = downloadManagerPro.getStatusById(downloadId);
                /**
                 * if download status not paused, pedding, running, then download.
                 */
                if (status != DownloadManager.STATUS_PAUSED
                    && status != DownloadManager.STATUS_PENDING
                    && status != DownloadManager.STATUS_RUNNING) {

                    File folder = new File(DOWNLOAD_FOLDER_NAME);
                    if (!folder.exists() || !folder.isDirectory()) {
                        folder.mkdirs();
                    }

                    DownloadManager.Request request = new DownloadManager.Request(
                                                                                  Uri.parse(APK_URL));
                    request.setAllowedNetworkTypes(Request.NETWORK_WIFI | Request.NETWORK_MOBILE);
                    request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME,
                                                              DOWNLOAD_FILE_NAME);
                    request.setTitle(getString(R.string.download_notification_title));
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    downloadId = downloadManager.enqueue(request);
                    if (downloadId == -1) {
                        Toast.makeText(context, getString(R.string.download_fail),
                                       Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, getString(R.string.download_begin),
                                       Toast.LENGTH_SHORT).show();

                        /** save download id to preferences **/
                        PreferencesUtils.putLongPreferences(context,
                                                            PreferencesUtils.KEY_NAME_DOWNLOAD_ID,
                                                            downloadId);
                        getContentResolver().registerContentObserver(DownloadManagerPro.CONTENT_URI,
                                                                     true, downloadObserver);
                        initData();
                    }
                }
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

                                           @Override
                                           public void onReceive(Context context, Intent intent) {
                                               /**
                                                * get the id of download which have download success, if the id is my id
                                                * and it's status is successful, then install it
                                                **/
                                               long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,
                                                                                             -1);
                                               if (completeDownloadId == downloadId) {
                                                   initData();
                                                   if (downloadObserver != null) {
                                                       getContentResolver().unregisterContentObserver(downloadObserver);
                                                   }
                                                   handler.sendMessage(handler.obtainMessage(0, 0,
                                                                                             0));

                                                   if (downloadManagerPro.getStatusById(downloadId) == DownloadManager.STATUS_SUCCESSFUL) {
                                                       String apkFilePath = new StringBuilder(
                                                                                              Environment.getExternalStorageDirectory()
                                                                                                         .getAbsolutePath()).append(DOWNLOAD_FOLDER_NAME)
                                                                                                                            .append(DOWNLOAD_FILE_NAME)
                                                                                                                            .toString();
                                                       install(context, apkFilePath);
                                                   }
                                               }
                                           }
                                       };

    /**
     * install app
     * 
     * @param context
     * @param filePath
     * @return whether apk exist
     */
    public static boolean install(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
            i.setDataAndType(Uri.parse("file://" + filePath),
                             "application/vnd.android.package-archive");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            return true;
        }
        return false;
    }

    class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver(){
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateCurrentBytes();
        }

    }

    public void updateCurrentBytes() {
        int[] bytesAndStatus = downloadManagerPro.getBytesAndStatus(downloadId);
        if (bytesAndStatus[2] != 0) {
            handler.sendMessage(handler.obtainMessage(0, bytesAndStatus[0], bytesAndStatus[1]));
        }
    }

    /**
     * MyHandler
     * 
     * @author Trinea 2012-12-18
     */
    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    downloadProgress.setVisibility(View.VISIBLE);
                    if (msg.arg2 < 0) {
                        downloadProgress.setIndeterminate(true);
                        downloadPrecent.setText("0%");
                        downloadSize.setText("0M/0M");
                    } else {
                        downloadProgress.setIndeterminate(false);
                        downloadProgress.setMax(msg.arg2);
                        downloadProgress.setProgress(msg.arg1);
                        downloadPrecent.setText(getNotiPercent(msg.arg1, msg.arg2));
                        downloadSize.setText(getAppSize(msg.arg1) + "/" + getAppSize(msg.arg2));
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
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

    static final DecimalFormat DOUBLE_DECIMAL_FORMAT = new DecimalFormat("0.##");

    public static final int    MB_2_BYTE             = 1024 * 1024;
    public static final int    KB_2_BYTE             = 1024;

    /**
     * @param size
     * @return
     */
    public static CharSequence getAppSize(long size) {
        if (size <= 0) {
            return "0M";
        }

        if (size >= MB_2_BYTE) {
            return new StringBuilder(16).append(DOUBLE_DECIMAL_FORMAT.format((double)size
                                                                             / MB_2_BYTE))
                                        .append("M");
        } else if (size >= KB_2_BYTE) {
            return new StringBuilder(16).append(DOUBLE_DECIMAL_FORMAT.format((double)size
                                                                             / KB_2_BYTE))
                                        .append("K");
        } else {
            return size + "B";
        }
    }

    public static String getNotiPercent(long progress, long max) {
        int rate = 0;
        if (progress <= 0 || max <= 0) {
            rate = 0;
        } else if (progress > max) {
            rate = 100;
        } else {
            rate = (int)((double)progress / max * 100);
        }
        return new StringBuilder(16).append(rate).append("%").toString();
    }
}
