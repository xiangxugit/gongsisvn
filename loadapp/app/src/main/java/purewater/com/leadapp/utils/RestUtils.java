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

    public static String IP = "192.168.0.200";

    public static String PORT = "28301";
    //需要loginName loginPassword
    public final static String GETTOKEN = "api/v1/token";
    //获取最新的apk
    public final static String NEWAPK = "api/v1/apk/new";
    public final static String DOWNLOAD = "test.apk";

    public static String getPath()
    {
         return "http://" + IP + ":" + PORT + "/";
//        return "http://" + IP + "/";
    }

    public static String getUrl(String url)
    {
        return getPath() + url;
    }








}
