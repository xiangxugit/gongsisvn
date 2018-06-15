package newwater.com.newwater.processpreserve;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class Service1 extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        //DO something

        //https://www.jianshu.com/p/7d3ff0a11ab8  数据库操作  https://blog.csdn.net/imxiangzi/article/details/76039978

        //闹钟定时播放视频
        // TODO 此处应该是读取本地的配置，本地的配置应该是在服务器获取的
        //开机器隔一段时间去监听查询
        Time t=new Time();
        t.setToNow();
        int hout = t.hour;
        int minute = t.minute;
        int second = t.second;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hout); //凌晨1点
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        Date date=calendar.getTime(); //第一次执行定时任务的时间
        Timer timer = new Timer();
        final MyHandler myHandler = new MyHandler();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.e("gogogogo","gogogogogog");
                Message msg = new Message();
                msg.what =0;
                myHandler.sendMessage(msg);
            }
        };

        final long PERIOD_DAY =10 * 1000;
        timer.schedule(task,date,PERIOD_DAY);

        //如果是空闲开始下载




    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i("Kathy", "onBind - Thread ID = " + Thread.currentThread().getId());
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Kathy", "onStartCommand - startId = " + startId + ", Thread ID = " + Thread.currentThread().getId());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.i("Kathy", "onDestroy - Thread ID = " + Thread.currentThread().getId());
        super.onDestroy();
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case 0:
                    Toast.makeText(Service1.this,"定时检测水质",Toast.LENGTH_SHORT).show();

                    //水质等的监控

                    //存入到数据库

                    break;
            }

        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        new PollingThread().start();
    }


    class PollingThread extends Thread {
        @Override
        public void run() {
            System.out.println("Polling...");
        }
    }

}
