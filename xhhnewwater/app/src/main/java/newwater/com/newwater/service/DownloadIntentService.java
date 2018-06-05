package newwater.com.newwater.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.File;

import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.interfaces.DownloadCallback;
import newwater.com.newwater.manager.RetrofitHttp;
import newwater.com.newwater.utils.SPDownloadUtil;

public class DownloadIntentService extends IntentService {
    private static final String TAG = "xzxz: DownIntentService";
//    private NotificationManager mNotifyManager;
    private String mDownloadFileName;
//    private Notification mNotification;

    public DownloadIntentService() {
        super("InitializeService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ----");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: ----");
        String downloadUrl = intent.getExtras().getString(Constant.VIDEO_DOWNLOAD_URL);
        final int downloadId = intent.getExtras().getInt(Constant.VIDEO_DOWNLOAD_ID);
        mDownloadFileName = intent.getExtras().getString(Constant.VIDEO_DOWNLOAD_FILE_NAME);

        Log.d(TAG, "url --" + downloadUrl);
        Log.d(TAG, "file_name --" + mDownloadFileName);

        final File file = new File(UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR + mDownloadFileName);
        long range = 0;
        int progress = 0;
        if (file.exists()) {
            range = SPDownloadUtil.getInstance().get(downloadUrl, 0);
            Log.d(TAG, "onHandleIntent: range = " + range);
            progress = (int) (range * 100 / file.length());
            if (range == file.length()) {
//                installApp(file);
                return;
            }
        }

        Log.d(TAG, "range = " + range);

/*        final RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notify_download);
        remoteViews.setProgressBar(R.id.pb_progress, 100, progress, false);
        remoteViews.setTextViewText(R.id.tv_progress, "已下载" + progress + "%");

        final NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContent(remoteViews)
                        .setTicker("正在下载")
                        .setSmallIcon(R.mipmap.ic_launcher);

        mNotification = builder.build();

        mNotifyManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyManager.notify(downloadId, mNotification);*/

        RetrofitHttp.getInstance().downloadFile(range, downloadUrl, mDownloadFileName, new DownloadCallback() {
            @Override
            public void onProgress(int progress) {
                Log.d(TAG, "下载进度：" + progress);
                /*remoteViews.setProgressBar(R.id.pb_progress, 100, progress, false);
                remoteViews.setTextViewText(R.id.tv_progress, "已下载" + progress + "%");
                mNotifyManager.notify(downloadId, mNotification);*/
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "下载完成");
//                mNotifyManager.cancel(downloadId);
//                installApp(file);
            }

            @Override
            public void onError(String msg) {
//                mNotifyManager.cancel(downloadId);
                Log.d(TAG, "下载发生错误--" + msg);
            }
        });
    }

    private void installApp(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
