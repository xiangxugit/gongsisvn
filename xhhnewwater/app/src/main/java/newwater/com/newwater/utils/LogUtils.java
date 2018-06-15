package newwater.com.newwater.utils;

import android.util.Log;

/**
 * LogUtils 日志工具类
 */

public class LogUtils {

    /**
     * Log.d
     *
     * @param isSavaLog 是否保存日志
     * @param tag       标签
     * @param msg       消息
     */
    public static void d(String isSavaLog, String tag, String msg) {
        Log.d(tag, msg);
    }
}
