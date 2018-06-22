package newwater.com.newwater;

import android.content.Context;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.marswin89.marsdaemon.DaemonApplication;
import com.marswin89.marsdaemon.DaemonConfigurations;

import org.xutils.x;

import newwater.com.newwater.manager.CrashHandler;
import newwater.com.newwater.processpreserve.Receiver1;
import newwater.com.newwater.processpreserve.Receiver2;
import newwater.com.newwater.processpreserve.Service1;
import newwater.com.newwater.processpreserve.Service2;
import newwater.com.newwater.utils.CrashRestart;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

public class App extends DaemonApplication {

    private static App instance;
    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("进入app的时候执行","进入app的时候执行");
        instance = this;

        // 初始化崩溃日志收集器
        CrashHandler.getInstance().init(this);

        //初始化xutils
        //
        x.Ext.init(this);
        x.Ext.setDebug(false);

        //配置本地数据库
        /**
         * 初始化DaoConfig配置
         */

               //app挂掉重启
       CrashRestart.getInstance().initCrashRestart(this);

               //进程保活

    }

//    public static  DbManager getdb(){
//        return db;
//    }

    public static Context getInstance() {
        return instance;
    }

    @Override
    protected DaemonConfigurations getDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "newwater.com.newwater.Processpreserving::process1",
                Service1.class.getCanonicalName(),
                Receiver1.class.getCanonicalName());

        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "newwater.com.newwater.Processpreserving::process2",
                Service2.class.getCanonicalName(),
                Receiver2.class.getCanonicalName());

        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        //return new DaemonConfigurations(configuration1, configuration2);//listener can be null
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }



    class MyDaemonListener implements DaemonConfigurations.DaemonListener{
        @Override
        public void onPersistentStart(Context context) {
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
        }

        @Override
        public void onWatchDaemonDaed() {
        }
    }
}

