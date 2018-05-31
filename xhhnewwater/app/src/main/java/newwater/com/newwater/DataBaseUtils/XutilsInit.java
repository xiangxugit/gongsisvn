package newwater.com.newwater.DataBaseUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2018/5/31 0031.
 */

public class XutilsInit {
    public static DbManager db;
    private Context context;
    public XutilsInit(Context context){
        this.context = context;
    }

    public DbManager getDb(){
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                //设置数据库名，默认xutils.db
                .setDbName("xinhonghai.db")
                //设置数据库路径，默认存储在app的私有目录
                .setDbDir(new File("/mnt/sdcard/"))
                //设置数据库的版本号
                .setDbVersion(2)
                //设置数据库打开的监听
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        //开启数据库支持多线程操作，提升性能，对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                //设置数据库更新的监听
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                    }
                })
                //设置表创建的监听
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table){
                        Log.i("JAVA", "onTableCreated：" + table.getName());
                    }
                });

                db = x.getDb(daoConfig);
                return db;
    }


}
