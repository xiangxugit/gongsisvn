package purewater.com.leadapp.utils;

import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.util.Map;
import org.xutils.x;

/**
 * Created by Administrator on 2018/5/14 0014.
 */

public class RestUtils {

//    public static String IP = "192.168.0.200";

    public static String IP = "192.168.0.50";
    public static String PORT = "28301";

    //获取Token
    public final static String GETTOKEN = "api/v1/token";
    //获取最新的apk
    public final static String NEWAPK = "api/v1/apk/new";
    //获取设备信息
    public final static String GET = "/api/v1/device/get";

    public final static String DOWNLOAD = "anjian.apk";

    public static String getPath()
    {
         return "http://" + IP + ":" + PORT + "/";
    }

    public static String getUrl(String url)
    {
        return getPath() + url;
    }

}
