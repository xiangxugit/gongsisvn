package newwater.com.newwater;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.danikula.videocache.HttpProxyCacheServer;
import com.marswin89.marsdaemon.DaemonApplication;
import com.marswin89.marsdaemon.DaemonConfigurations;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.io.File;

import newwater.com.newwater.Processpreserving.Receiver1;
import newwater.com.newwater.Processpreserving.Receiver2;
import newwater.com.newwater.Processpreserving.Service1;
import newwater.com.newwater.Processpreserving.Service2;
import newwater.com.newwater.utils.CrashRestart;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

public class App extends DaemonApplication {

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

        //初始化xutils
        //
        x.Ext.init(this);
        x.Ext.setDebug(false);

        //配置本地数据库
        /**
         * 初始化DaoConfig配置
         */

               //app挂掉重启
//               CrashRestart.getInstance().initCrashRestart(this);

               //进程保活

    }

//    public static  DbManager getdb(){
//        return db;
//    }


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

