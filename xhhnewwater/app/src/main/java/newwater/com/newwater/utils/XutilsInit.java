package newwater.com.newwater.utils;

import android.content.Context;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

import java.io.File;

import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.utils.RestUtils;

/**
 * Created by Administrator on 2018/5/31 0031.
 */

public class XutilsInit {
    private static final String TAG = "XutilsInit";
    
    public static DbManager db;
    private Context context;
    public XutilsInit(Context context){
        this.context = context;
    }

    public DbManager getDb(){
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                //设置数据库名，默认xutils.db
                .setDbName(Constant.DB_NAME)
                //设置数据库路径，默认存储在app的私有目录
                .setDbDir(new File(RestUtils.getDbDir()))
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
                        LogUtils.i(TAG, "onTableCreated：" + table.getName());
                    }
                });

                db = x.getDb(daoConfig);
                return db;
    }


}
