package newwater.com.newwater.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import newwater.com.newwater.App;
import newwater.com.newwater.MainActivity;

/**
 * Created by Administrator on 2018/5/12 0012.
 */

public class CrashRestart implements  Thread.UncaughtExceptionHandler {

    public static CrashRestart  crasRestart;
    private Thread.UncaughtExceptionHandler appDefaultHadler;
    private App mAppContext;

    public void initCrashRestart(App mAppContext){
        this.mAppContext = mAppContext;
        appDefaultHadler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);

    }

    public static  CrashRestart getInstance(){
        if (crasRestart == null) {
            crasRestart = new CrashRestart();
        }
        return crasRestart;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && appDefaultHadler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            appDefaultHadler.uncaughtException(thread, ex);
        } else {
            AlarmManager mgr = (AlarmManager) mAppContext.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(mAppContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("crash", true);
            PendingIntent restartIntent = PendingIntent.getActivity(mAppContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            System.gc();
        }
    }


    /**
     * 错误处理,收集错误信息 发送错误报告等操作均在此完成.
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 自定义处理错误信息

        //恢复数据

        
        return true;
    }
}
