package newwater.com.newwater.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import newwater.com.newwater.App;
import newwater.com.newwater.constants.UriConstant;

/**
 * LogUtils 日志工具类
 */
@SuppressLint("SimpleDateFormat")
public class LogUtils {

    // 是用来控制，是否打印日志
    private static final boolean isDeBug = true;
    // 存放日志文件的路径
    private static final String DIR_PATH = UriConstant.APP_ROOT_PATH + UriConstant.LOG_DIR;
    // debug 版本存放日志文件的最大个数
    private static final int LIMIT_LOG_COUNT_DEBUG = 50;
    // debug 版本存放日志文件的文件大小（单位：K）
    private static final int LIMIT_LOG_SIZE_DEBUG = 10 * 1024;
    // release 版本存放日志文件的最大个数
    private static final int LIMIT_LOG_COUNT_RELEASE = 5;
    // release 版本存放日志文件的文件大小（单位：K）
    private static final int LIMIT_LOG_SIZE_RELEASE = 1 * 1024;

    private static int limitLogCount;
    private static int limitLogSize;

    public static void v(String tag, String msg) {
        v(true, tag, msg);
    }

    public static void d(String tag, String msg) {
        d(true, tag, msg);
    }

    public static void i(String tag, String msg) {
        i(true, tag, msg);
    }

    public static void w(String tag, String msg) {
        w(true, tag, msg);
    }

    public static void e(String tag, String msg) {
        e(true, tag, msg);
    }

    /**
     * verbose等级的日志输出
     *
     * @param tag 日志标识
     * @param msg 要输出的内容
     * @return void 返回类型
     * @throws
     */
    public static void v(boolean isWrite, String tag, String msg) {
        // 是否开启日志输出
        if (isDeBug) {
            Log.v(tag, msg);
        }
        // 是否将日志写入文件
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * debug等级的日志输出
     *
     * @param tag 标识
     * @param msg 内容
     * @return void 返回类型
     * @throws
     */
    public static void d(boolean isWrite, String tag, String msg) {
        if (isDeBug) {
            Log.d(tag, msg);
        }
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * info等级的日志输出
     *
     * @param tag 标识
     * @param msg 内容
     * @return void 返回类型
     * @throws
     */
    public static void i(boolean isWrite, String tag, String msg) {
        if (isDeBug) {
            Log.i(tag, msg);
        }
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * warn等级的日志输出
     *
     * @param tag 标识
     * @param msg 内容
     * @return void 返回类型
     * @throws
     */
    public static void w(boolean isWrite, String tag, String msg) {
        if (isDeBug) {
            Log.w(tag, msg);
        }
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * error等级的日志输出
     *
     * @param tag 标识
     * @param msg 内容
     * @return void 返回类型
     */
    public static void e(boolean isWrite, String tag, String msg) {
        if (isDeBug) {
            Log.w(tag, msg);
        }
        if (isWrite) {
            write(tag, msg);
        }
    }

    /**
     * 用于把日志内容写入制定的文件
     * @param @param tag 标识
     * @param @param msg 要打印的内容
     * @return void
     * @throws
     */
    private static void write(String tag, String msg) {
        if (App.isAppDbg()) {
            limitLogCount = LIMIT_LOG_COUNT_DEBUG;
            limitLogSize =LIMIT_LOG_SIZE_DEBUG;
        } else {
            limitLogCount = LIMIT_LOG_COUNT_RELEASE;
            limitLogSize = LIMIT_LOG_SIZE_RELEASE;
        }
        String fileName = "0.log";

        write(fileName, tag, msg);
        FileUtil.limitAppLogCount(DIR_PATH, limitLogCount);
    }

    /**
     * 用于把日志内容写入制定的文件
     * @param @param tag 标识
     * @param @param msg 要打印的内容
     * @return void
     * @throws
     */
    private static void write(String fileName, String tag, String msg) {
        String path = FileUtil.createMkdirsAndFiles(DIR_PATH, fileName);
        if (TextUtils.isEmpty(path)) {
            return;
        }
        String currentTime = new SimpleDateFormat("MM-dd HH:mm:ss.SSS").format(new Date());
        String log = currentTime + " " + tag + " " + msg;
        FileUtil.write2File(path, log, true);
    }

}
