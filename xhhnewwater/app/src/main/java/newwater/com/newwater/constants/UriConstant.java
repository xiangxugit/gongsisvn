package newwater.com.newwater.constants;

public class UriConstant {

    public static String IP = "192.168.0.200";

    public static String PORT = "28301";

    public final static String LOGIN = "huapage/mobi/app/login/user";

    //需要loginName loginPassword
    public final static String GETTOKEN = "api/v1/token";
    //获取最新的apk
    public final static String NEWAPK = "api/v1/apk/new";

    //水质信息采集
    public final static String WATERQUALITY = "api/v1/device/data/waterQuality";
    // 根目录
    public static final String APP_ROOT_PATH = "/mnt/sdcard/xhh/";

    // 视频文件夹
    public static final String VIDEO_DIR = "video/";

    // 初始视频路径（不含前面公共部分）
    public static final String INIT_VIDEO_PATH = "";

    // database文件夹
    public static final String DB_DIR = "db/";

    // 分隔符
    public static final String FILE_SEPARATE = "/";
}
