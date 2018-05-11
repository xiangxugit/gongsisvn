package newwater.com.newwater.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import newwater.com.newwater.App;
import newwater.com.newwater.beans.DeiviceParams;

/**
 * Created by Administrator on 2018/4/28 0028.
 */

public  class TimeRun {
    public static TimerTask task;

    /**
     *
     * @param context
     * @param handler
     * @param loopjiange
     * @param what
     */
    public TimeRun(Context context, Date time, final Handler handler,final long loopjiange,final int what){
        task = new TimerTask() {
            @Override
            public void run() {
//                Message msg = new Message();
//                msg.what = what;
//                handler.sendMessage(msg);


                //进行数据库上传

                List<DeiviceParams> list = null;
                try {

                    list = App.db.findAll(DeiviceParams.class);
                    if(null==list){

                    }else{
                        Log.e("---",""+ list.toString());
                    }
                } catch (DbException e) {
                    e.printStackTrace();
                }


//                UploadLocalData uploadLocalData = new UploadLocalData();
//                uploadLocalData.upload("",list.toString());





            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task,time,loopjiange);
    }


    public void cancelTimer(){
        this.task.cancel();
    }

    //TODO 封装一个获取date的方法
    public static  Date tasktime(int hour,int minute,int second){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour); //凌晨1点
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        Date date=calendar.getTime(); //第一次执行定时任务的时间
        return date;
    }



}
