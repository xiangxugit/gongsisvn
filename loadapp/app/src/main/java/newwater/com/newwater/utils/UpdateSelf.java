package newwater.com.newwater.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.io.File;
import org.xutils.x;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class UpdateSelf {
    private String url;
    private Context context;
    public UpdateSelf(Context context, String updateurl) {
        this.url = updateurl;
        this.context = context;
    }
    public void UpdateApk(){
        RequestParams params = new RequestParams(url);
//自定义保存路径，Environment.getExternalStorageDirectory()：SD卡的根目录
        params.setSaveFilePath(Environment.getExternalStorageDirectory()+"/myapp/");
//自动为文件命名
        params.setAutoRename(true);
        x.http().post(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                //apk下载完成后，调用系统的安装方法
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(result), "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
            //网络请求之前回调
            @Override
            public void onWaiting() {
            }
            //网络请求开始的时候回调
            @Override
            public void onStarted() {
            }
            //下载的时候不断回调的方法
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //当前进度和文件总大小
                Log.i("JAVA","current："+ current +"，total："+total);
            }
        });
       }
}
