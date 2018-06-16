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
    private List<? extends Object> contentList;
    private String tablename;
    private long cycle;
    private String operateflag;
    private DbManager dbManager;
    private MainActivity.MyHandler myHandler;
    public TimerTask task;


    public static UploadLocalData getInstance(Context context, String url, List<? extends Object> contentList, long cycle) {
        if (instance == null) {
            synchronized (UploadLocalData.class) {
                if (instance == null) {
                    instance = new UploadLocalData(context, url, contentList, cycle);
                }
            }
        }
        return instance;
    }

    /**
     * @param context
     * @param url
     * @param contentList
     * @param cycle     循环存入数据库和上报
     */
    private UploadLocalData(Context context, String url, List<? extends Object> contentList, long cycle) {
        this.context = context;
        this.url = url;
        this.contentList = contentList;
        this.cycle = cycle;
        if (null == dbManager) {
            dbManager = new XutilsInit(context).getDb();
        }
//        upload();
    }

    private void uploaddata() {
        String contentJSON = JSON.toJSONString(contentList);
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
        }, contentJSON);
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
