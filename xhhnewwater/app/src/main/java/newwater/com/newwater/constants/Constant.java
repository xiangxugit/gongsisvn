package newwater.com.newwater.constants;

import newwater.com.newwater.utils.BaseSharedPreferences;

public class Constant {
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

    public static final int RECEIVE_PUSH_VIDEO_STRATEGY_WAIT_TIME = 30 * 60; // 收到推送策略后等多久再处理（秒）

    public static final int UPDATE_SCODE = 60*1000*60*24;//一天一更新

    public static final int IS_DOWNING_WAIT_TIME = 10; // 要下载视频时如果正在下载，等待多久再发请求（秒）
    public static final int DEFAULT_FREE_AD_DURATION = 30;  // 默认一次免费喝水广告的播放时长
    public static final int FAST_CLICK_DELAY_TIME = 500;  // 这个时间以内的认为是重复点击（毫秒）


    /*intent和bundle的key*/
    public static final String FREE_AD_DURATION = "free_ad_duration";

    /*msg的what值*/
    public static final int MSG_NEW_AD_VIDEO_STRATEGY_PUSH = 11;
    public static final int NSG_IS_DOWNLOADING_WAITING = 12;
    public static final int MSG_UPDATE_SCODE = 13;


    /*定时任务的操作类型*/
    public static final int TIME_OPERATE_UPDATEWATER = 1;
    public static final int TIME_OPETATE_UPDATESCODE = 2;

    /*BaseSharedPreferences Key值*/
    public static final String SCODEKEY = "scode";

    public static final String DOWN_ERROR_EXCEPTION_WRONG_URL = "404 Not Found";

}
