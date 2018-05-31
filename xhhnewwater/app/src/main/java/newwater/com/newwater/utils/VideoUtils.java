package newwater.com.newwater.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.List;
import java.util.Map;

import newwater.com.newwater.TestJSON;
import newwater.com.newwater.beans.Advs_Video;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class VideoUtils {
    private Context context;
    private Callback.Cancelable cancelable;

    /**
     * 进度条
     */
    private ProgressDialog progressDialog;
    public VideoUtils(Context context){
        this.context = context;
    }

    //https://blog.csdn.net/wangbaochu/article/details/50943335 进程保活

    //https://www.cnblogs.com/bugly/p/5765334.html

     //https://blog.csdn.net/qingwenje2008/article/details/76186727  //断电续传


    //判断是否空闲
    public void downloadvideo(){
        boolean getwatering = true;
        if(true){
            String testa = TestJSON.strategy();
            JSONArray alldata = JSON.parseArray(testa);
            String test = alldata.getString(0);
            com.alibaba.fastjson.JSONObject testobj = JSON.parseObject(test);
            String videoListString = testobj.getString("videoList");
            Log.e("videoListString","videoListString"+videoListString);
            final JSONArray videolist = JSON.parseArray(videoListString);
            //准备断点续传


            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("下载文件");
            //设置信息
            progressDialog.setMessage("下载中....");
            //设置信息格式
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //

            progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "暂停",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //点击取消正在下载的操作
                    cancelable.cancel();
            }});
            //开始断点续传
            for(int i=0;i<videolist.size();i++){
                doDownload(videolist.getString(i));
            }


        }
    }





    //TODO 从服务器获取到播放列表


    //TODO 根据播放视频的名称来检索 缓存文件夹是否有文件，如果没有的话加入到nocachelist 如果有的话加入到cacheList

    //TODO 获取本地播放策略和网络播放策略进行对比，如果是一样的，就按照本地播放策略，如果不一样，更新本地播放策略

    //TODO 播放处理

    /**
     *根据播放策略定闹钟，播放对应的视频
     *
     *
     *
     */


    /**
     * Returns the file extension or an empty string iff there is no
     * extension. This method is a convenience method for obtaining the
     * extension of a url and has undefined results for other Strings.
     * @param url
     * @return The file extension of the given url.
     */
    public static String getFileExtensionFromUrl(String url) {
        String filename = url.trim();

        filename = filename.substring(filename.lastIndexOf("/") + 1);
        return filename;

    }


    public void doDownload(String url){
        RequestParams requestParams = new RequestParams(url);
        requestParams.setAutoResume(true);//设置为断点续传
        requestParams.setAutoRename(false);
        String name = getFileExtensionFromUrl(url);
        requestParams.setSaveFilePath("/sdcard/xutils/"+name);
        requestParams.setExecutor(new PriorityExecutor(2, true));
        requestParams.setCancelFast(true);
        cancelable = x.http().get(requestParams, new Callback.ProgressCallback<File>()  {
            @Override
            public void onCancelled(CancelledException arg0) {
                Log.e("tag", "取消"+Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("tag", "onError: 失败"+Thread.currentThread().getName());
                progressDialog.dismiss();
            }

            @Override
            public void onFinished() {
                Log.e("tag", "完成,每次取消下载也会执行该方法"+Thread.currentThread().getName());
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(File arg0) {
                Log.e("tag", "下载成功的时候执行"+Thread.currentThread().getName());
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (isDownloading) {
                    progressDialog.setProgress((int) (current*100/total));
                    Log.e("tag", "下载中,会不断的进行回调:"+Thread.currentThread().getName());
                }
            }

            @Override
            public void onStarted() {
                Log.e("tag", "开始下载的时候执行"+Thread.currentThread().getName());
                progressDialog.show();
            }

            @Override
            public void onWaiting() {
                Log.e("tag", "等待,在onStarted方法之前执行"+Thread.currentThread().getName());
            }
        });
    }

    /**
     * 获取adVideoMap中第一个尚未下载到本地的Advs_Video的bean类
     * @param adVideoMap 从配置文件上获取的Advs_Video的全集
     * @return
     */
    public static Advs_Video getFirstRemoteVideo(Map<String, List<Advs_Video>> adVideoMap) {
        for (List<Advs_Video> adList: adVideoMap.values()) {
            if (null != adList && 0 != adList.size()) {
                for (Advs_Video ad : adList) {
                    if (null != ad && !ad.isLocal()) {
                        return ad;
                    }
                }
            }
        }
        return null;
    }
}
