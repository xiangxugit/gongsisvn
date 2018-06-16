package newwater.com.newwater.constants;

public class Constant {
    public static final int DOWNLOADAPK_ID = 10;
    public static final String DB_NAME = "xinhonghai.db";
    public static final int DOWNLOAD_MAX_RETRY_TIME = 3;  //
    public static final String DOWN_ERROR_MSG_WRONG_URL = "404 Not Found";
    public static final String DOWN_ERROR_MSG_WRONG_BASE_URL = "Incorrect BaseUrl";

    public static final String VIDEO_PUSH_HANDLE_DOING = "1";  // push文件第一个字符存储的状态：正在处理
    public static final String VIDEO_PUSH_HANDLE_TO_DO = "0";  // push文件第一个字符存储的状态：尚未处理

    //是否是测试模式
    public static final boolean TEST = true;
    /*各种等待时间*/
    public static final int FAST_CLICK_DELAY_TIME = 500;  // 这个时间以内的认为是重复点击（毫秒）
    public static final int POOL_TIME = 800;// 轮询get_ioRunData()时间间隔ms（付工那边的代码start）
    public static final int RECEIVE_PUSH_VIDEO_STRATEGY_WAIT_TIME = 1 * 60; // 收到推送策略后等多久再处理（秒）
    public static final int IS_DOWNING_WAIT_TIME = 10; // 要下载视频时如果正在下载，等待多久再发请求（秒）
    public static final int ALL_DOWN_WAIT_TIME = 3000; // 全部下载完毕后等多久在开始刷新及播放（毫秒）
    public static final int DEFAULT_FREE_AD_DURATION = 10;  // 默认一次免费喝水广告的播放时长（秒）
    public static final int QUIT_DRINK_INTERFACE_WAIT_TIME = 60 * 60;  // 喝水的时候，多少秒无操作则退出（秒）
    public static final int UPDATE_SCODE = 60 * 1000 * 60 * 24;// 一天一更新
    public static final long UPLOAD_TIME = 60 * 1000;// 没一分钟上报一次 水质
    public static final long TEMPSCODE_TIME= 24 * 60 * 60 * 1000;
    public static final long AD_RECORD_UPLOAD_PERIOD = 24 * 60 * 60;// 上报一次视频播放记录的周期（秒）
    public static final long CHECK_RENT_DEADLINE_PERIOD = 24 * 60 * 60;// 检查一次租期是否到期的周期（秒）

    /*水的各种标准*/
    public static final int MOT_CFG_PP_FLOW = 5000;//pp棉制水预警值 90%  根据制水量 设备激活以后才出来
    public static final int MOT_CFG_GRAIN_CARBON_FLOW = 15000;//颗粒活性炭值水预警值 90%
    public static final int MOT_CFG_PRESS_CARBON_FLOW = 15000;//压缩活性炭预警值 90%
    public static final int MOT_CFG_POSE_CARBON_FLOW = 15000;//后置活性炭预警值 90%
    public static final int MOT_CFG_RO_FLOW = 15000;//反渗透模预警值 90%
    public static final double MOT_CFG_MAX_FLOW = 0.5;//单次取水量最大值
    public static final double TDSERROR = 200;//出水的水质比这个更低的话就是水质有问题

    /*推送操作类型*/
    public static final int PUSH_OPERATION_TYPE_OPERATE = 1; // 操作
    public static final int PUSH_OPERATION_TYPE_CONFIG = 2; // 配置
    public static final int PUSH_OPERATION_TYPE_LOGIN = 3; // 登录
    public static final int PUSH_OPERATION_TYPE_UPDATE_ID = 4; // 更新信鸽ID

    /*预警故障类别*/
    public static final int NOTICE_TYPE_NO_NETWORK = 1;  // 断网
    public static final int NOTICE_TYPE_LESS_FILTER = 2;  // 滤芯不足
    public static final int NOTICE_TYPE_NO_FILTER = 3;  // 滤芯用完
    public static final int NOTICE_TYPE_WATER_QUALITY_UNUSUAL = 4;  // 水质异常
    public static final int NOTICE_TYPE_LESS_CUP = 5;  // 水杯不足
    public static final int NOTICE_TYPE_NO_CUP = 6;  // 水杯用完
    public static final int NOTICE_TYPE_WATER_CONSUMPTION_UNUSUAL = 7; // 出水量异常
    public static final int NOTICE_TYPE_ELEC_LEAK = 8;  // 漏电
    public static final int NOTICE_TYPE_WATER_LEAK = 9;  // 漏水
    public static final int NOTICE_TYPE_ORIGIN_WATER_LACK = 10;  // 原水缺水
    public static final int NOTICE_TYPE_AD_URL_WRONG = 11;  // 广告视频下载地址错误

    /*预警级别*/
    public static final int NOTICE_LEVEL_ABNORMAL = 0;
    public static final int NOTICE_LEVEL_BREAK_DOWN = 1;

    /*intent和bundle的key*/
    public static final String KEY_FREE_AD_DURATION = "free_ad_duration";
//    public static final String KEY_LOAD_SEND_DEVICE_ID = "load_send_device_id";
//    public static final String KEY_LOAD_SEND_DRINK_MODE = "load_send_drink_mode";
//    public static final String KEY_LOAD_SEND_RENT_DEADLINE = "load_send_rent_deadline";
//    public static final String KEY_LOAD_SEND_CONTRACT_INFO = "load_send_contract_info";
    public static final String KEY_BREAK_DOWN_ERR_REASON = "break_down_errcode";

    /*msg的what值*/
    public static final int MSG_NEW_AD_VIDEO_STRATEGY_PUSH = 11;
    public static final int MSG_WAITING_THEN_DOWNLOAD = 12;
    public static final int MSG_UPDATE_SCODE = 13;
    public static final int MSG_ALL_DOWN_COMPLETE = 14;
    public static final int MSG_QUIT_DRINK_INTERFACE = 15;

    /*定时任务的操作类型*/
    public static final int TIME_OPERATE_UPDATEWATER = 1;
    public static final int TIME_OPETATE_UPDATESCODE = 2;
    public static final int TIME_OPETATE_WARNING = 3;

    /*BaseSharedPreferences的Key值*/
    public static final String SCODEKEY = "scode";
    public static final String DEVICE_CONFIG_STRING_KEY = "device_config_string";
    public static final String DEVICE_ID_KEY = "device_id";
    public static final String DRINK_MODE_KEY = "drink_mode";
    public static final String RENT_DEADLINE_KEY = "rent_deadline";
    public static final String CONTRACT_INFO_KEY = "contract_info";
    public static final String DEVICE_PP_FLOW_KEY = "device_pp_flow";
    public static final String DEVICE_GRAIN_CARBON_KEY = "device_grain_carbon";
    public static final String DEVICE_PRESS_CARBON_KEY = "device_press_carbon";
    public static final String DEVICE_POSE_CARBON_KEY = "device_pose_carbon";
    public static final String DEVICE_RO_FLOW_KEY = "device_ro_flow";
    public static final String DEVICE_UP_TIME_KEY = "device_up_time";
    public static final String DEVICE_FLUSH_INTERVAL_KEY = "device_flush_interval";
    public static final String DEVICE_FLUSH_DURATION_KEY = "device_flush_duration";
    public static final String DEVICE_HEATING_TEMP_KEY = "device_heating_temp";
    public static final String DEVICE_COOLING_TEMP_KEY = "device_cooling_temp";
    public static final String DEVICE_HEATING_ALL_DAY_KEY = "device_heating_all_day";
    public static final String DEVICE_COOLING_ALL_DAY_KEY = "device_cooling_all_day";
    public static final String DEVICE_HEATING_INTERVAL_KEY = "device_heating_interval";
    public static final String DEVICE_COOLING_INTERVAL_KEY = "device_cooling_interval";
    public static final String DEVICE_MAX_FLOW_KEY = "device_max_flow";
    public static final String DEVICE_TDS_THRESHOLD_KEY = "device_tds_threshold";

    /*广告类型*/
    public static final int AD_TYPE_IDLE = 0;
    public static final int AD_TYPE_FREE = 1;
    public static final int AD_TYPE_INIT = 3;

    /*饮水模式*/
    public static final int DRINK_MODE_WATER_SALE = 1;
    public static final int DRINK_MODE_MACHINE_SALE = 2;
    public static final int DRINK_MODE_MACHINE_RENT = 3;

    /*设备参数的默认值*/
    public static final int DRINK_MODE_DEFAULT = DRINK_MODE_WATER_SALE;
    public static final String RENT_DEADLINE_DEFAULT = "2099-12-31 23:59:59";
    public static final String CONTRACT_INFO_DEFAULT = "维护人员";
    public static final int DEVICE_PP_FLOW_DEFAULT = 5000;
    public static final int DEVICE_GRAIN_CARBON_DEFAULT = 15000;
    public static final int DEVICE_PRESS_CARBON_DEFAULT = 15000;
    public static final int DEVICE_POSE_CARBON_DEFAULT = 15000;
    public static final int DEVICE_RO_FLOW_DEFAULT = 5000;
    public static final int DEVICE_UP_TIME_DEFAULT = 5;
    public static final int DEVICE_FLUSH_INTERVAL_DEFAULT = 5;
    public static final int DEVICE_FLUSH_DURATION_DEFAULT = 10;
    public static final int DEVICE_HEATING_TEMP_DEFAULT = 75;
    public static final int DEVICE_COOLING_TEMP_DEFAULT = 8;
    public static final int DEVICE_HEATING_ALL_DAY_DEFAULT = 1;
    public static final int DEVICE_COOLING_ALL_DAY_DEFAULT = 1;
    public static final String DEVICE_HEATING_INTERVAL_DEFAULT = "DEVICE_HEATING_INTERVAL_DEFAULT";
    public static final String DEVICE_COOLING_INTERVAL_DEFAULT = "DEVICE_COOLING_INTERVAL_DEFAULT";
    public static final int DEVICE_MAX_FLOW_DEFAULT = 500;
    public static final int DEVICE_TDS_THRESHOLD_DEFAULT = 200;

//    百分比
    public static final double PERCENT = 0.9;
}
