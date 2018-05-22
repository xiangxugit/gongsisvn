package newwater.com.newwater.Processpreserving;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.serialport.ComUtil;
import android.serialport.DevUtil;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import com.coolerfall.daemon.Daemon;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/5/17 0017.
 */

public class DaemonService extends Service {
    private final static int GRAY_SERVICE_ID = -1001;

    private String DevPath = "/dev/ttyS4";//默认串口
    private final int Baudrate = 115200;//默认波特率
    private final int LoopIdle = 50;//线程空闲时间ms

    private DevUtil devUtil=null;
    public static ComThread comThread;


    @Override
    public void onCreate() {
        super.onCreate();
        Daemon.run(this, DaemonService.class, Daemon.INTERVAL_ONE_MINUTE * 2);
        startTimeTask();
        grayGuard();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
		/* do something here */
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
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, hout); //凌晨1点
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, second);
//        Date date=calendar.getTime(); //第一次执行定时任务的时间
//        Timer timer = new Timer();
//        final DaemonService.MyHandler myHandler = new DaemonService.MyHandler();
//
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                Log.e("gogogogo","gogogogogog");
//                Message msg = new Message();
//                msg.what =0;
//                myHandler.sendMessage(msg);
//            }
//        };
//
//        final long PERIOD_DAY =10 * 1000;
//        timer.schedule(task,date,PERIOD_DAY);
//        flags = START_STICKY;
//          return super.onStartCommand(intent, flags, startId);

        File dev = new File(DevPath);
        devUtil = new DevUtil(null);
        boolean r = devUtil.openCOM(dev, Baudrate, 0);
        if(r) {
            if (comThread == null) {
                comThread = new ComThread();
                comThread.start();

            }
        }
          return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, DaemonService.class));
    }



    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case 0:
                    Toast.makeText(DaemonService.this,"定时检测水质",Toast.LENGTH_SHORT).show();

                    //水质等的监控

                    //存入到数据库

                    break;
            }

        }
    }


    private void startTimeTask() {

    }


    public static class DaemonInnerService extends Service {

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }


    private void grayGuard() {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, DaemonInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }



        //发送唤醒广播来促使挂掉的UI进程重新启动起来
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent alarmIntent = new Intent();
//        alarmIntent.setAction(WakeReceiver.GRAY_WAKE_ACTION);
//        PendingIntent operation = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            alarmManager.setWindow(AlarmManager.RTC_WAKEUP,
//                    System.currentTimeMillis(), ALARM_INTERVAL, operation);
//        }else {
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                    System.currentTimeMillis(), ALARM_INTERVAL, operation);
//        }
    }




}
