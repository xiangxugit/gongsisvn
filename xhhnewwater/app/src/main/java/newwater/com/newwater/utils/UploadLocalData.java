package newwater.com.newwater.utils;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import newwater.com.newwater.beans.SysDeviceNoticeAO;
import newwater.com.newwater.beans.SysDeviceWaterQualityAO;
import newwater.com.newwater.view.activity.MainActivity;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class UploadLocalData {
    private static final String TAG = "UploadLocalData";
    private static UploadLocalData instance;
    private Context context;
    private String url;
//    private List<? extends Object> contentList;
    List<? extends Object> contentList;
    private String tablename;
    private long cycle;
    private int operateflag;
    private DbManager dbManager;
    private MainActivity.MyHandler myHandler;
    public TimerTask task;
    public String postdata;
//    private String operateflag;




//    public static UploadLocalData getInstance(Context context, String url, List<? extends Object> contentList, long cycle) {
public static UploadLocalData getInstance(Context context, String url, int operateFlag, long cycle) {

        if (instance == null) {
            synchronized (UploadLocalData.class) {
                if (instance == null) {
                    instance = new UploadLocalData(context, url, operateFlag, cycle);
                }
            }
        }
        return instance;
    }

    /**
     * @param context
     * @param url
     * @param cycle     循环存入数据库和上报
     */
    private UploadLocalData(Context context, String url, int operateflag, long cycle) {
        this.context = context;
        this.url = url;
        this.operateflag = operateflag;
        this.cycle = cycle;
        if (null == dbManager) {
            dbManager = new XutilsInit(context).getDb();
        }
//        upload();
    }

    private void uploaddata() {
//        String contentJSON = JSON.toJSONString(uploaddata);

//        判断是哪一种的东西
        String url = this.url;

        if(this.operateflag==1){
            //水质上报
            try {
               this.contentList = dbManager.findAll(SysDeviceWaterQualityAO.class);
                this.postdata = JSON.toJSONString(contentList);
//                List<SysDeviceWaterQualityAO> listQualityAO = dbManager.findAll(SysDeviceWaterQualityAO.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }


        if(this.operateflag==3){
            //水质上报
            try {
                this.contentList = dbManager.findAll(SysDeviceNoticeAO.class);
                this.postdata = JSON.toJSONString(contentList);
//                List<SysDeviceWaterQualityAO> listQualityAO = dbManager.findAll(SysDeviceWaterQualityAO.class);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }



        OkHttpUtils.postAsyn(url, new OkHttpUtils.StringCallback() {
            @Override
            public void onFailure(int errCode, Request request, IOException e) {
//                Toast.makeText(context, "同步失败, errCode = " + errCode, Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: " + "同步失败, errCode = " + errCode);
            }

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onFailure: " + "同步成功, errCode = " + response);
                deleteLocalUploadData(contentList);
            }
        }, this.postdata);
    }


    public void upload() {
        Time sCodeTime = new Time();
        sCodeTime.setToNow();
        int hourscode = sCodeTime.hour;
        int minutescode = sCodeTime.minute;
        int secondcode = sCodeTime.second;
        Date updatetime = TimeRun.tasktime(hourscode, minutescode, secondcode);
        task = new TimerTask() {
            @Override
            public void run() {
                uploaddata();
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task, updatetime, cycle);
    }


    private void deleteLocalUploadData(List<? extends Object> content) {
        try {
            dbManager.delete(content);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}
