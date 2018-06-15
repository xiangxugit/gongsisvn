package newwater.com.newwater.utils;

import android.content.Context;
import android.text.format.Time;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import newwater.com.newwater.App;
import newwater.com.newwater.DataBaseUtils.XutilsInit;
import newwater.com.newwater.MainActivity;
import newwater.com.newwater.beans.DeiviceParams;
import newwater.com.newwater.beans.SysDeviceWaterQualityAO;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import okhttp3.Request;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class UploadLocalData {
    private static Context context;
    private static String url;
    private static List<?> content= new ArrayList<>();
    private static String tablename;
    private static long cycle;
    private static String operateflag;
    private static DbManager dbManager;
    private MainActivity.MyHandler myHandler;
    public static TimerTask task;

    /**
     *
     * @param context
     * @param url
     * @param content
     * @param tablename
     * @param cycle
     * 循环存入数据库和上报
     */
    public  UploadLocalData(Context context, String url, List<?> content, String tablename, String cycle){
        this.context = context;
        this.url = url;
        this.content = content;
        this.tablename = tablename;
        if(null==dbManager){
        dbManager = new XutilsInit(context).getDb();
        }
        upload();

    }

    public  static void uploaddata(){
        String contentJSON = JSON.toJSONString(content);
        OkHttpUtils.postAsyn(url, new OkHttpUtils.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(context,"同步失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(String response) {
//
                deleteLocalUploadData(content);

            }
        },contentJSON);
    }



    public static void upload(){
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
            timer.schedule(task,updatetime,cycle);
        }






public static void deleteLocalUploadData(List<?> content){
    try {
        dbManager.delete(content);
    } catch (DbException e) {
        e.printStackTrace();
    }
}
















}
