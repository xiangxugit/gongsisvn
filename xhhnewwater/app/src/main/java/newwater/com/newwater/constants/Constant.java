package newwater.com.newwater.constants;

public class Constant {
    public static final int LOAD_APP_SEND_FLAG = 101;
    public static final String DB_NAME = "xinhonghai.db";
    public static final int DOWNLOAD_MAX_RETRY_TIME = 3;  //
    public static final int IDLE_TIME_RECHECK_CIRCLE = 10;  // 空闲时检查当前时段是否有应播视频的周期（分钟）
    public static final int MOT_CFG_PP_FLOW = 5000;//pp棉制水预警值
    public static final int MOT_CFG_GRAIN_CARBON_FLOW=15000;//颗粒活性炭值水预警值
    public static final int MOT_CFG_PRESS_CARBON_FLOW = 15000;//压缩活性炭预警值
    public static final int MOT_CFG_POSE_CARBON_FLOW = 15000;//后置活性炭预警值
    public static final int MOT_CFG_RO_FLOW = 15000;//反渗透模预警值
    public static final double MOT_CFG_MAX_FLOW = 0.5;//单次取水量最大值
    public static final long UPLOAD_TIME = 60*1000;//没一分钟上报一次 水质
    public static final double TDSERROR = 30;//出水的水质比这个更低的话就是水质有问题
    public static final int ALL_DOWN_WAIT_TIME = 3000; // 全部下载完毕后等多久在开始刷新及播放

    public static final int RECEIVE_PUSH_VIDEO_STRATEGY_WAIT_TIME = 1 * 60; // 收到推送策略后等多久再处理（秒）

    public static final int UPDATE_SCODE = 60*1000*60*24;//一天一更新

    public static final int IS_DOWNING_WAIT_TIME = 10; // 要下载视频时如果正在下载，等待多久再发请求（秒）
    public static final String VIDEO_PUSH_HANDLE_DOING = "1";  // push文件第一个字符存储的状态：正在处理
    public static final String VIDEO_PUSH_HANDLE_TO_DO = "0";  // push文件第一个字符存储的状态：尚未处理

    /*各种等待时间*/
    public static final int DEFAULT_FREE_AD_DURATION = 30;  // 默认一次免费喝水广告的播放时长（秒）
    public static final int FAST_CLICK_DELAY_TIME = 500;  // 这个时间以内的认为是重复点击（毫秒）
    public static final int QUIT_DRINK_INTERFACE_WAIT_TIME = 10;  // 喝水的时候，多少秒无操作则退出（秒）

    /*推送操作类型*/
    public static final int PUSH_OPERATION_TYPE_OPERATE = 1; // 操作
    public static final int PUSH_OPERATION_TYPE_CONFIG = 2; // 配置
    public static final int PUSH_OPERATION_TYPE_LOGIN = 3; // 登录
    public static final int PUSH_OPERATION_TYPE_UPDATE_ID = 4; // 更新信鸽ID

    /*intent和bundle的key*/
    public static final String KEY_FREE_AD_DURATION = "free_ad_duration";
    public static final String KEY_LOAD_SEND_DEVICE_ID = "load_send_device_id";
    public static final String KEY_LOAD_SEND_DRINK_MODE = "load_send_drink_mode";
    public static final String KEY_BREAK_DOWN_ERRCODE = "break_down_errcode";

    /*msg的what值*/
    public static final int MSG_NEW_AD_VIDEO_STRATEGY_PUSH = 11;
    public static final int MSG_WAITING_THEN_DOWNLOAD = 12;
    public static final int MSG_UPDATE_SCODE = 13;
    public static final int MSG_ALL_DOWN_CONPLETE = 14;
    public static final int MSG_QUIT_DRINK_INTERFACE = 15;

    /*定时任务的操作类型*/
    public static final int TIME_OPERATE_UPDATEWATER = 1;
    public static final int TIME_OPETATE_UPDATESCODE = 2;

    /*BaseSharedPreferences Key值*/
    public static final String SCODEKEY = "scode";
    public static final String DEVICE_ID_KEY = "device_id";
    public static final String DRINK_MODE_KEY = "drink_mode";

    public static final String DOWN_ERROR_MSG_WRONG_URL = "404 Not Found";

    /*广告类型*/
    public static final int AD_TYPE_IDLE = 0;
    public static final int AD_TYPE_FREE = 1;

    /*饮水模式*/
    public static final int DRINK_MODE_WATER_SALE = 1;
    public static final int DRINK_MODE_MACHINE_SALE = 2;
    public static final int DRINK_MODE_MACHINE_RENT = 3;
}
