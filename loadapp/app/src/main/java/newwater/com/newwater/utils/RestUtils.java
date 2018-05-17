package purewater.com.leadapp.utils;

/**
 * Created by Administrator on 2018/5/14 0014.
 */

public class RestUtils {

    public static String IP = "10.167.240.112";

    public static String PORT = "9000";

    public final static String LOGIN = "huapage/mobi/app/login/user";

    public static String getPath()
    {
        // return "http //" + IP + ":" + PORT + "/";
        return "http://" + IP + "/";
    }

    public static String getUrl(String url)
    {
        return getPath() + url;
    }





}
