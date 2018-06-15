package newwater.com.newwater.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.serialport.DevUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import newwater.com.newwater.Sys_Device_Monitor_Config_DbOperate;
import newwater.com.newwater.beans.SysDeviceMonitorConfig;
import newwater.com.newwater.utils.RestUtils;
import newwater.com.newwater.utils.UploadLocalData;
import newwater.com.newwater.utils.XutilsInit;
import newwater.com.newwater.R;
import newwater.com.newwater.TestJSON;
import newwater.com.newwater.beans.AdvsPlayRecode;
import newwater.com.newwater.processpreserve.ComThread;
import newwater.com.newwater.processpreserve.DaemonService;
import newwater.com.newwater.beans.AdvsVideo;
import newwater.com.newwater.beans.DeviceEntity;
import newwater.com.newwater.beans.DispenserCache;
import newwater.com.newwater.beans.PushEntity;
import newwater.com.newwater.broadcast.ConnectionChangeReceiver;
import newwater.com.newwater.broadcast.MessageReceiver;
import newwater.com.newwater.broadcast.UpdateBroadcast;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.interfaces.DownloadCallback;
import newwater.com.newwater.manager.DownloadManager;
import newwater.com.newwater.manager.IjkManager;
import newwater.com.newwater.utils.BaseSharedPreferences;
import newwater.com.newwater.utils.CommonUtil;
import newwater.com.newwater.utils.FileUtil;
import newwater.com.newwater.utils.ControllerUtils;
import newwater.com.newwater.utils.GetDeviceInfo;
import newwater.com.newwater.utils.TimeBack;
import newwater.com.newwater.utils.TimeRun;
import newwater.com.newwater.utils.TimeUtils;
import newwater.com.newwater.utils.VideoUtils;
import newwater.com.newwater.view.PopBuy;
import newwater.com.newwater.view.PopLeftOperate;
import newwater.com.newwater.view.PopRightOperate;
import newwater.com.newwater.view.PopWarning;
import newwater.com.newwater.view.PopWaterSale;
import newwater.com.newwater.view.PopQrCode;
import newwater.com.newwater.view.PopWantWater;

import static newwater.com.newwater.utils.PermissionUtils.permissions;

public class MainActivity extends BaseActivity implements View.OnClickListener, IjkManager.PlayerStateListener {
    private static final String TAG = "MainActivity";

    public static final String filePath = "/sdcard/xutils/xUtils_1.avi";

    //region 私有变量

    private Context mContext;
    private int deviceId;
    private int drinkMode;
    private String rentDeadline;
    private String contractInfo;
    private TextView dixieccup;//纸杯和我要饮水按钮
    private Boolean operateornot = false;
    public static LinearLayout leftoperate;//左边操作区域
    public static LinearLayout rightoperate;//右边操作区域
    private ImageView ivWantWater;

    //popwindow操作
    private View contentView;

    //出杯子的提示
    private View outCupView;
    private RelativeLayout outcupleft;
    private RelativeLayout outcupright;

    private RelativeLayout root;
    private ImageView qrcode;

    static int pos = 0;
    private final static int DATADELETE = 2;

    private PopWantWater popWantWater = null;
    private PopWaterSale popWaterSale = null;
    private PopQrCode popQrCode = null;
    private PopLeftOperate popLeft = null;
    private PopRightOperate popRight = null;
    private PopBuy popBuy = null;
    private PopWarning popWarning = null;

    //付工那边的代码start
    private final int PollTime = 800;//轮询get_ioRunData()时间间隔ms

//    private DevUtil devUtil = null;

    public static final String FLAG = "UPDATE";

    private UpdateBroadcast myBroadcast;

    //左边操作窗口的组件
    private TextView hotwatertext;
    private TextView coolwatertext;
    private TextView ppmvalue;
    private TextView ppm;//下方的

    //endregion

    //region 四个使能按钮
    private LinearLayout tobehot;
    private LinearLayout tobecool;
    private LinearLayout zhishui;
    private LinearLayout chongxi;
    private Button exit;

    private ImageView hotico;//是否加热的imageview
    private TextView hotornot;//是否加热text

    private ImageView coolico;//是否制冷的imageView
    private TextView cooltext;//是否制冷text

    private ImageView zhishuiico;//是否制水的imageView
    private TextView zhishuitext;//是佛止水的text

    private ImageView chongxiimage;//冲洗imageView
    private TextView chongxitext;//冲洗text;
    //endregion

    //region 视频变量

    // 视频播放
    /*播放器*/
    private IjkManager playerManager;
    /*初始视频列表*/
    //private List<AdvsVideo> initAdVideoList;
    /*推送策略中的视频列表*/
    private List<AdvsVideo> pushAdVideoList;
    /*所有要播放的闲时视频列表*/
    private List<AdvsVideo> allAdVideoList; //（同步后应该与push列表一致）
    /*当前要播放的闲时视频列表*/
    private List<AdvsVideo> curAdVideoList;
    /*当前播放的视频在initAdVideoList中的index*/
    private int initAdIndex;
    /*当前播放的视频在curAdVideoList中的index*/
    private int curAdIndex;
    /*当前下载的视频在pushAdVideoList中的index*/
    private int pushAdIndex;
    /*是否正在播放视频*/
    private boolean isPlaying;
    /*是否正在下载*/
    private boolean isDownloading;
    /*当前播放的是初始视频*/
    private boolean isPlayInitVideo;
    /*正处于推送收到后的等待时间*/
    private boolean isWaitPush;
    /*当前时间*/
    private String curTime;

    //endregion

    private MyHandler myHandler;
    private DbManager dbManager;
    private ComThread comThread;//comThread服务是用来获取设备数据
    private DevUtil devUtil;//设备操作的工具类

    //region -------------------------- 生命周期 start --------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        initVideo();
        getDevice_Monitor_Config();
        registerXinGe();
        initComThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != playerManager) {
            playerManager.start();
        }
        myHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DispenserCache.isFreeAdDone) {
                    Log.d(TAG, "dismissPop: 看完广告视频，可以喝水了");
                    dismissPop(popWantWater);
                    showPopLeftOperate();
                    showPopRightOperate();
                } else {
                    Log.d(TAG, "dismissPop: 要出现了");
                    showPopWantWater();
                }
            }
        }, 100);

    }

    @Override
    protected void onPause() {
        if (null != playerManager) {
            playerManager.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO: 2018/6/12 0012 清空各种
        myHandler.removeCallbacksAndMessages(null);
        myHandler = null;
        dismissAllPop();
        super.onDestroy();
    }

    //endregion -------------------------- 生命周期 end --------------------------

    private void initData() {
        mContext = MainActivity.this;
        myHandler = new MyHandler();
        // 获取设备信息
        getDeviceInfo();
        // 数据库
        dbManager = new XutilsInit(MainActivity.this).getDb();
        // 网络变化广播接收器
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        ConnectionChangeReceiver myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
        // 视频播放
        DispenserCache.freeAdVideoList = new ArrayList<>();
        DispenserCache.initAdVideoList = new ArrayList<>();
        pushAdVideoList = new ArrayList<>();
        allAdVideoList = new ArrayList<>();
        curAdVideoList = new ArrayList<>();
        // 从本地文件中取出推送数据，看是否需要处理
        getPushStrategy();
        // 从数据库中取出数据填充列表
        try {
            List<AdvsVideo> allAds = dbManager.findAll(AdvsVideo.class);
            if (null != allAds && 0 != allAds.size()) {
                for (AdvsVideo ad : allAds) {
                    if (null == ad) continue;
                    dividerAds(allAds);
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        curTime = TimeUtils.getCurrentTime();
        curAdIndex = 0;
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        root = (RelativeLayout) findViewById(R.id.root);
        ivWantWater = findViewById(R.id.wantwater);

        // 获取当前设备是哪一种模式
        int model = TestJSON.getModel();
        if (model == 0) {
            //售水模式
        }

        //监控设备
        listenDevice();

        //监控设备
//        startService(new Intent(mContext, Service1.class));

        startService(new Intent(mContext, DaemonService.class));
//        String ACTION = "com.ryantang.service.PollingService";
//        PollingUtils.startPollingService(this, 5, Service1.class, ACTION);
        //下载视频
//        VideoUtils videodownload = new VideoUtils(mContext);
//        videodownload.downloadvideo();
        //左边的操作界面组件获得

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.left_pop, null);

        hotwatertext = (TextView) view.findViewById(R.id.hotwatertext);
        coolwatertext = (TextView) view.findViewById(R.id.coolwatertext);
        ppmvalue = (TextView) view.findViewById(R.id.ppmvalue);
        ppm = (TextView) view.findViewById(R.id.ppm);
        //监控预警配置表的操作

    }

    private void initVideo() {
        // 初始化播放器
        playerManager = new IjkManager(this, R.id.idle_ad_video);
        playerManager.setFullScreenOnly(true);
        playerManager.setScaleType(IjkManager.SCALETYPE_FILLPARENT);
        playerManager.playInFullScreen(true);
        playerManager.setOnPlayerStateChangeListener(this);
        // 获取当前时段应播视频的列表
        loopPlayVideo();
        scheduleUploadVideo();
    }

    private void getDevice_Monitor_Config() {
        Sys_Device_Monitor_Config_DbOperate sys_device_monitor_config_dbOperate = new Sys_Device_Monitor_Config_DbOperate(MainActivity.this);
        try {
            sys_device_monitor_config_dbOperate.add(null);
        } catch (DbException e) {
            e.printStackTrace();
        }

        //查询数据
//        try {
//            List warningdata = sys_device_monitor_config_dbOperate.find();
//            Log.e("waringdata",warningdata.toString());
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
    }

    private void registerXinGe() {
        DynamicReceiver myReceiver = new DynamicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MessageReceiver.PUSHACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(myReceiver, filter);
    }

    private void initComThread() {
        if (null == DaemonService.comThread) {
            comThread = new ComThread(MainActivity.this, null);
        }
        if (null == devUtil) {
            devUtil = new DevUtil(null);
        }
    }

    /**
     * 获取设备信息
     */
    private void getDeviceInfo() {
        // 从configString获取
        String configString = BaseSharedPreferences.getString(mContext, Constant.DEVICE_CONFIG_STRING_KEY);
        SysDeviceMonitorConfig config = JSONObject.parseObject(configString, SysDeviceMonitorConfig.class);
        if (null == config) {
            // TODO: 2018/6/15 0015 上报数据错误
        }
        drinkMode = config.getProductChargMode();
        BaseSharedPreferences.setInt(mContext, Constant.DRINK_MODE_KEY, 0 == drinkMode ? Constant.DRINK_MODE_WATER_SALE : drinkMode);
        rentDeadline = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(config.getProductRentTime());
        BaseSharedPreferences.setString(mContext, Constant.RENT_DEADLINE_KEY, rentDeadline);
        contractInfo = config.getAdminUserTelephone();
        BaseSharedPreferences.setString(mContext, Constant.CONTRACT_INFO_KEY, TextUtils.isEmpty(contractInfo) ? "维护人员" : contractInfo);
        // 2、intent没有则本地获取
        if (0 == deviceId) {
            if (BaseSharedPreferences.containInt(mContext, Constant.DEVICE_ID_KEY)) {
                deviceId = BaseSharedPreferences.getInt(mContext, Constant.DEVICE_ID_KEY);
            }
        }
        if (0 == drinkMode) {
            if (BaseSharedPreferences.containInt(mContext, Constant.DRINK_MODE_KEY)) {
                drinkMode = BaseSharedPreferences.getInt(mContext, Constant.DRINK_MODE_KEY);
            }
        }
        if (TextUtils.isEmpty(rentDeadline)) {
            if (BaseSharedPreferences.containInt(mContext, Constant.KEY_LOAD_SEND_RENT_DEADLINE)) {
                rentDeadline = BaseSharedPreferences.getString(mContext, Constant.KEY_LOAD_SEND_RENT_DEADLINE);
            }
        }
        if (TextUtils.isEmpty(contractInfo)) {
            if (BaseSharedPreferences.containInt(mContext, Constant.KEY_LOAD_SEND_CONTRACT_INFO)) {
                contractInfo = BaseSharedPreferences.getString(mContext, Constant.KEY_LOAD_SEND_CONTRACT_INFO);
            }
        }
        // 3、都没有则上报错误
        if (0 == deviceId) {
            // TODO: 2018/6/11 0011 上报无deviceId错误
            Log.d(TAG, "initData: 无deviceId！");
            moveToBreakDownActivity(getString(R.string.break_down_reason_no_device_id));
            return;
        }
        if (0 == drinkMode) {
            // TODO: 2018/6/11 0011 上报无drinkMode错误
            Log.d(TAG, "initData: 无drinkMode！");
            moveToBreakDownActivity(getString(R.string.break_down_reason_no_drink_mode));
            return;
        }
        if (TextUtils.isEmpty(rentDeadline)) {
            // TODO: 2018/6/11 0011 上报无rentDeadline错误
            Log.d(TAG, "initData: rentDeadline！");
            moveToBreakDownActivity(getString(R.string.break_down_reason_no_rent_deadline));
            return;
        }
        if (TextUtils.isEmpty(contractInfo)) {
            // TODO: 2018/6/11 0011 上报无contractInfo错误
            Log.d(TAG, "initData: contractInfo！");
            moveToBreakDownActivity(getString(R.string.break_down_reason_no_contract_info));
            return;
        }
    }

    /**
     * 定时上报视频播放记录
     */
    private void scheduleUploadVideo() {
        String url = RestUtils.getUrl(UriConstant.AD_VIDEO_RECORD_LIST);
        try {
            List<AdvsPlayRecode> adRecordList = dbManager.findAll(AdvsPlayRecode.class);
            if (null != adRecordList) {
                UploadLocalData.getInstance(mContext, url, adRecordList,
                        Constant.AD_RECORD_UPLOAD_PERIOD * 1000).upload();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环播放广告视频
     */
    private void loopPlayVideo() {
        Log.d(TAG, "loopPlayVideo: 开始循环播放广告视频");
        if (getCurrentVideoList()) {
            // 有视频，则顺序播放
            playVideo();
        } else {
            // 无视频，则播放初始视频
            playInitVideo();
        }
    }

    /**
     * 获取当前应该播放的闲时视频的列表
     *
     * @return 返回值为当前时段是否有videoList
     */
    private boolean getCurrentVideoList() {
        Log.d(TAG, "getCurrentVideoList: 开始获取当前应播视频列表");
        boolean hasVideo = false;
        for (AdvsVideo ad : allAdVideoList) {
            int index = VideoUtils.getAdIndexFromList(ad, curAdVideoList);
            if (-1 == index) {
                // 列表中不存在此广告，则加入此广告
                if (TimeUtils.isCurrentDateTimeInPlan(
                        ad.getAdvsPlayBeginDatetimes(), ad.getAdvsPlayEndDatetimes(),
                        ad.getAdvsPlayBeginTime(), ad.getAdvsPlayEndTime(), curTime)) {
                    curAdVideoList.add(ad);
                    hasVideo = true;
                }
            } else if (-1 < index) {
                // 列表中存在此广告，则更新此广告数据
                if (TimeUtils.isCurrentDateTimeInPlan(
                        ad.getAdvsPlayBeginDatetimes(), ad.getAdvsPlayEndDatetimes(),
                        ad.getAdvsPlayBeginTime(), ad.getAdvsPlayEndTime(), curTime)) {
                    curAdVideoList.set(index, ad);
                    hasVideo = true;
                }
            }
        }
        if (!hasVideo)
            Log.d(TAG, "getCurrentVideoList: 当前时段无视频！");
        return hasVideo;
    }

    /**
     * 播放初始视频
     */
    private void playInitVideo() {
        if (null == DispenserCache.initAdVideoList || 0 == DispenserCache.initAdVideoList.size())
            return;
        isPlayInitVideo = true;
        AdvsVideo ad = DispenserCache.initAdVideoList.get(initAdIndex % DispenserCache.initAdVideoList.size());
        playerManager.play(ad.getAdvsVideoLocaltionPath());

    }

    /**
     * 播放curDownAdVideoList中的视频
     */
    private void playVideo() {
        if (null == curAdVideoList || 0 == curAdVideoList.size()) return;
        AdvsVideo ad = curAdVideoList.get(curAdIndex % curAdVideoList.size());
        if (!TimeUtils.isCurrentDateTimeInPlan(
                ad.getAdvsPlayBeginDatetimes(), ad.getAdvsPlayEndDatetimes(),
                ad.getAdvsPlayBeginTime(), ad.getAdvsPlayEndTime(), curTime)) {
            // 当前列表中有过了时间段的视频，则remove掉，重新刷新一次
            curAdVideoList.remove(ad);
            if (0 == curAdVideoList.size()) {
                playInitVideo();
                return;
            } else {
                playVideo();
                return;
            }
        }
        isPlayInitVideo = false;
//        Log.d(TAG, "playVideo: 本地路径：" + ad.getAdvsVideoLocaltionPath());
        playerManager.play(ad.getAdvsVideoLocaltionPath());
//        playerManager.play(UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR + "dsadasdas.mp4");
    }

    /**
     * 从推送中解析出pushAdVideoList
     */
    private void getPushStrategy() {
        Log.d(TAG, "getPushStrategy: 开始处理strategy的json数据");
        // 从本地文件取出数据，如果是已经处理的数据，则不响应；反之则处理。
        String decode = CommonUtil.decode(FileUtil.readTxtFile(UriConstant.APP_ROOT_PATH +
                UriConstant.VIDEO_DIR + UriConstant.VIDEO_PUSH_FILE_NAME));
        if (TextUtils.isEmpty(decode)) {
            Log.d(TAG, "getPushStrategy: 无推送数据！");
            return;
        }
        FileUtil.saveContentToSdcard(UriConstant.APP_ROOT_PATH +
                        UriConstant.VIDEO_DIR + UriConstant.VIDEO_PUSH_FILE_NAME,
                CommonUtil.encode(Constant.VIDEO_PUSH_HANDLE_DOING + decode.substring(1)));
        String status = decode.substring(0, 1);
        if (Constant.VIDEO_PUSH_HANDLE_DOING.equals(status)) {
            Log.d(TAG, "getPushStrategy: 已经处理过推送，不响应");
            return;
        }
        List<AdvsVideo> adList = JSONArray.parseArray(decode.substring(1), AdvsVideo.class);
        if (null == adList || 0 == adList.size()) {
            Log.d(TAG, "getPushStrategy: 推送数据有误！");
            // TODO: 2018/6/8 0008 是否上报一次错误？
            return;
        }
        for (AdvsVideo ad : adList) {
            if (null == ad) continue;
            String upDate = ad.getAdvsPlayBeginDatetimes();
            String downDate = ad.getAdvsPlayEndDatetimes();
            Log.d(TAG, "getPushStrategy: upDate = " + upDate + ", downDate = " + downDate + ", url = " + ad.getAdvsVideoDownloadPath());
            if (TimeUtils.isFutureSchedule(upDate, downDate, curTime)) {
                Log.d(TAG, "getPushStrategy: ooooooooooooooook! put!");
                pushAdVideoList.add(ad);
            }
        }
        Log.d(TAG, "getPushStrategy: json处理完毕");
        // 删除本次策略中没有的广告的本地文件
        try {
            List<AdvsVideo> allDbAdList = dbManager.findAll(AdvsVideo.class);
            for (AdvsVideo ad : allDbAdList) {
                int index = VideoUtils.getAdIndexFromList(ad, pushAdVideoList);
                Log.d(TAG, "refreshAllAdVideoData: index = " + index);
                if (-1 == index) {
                    String localPath = ad.getAdvsVideoLocaltionPath();
                    Log.d(TAG, "refreshAllAdVideoData: 删除本地文件：" + localPath);
                    FileUtil.deleteFile(localPath);

                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void downloadAllVideo() {
        Log.d(TAG, "getPushStrategy: 进入循环下载");
        // 下载完毕，则去同步各种数据
        if (pushAdIndex >= pushAdVideoList.size()) {
            Log.d(TAG, "getPushStrategy: 全部视频状态为已下载，return");
            myHandler.sendEmptyMessageDelayed(Constant.MSG_ALL_DOWN_COMPLETE, Constant.ALL_DOWN_WAIT_TIME);
            return;
        }
        // 没下完，则下载
        AdvsVideo ad = pushAdVideoList.get(pushAdIndex);
        // 如果为空，则下载下一个
        if (null == ad) {
            Log.d(TAG, "getPushStrategy: 本条广告为空，return");
            pushAdIndex++;
            downloadAllVideo();
            return;
        }
        // 已经是本地的，则更新isLocal并下载下一个
        String localPath = VideoUtils.checkIfVideoIsLocal(ad, allAdVideoList);
        if (!TextUtils.isEmpty(localPath)) {
            Log.d(TAG, "getPushStrategy: 本条广告已有本地路径，return");
            ad.setLocal(true);
            ad.setAdvsVideoLocaltionPath(localPath);
            pushAdVideoList.set(pushAdIndex, ad);
            pushAdIndex++;
            downloadAllVideo();
            return;
        }
        // 下载地址为空，上报地址错误
        if (TextUtils.isEmpty(ad.getAdvsVideoDownloadPath())) {
            Log.d(TAG, "onError: dl_info: URL为空！");
            // TODO: 2018/6/6 0006 上报地址错误
            pushAdVideoList.remove(ad);
            return;
        }
        downloadVideo(ad);
    }

    private void downloadVideo(AdvsVideo ad) {
        if (isDownloading) {
            Log.d(TAG, "downloadVideo: 正在下载，return..");
            if (myHandler.hasMessages(Constant.MSG_WAITING_THEN_DOWNLOAD)) {
                myHandler.removeMessages(Constant.MSG_WAITING_THEN_DOWNLOAD);
            }
            myHandler.sendEmptyMessageDelayed(Constant.MSG_WAITING_THEN_DOWNLOAD,
                    Constant.IS_DOWNING_WAIT_TIME * 1000);
            return;
        }
        String downloadPath = ad.getAdvsVideoDownloadPath();
        Log.d(TAG, "downloadVideo: 开始下载广告视频 pushAdIndex = " + pushAdIndex + ", url = " + downloadPath);
        isDownloading = true;
        DownloadManager dlManager = DownloadManager.getInstance();
        dlManager.setDownloadCallback(new DownloadCallback() {
            @Override
            public void onComplete(String localPath) {
                Log.d(TAG, "onComplete: dl_info: 下载完成！localPath -- " + localPath);
                isDownloading = false;
                AdvsVideo ad = pushAdVideoList.get(pushAdIndex);
                ad.setLocal(true);
                ad.setAdvsVideoLocaltionPath(localPath);
                pushAdVideoList.set(pushAdIndex, ad);
                pushAdIndex++;
                downloadAllVideo();
            }

            @Override
            public void onError(String msg) {
                Log.d(TAG, "onError: dl_info: 下载错误！msg -- " + msg);
                isDownloading = false;
                // 网址错误则上报错误信息；其他错误则放在最后再下
                if (msg.contains(Constant.DOWN_ERROR_MSG_WRONG_URL) || msg.contains(Constant.DOWN_ERROR_MSG_WRONG_BASE_URL)) {
                    Log.d(TAG, "onError: dl_info: URL有误！");
                    // TODO: 2018/6/8 0008 上报地址错误
                    pushAdVideoList.remove(pushAdIndex);
                    downloadAllVideo();
                    return;
                }
                Log.d(TAG, "onError: dl_info: 将本广告视频移动至list最后");
                AdvsVideo advsVideo = pushAdVideoList.get(pushAdIndex);
                pushAdVideoList.remove(pushAdIndex);
                pushAdVideoList.add(advsVideo);
                if (myHandler.hasMessages(Constant.MSG_WAITING_THEN_DOWNLOAD)) {
                    myHandler.removeMessages(Constant.MSG_WAITING_THEN_DOWNLOAD);
                }
                myHandler.sendEmptyMessageDelayed(Constant.MSG_WAITING_THEN_DOWNLOAD,
                        Constant.IS_DOWNING_WAIT_TIME * 1000);
            }

            @Override
            public void onProgress(int progress) {
                Log.d(TAG, "onProgress: dl_info: 正在下载.. progress = " + progress);
            }
        });
        dlManager.startDown(Constant.DOWNLOADAPK_ID, downloadPath.substring(0, downloadPath.lastIndexOf('/') + 1),
                downloadPath, UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR);
    }

    private void refreshAllAdVideoData() {
        Log.d(TAG, "refreshAllAdVideoData: 开始更新数据库及缓存list");
        playerManager.stop();
        // 同步数据到数据库
        try {
            dbManager.delete(AdvsVideo.class);
            dbManager.saveOrUpdate(pushAdVideoList);
            FileUtil.deleteFile(UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR + UriConstant.VIDEO_PUSH_FILE_NAME);
        } catch (DbException e) {
            e.printStackTrace();
        }
        // 同步数据到各个list
        curAdIndex = 0;
        allAdVideoList.clear();
        DispenserCache.initAdVideoList.clear();
        DispenserCache.freeAdVideoList.clear();
        dividerAds(pushAdVideoList);
    }

    private void dividerAds(List<AdvsVideo> adVideoList) {
        Log.d(TAG, "refreshAllAdVideoData: 开始广告视频分类");
        if (null == adVideoList || 0 == adVideoList.size()) {
            Log.d(TAG, "dividerAds: adVideoList为空");
            return;
        }
        for (AdvsVideo ad : adVideoList) {
            switch (ad.getAdvsPlayScene()) {
                case Constant.AD_TYPE_IDLE:
                    allAdVideoList.add(ad);
                    break;
                case Constant.AD_TYPE_FREE:
                    DispenserCache.freeAdVideoList.add(ad);
                    break;
                case Constant.AD_TYPE_INIT:
                    DispenserCache.initAdVideoList.add(ad);
                    break;
            }
        }
    }

    public void listenDevice() {

        //设置设备参数
        String deviceconfig = BaseSharedPreferences.getString(MainActivity.this,Constant.DEVICE_CONFIG_STRING_KEY);
        SysDeviceMonitorConfig sysDeviceMonitorConfig = JSONObject.parseObject(deviceconfig,SysDeviceMonitorConfig.class);



//        motCfgPpFlow


        //设置DevUtil里面的初始值
//        DevUtil



//        private int run_sTDS=20;     //原水TDS值
//        private int run_oTDS=10;     //出水TDS值
//        private int run_hotTemp=42;  //热水温度
//        private int run_coolTemp=42; //冷水温度
//        private byte run_bHot=1;     //加热状态，01 未加热，02 加热
//        private byte run_bCool=1;    //制冷状态，01 未制冷，02 制冷
//        private byte run_bWater=1;   //制水状态，01 未制水，02 制水
//        private byte run_bRinse=1;   //冲洗状态，01 未冲洗，02 冲洗
//        private byte run_bFault=1;   //源水缺水，01 不缺水，02 缺水
//        private byte run_bLeak=1;    //漏水，01 未漏水，02 漏水
//        private byte run_bSwitch=1;  //开关机状态，01 ，02 关
//        private int run_bCup=1;      //缺杯，01 不缺杯，02 缺杯
//        private boolean run_hotWaterSW=false;     //热水出开关
//        private boolean run_normalWaterSW=false;  //温水出开关
//        private boolean run_coolWaterSW=false;    //冷水出开关
//        private int run_waterFlow=0;     //本次总出水计量
//
//
//        private byte run_bSta=-1;    //通讯状态，0:offline;1:online;-1:Stop
//        private String run_upTime = ComUtil.getNowStr();//刷新时间
//
//        //定值参数
//        private int pam_rinseInterval=5;     //冲洗间隔，单位分钟
//        private int pam_rinseTimeLong=10;    //冲洗时长，单位秒
//        private int pam_hotTemp=75;          //加热温度
//        private int pam_coolTemp=8;          //制冷温度
//        private boolean pam_hotEnabled=true;  //加热使能
//        private boolean pam_coolEnabled=true; //制冷使能




        if(Constant.TEST ==false){
        //水质监听
        Time t = new Time();
        Time tupload = new Time();
        tupload.setToNow();
        int houtupload = t.hour;
        int minuteupload = t.minute;
        int seconduplod = t.second;
        Date updatetime = TimeRun.tasktime(houtupload, minuteupload, seconduplod);
        TimeRun timeRun = new TimeRun(MainActivity.this, updatetime, myHandler, Constant.UPLOAD_TIME, DATADELETE, Constant.TIME_OPERATE_UPDATEWATER);
        timeRun.startTimer();
        //定时刷新二维码
        Time sCodeTime = new Time();
        sCodeTime.setToNow();
        int hourscode = t.hour;
        int minutescode = t.minute + 1;
        int secondcode = t.second;
        Date sCodeTimeUpdate = TimeRun.tasktime(houtupload, minuteupload, seconduplod);
        TimeRun timeRunScode = new TimeRun(MainActivity.this, sCodeTimeUpdate, myHandler, Constant.UPLOAD_TIME, Constant.MSG_UPDATE_SCODE, Constant.TIME_OPETATE_UPDATESCODE);
        timeRunScode.startTimer();

        //预警
        Time wArningTime = new Time();
        wArningTime.setToNow();
        int hourwaring = t.hour;
        int minutewaring  = t.minute + 1;
        int secondwaring  = t.second;
        Date waringTimeUpdate = TimeRun.tasktime(hourwaring, minutewaring, secondwaring);
        TimeRun waringRunScode = new TimeRun(MainActivity.this, waringTimeUpdate, myHandler, Constant.UPLOAD_TIME, Constant.MSG_UPDATE_SCODE, Constant.TIME_OPETATE_UPDATESCODE);
        waringRunScode.startTimer();
        }


    }

    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /**
     * 判断Activity是否可用
     *
     * @return
     */
    private boolean isActivityValidate() {
        // TODO: 2018/6/12 0012 判断能弹窗的前提
        boolean b = this.isDestroyed() || this.isFinishing();
        return !b;
//        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
//        for (ActivityManager.RunningTaskInfo info : list) {
//            // 注意这里的 topActivity 包含 packageName和className
//            if (info.topActivity.toString().equals(this) || info.baseActivity.toString().equals(this)) {
//                Log.i(TAG, info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
//                return true;
//            }
//        }
//        Log.d(TAG, "isActivityValidate: 不可用！");
//        return false;
    }

    /**
     * 获取设备实体
     * device_number：设备唯一标识ID
     */
    public void GetDeviceInfo() {
        GetDeviceInfo deviceInfo = new GetDeviceInfo();
        String str = deviceInfo.getIMEI(this);

        DeviceEntity deviceEntity = new DeviceEntity();
        deviceEntity.setDevice_number(str);

        try {
            dbManager.save(deviceEntity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    // -------------------------- 回调 start --------------------------

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.exit:
                TimeBack timeback = new TimeBack(exit, 30000, 1000);
                break;
            case R.id.leftpop:
                //免费喝水跳转到广告，倒计时然后进入操作界面，隐藏操作界面
                leftoperate.setVisibility(View.GONE);
                rightoperate.setVisibility(View.GONE);
                break;
            case R.id.rightpop:
                //弹出二维码
                break;
            case R.id.wantwater:
                popWaterSale.showPopupWindow(new View(mContext));
                break;
            case R.id.tobehot:
                //加热使能
                break;
            case R.id.tobecool:
                //制冷使能
                break;
            case R.id.chongxi:
                //冲洗使能
                break;
        }

    }

    //region --------- ijk 监听 start ---------

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete: isPlayInitVideo = " + isPlayInitVideo);
        curTime = TimeUtils.getCurrentTime();
        if (isPlayInitVideo) {
            initAdIndex += 1;
        } else {
            AdvsVideo curAd = allAdVideoList.get(curAdIndex % curAdVideoList.size());
            AdvsPlayRecode curAdRecord = new AdvsPlayRecode(curAd.getAdvsId(), deviceId, TimeUtils.getCurrentTime(),
                    curAd.getAdvsVideoLengthOfTime(), curAd.getAdvsChargMode(),
                    curAd.getAdvsIndustry(), curAd.getAdvsPlayScene());
            try {
                dbManager.save(curAdRecord);
            } catch (DbException e) {
                e.printStackTrace();
            }
            curAdIndex += 1;
        }
        loopPlayVideo();
    }

    /**
     * 错误
     * 暂时只有一种错误回调，即what = MediaPlayer.MEDIA_ERROR_UNKNOWN， extra = 0，后期可做修改（IjkVideoView中）
     *
     * @param what
     */
    @Override
    public void onError(int what, int extra) {
//        Log.d(TAG, "onError: what = " + what + ", extra = " + extra);
        if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            //媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
            Toast.makeText(mContext, "网络服务错误", Toast.LENGTH_LONG).show();
        } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
            Toast.makeText(mContext, "文件不存在或错误，或网络不可访问错误", Toast.LENGTH_SHORT).show();
        }
        playerManager.onDestroy();//释放
        playVideo();//播放
    }

    @Override
    public void onInfo(int what, int extra) {
        Log.d(TAG, "onInfo: what = " + what + ", extra = " + extra);
    }
    //endregion --------- ijk end ---------

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        /*if (requestCode == PermissionUtils.CODE_INTERNET) {
            showToast("访问网络权限已申请");
        }
        if (requestCode == PermissionUtils.CODE_READ_PHONE_STATE) {
            showToast("访问电话状态权限已申请");
        }
        if (requestCode == PermissionUtils.CODE_ACCESS_NETWORK_STATE) {
            showToast("获取网络信息状态权限已申请");
        }
        if (requestCode == PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE) {
            showToast("读取SD卡权限已申请");
        }
        if (requestCode == PermissionUtils.CODE_MOUNT_UNMOUNT_FILESYSTEMS) {
            showToast("挂载、反挂载外部文件系统权限已申请");
        }*/
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                //判断是否勾选禁止后不再询问
                boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                if (showRequestPermission) {
                    Toast.makeText(MainActivity.this, "权限未申请", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // -------------------------- 回调 end --------------------------

    // -------------------------- 内部类 start --------------------------

    public class DynamicReceiver extends XGPushBaseReceiver {
        @Override
        public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

        }

        @Override
        public void onUnregisterResult(Context context, int i) {

        }

        @Override
        public void onSetTagResult(Context context, int i, String s) {

        }

        @Override
        public void onDeleteTagResult(Context context, int i, String s) {

        }

        @Override
        public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
            Log.d(TAG, "onTextMessage: receive new push");
            String pushString = xgPushTextMessage.toString();
            Log.e("收到消息: ", pushString);

            PushEntity pushEntity = JSONObject.parseObject(pushString, PushEntity.class);
            if (null == pushEntity) {
                Log.d(TAG, "onTextMessage: 推送为空！");
                return;
            }
            // 获取内容
            String content = pushEntity.getOperationContent();
            if (TextUtils.isEmpty(content)) {
                Log.d(TAG, "onTextMessage: 推送内容为空！");
                Log.d(TAG, "onTextMessage: pushEntity = " + pushEntity.toString());
                return;
            }
            Log.d(TAG, "onTextMessage: 操作类型：" + pushEntity.getOperationType());
            switch (pushEntity.getOperationType()) {
                case Constant.PUSH_OPERATION_TYPE_OPERATE:
                    //TODO 收到推送后的操作  1冲洗 2开盖 3开关机
                    String operateflag = "1";
                    if ("1".equals(operateflag)) {
                        ControllerUtils.operateDevice(6, false);
                    }

                    if ("2".equals(operateflag)) {
                        ControllerUtils.operateDevice(5, false);
                    }

                    if ("3".equals(operateflag)) {
                        ControllerUtils.operateDevice(2, false);
                    }

                    break;
                case Constant.PUSH_OPERATION_TYPE_CONFIG:
                    // 存入本地文件
                    Log.d(TAG, "onTextMessage: 推送类型为：配置。开始将push的strategy存入本地..");
                    FileUtil.saveContentToSdcard(UriConstant.APP_ROOT_PATH +
                                    UriConstant.VIDEO_DIR + UriConstant.VIDEO_PUSH_FILE_NAME,
                            CommonUtil.encode(Constant.VIDEO_PUSH_HANDLE_TO_DO + content));
                    // 发送延时消息处理
                    if (myHandler.hasMessages(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH)) {
                        myHandler.removeMessages(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH);
                    }
                    myHandler.sendEmptyMessageDelayed(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH,
                            Constant.RECEIVE_PUSH_VIDEO_STRATEGY_WAIT_TIME * 1000);
                    break;
                case Constant.PUSH_OPERATION_TYPE_LOGIN:
                    break;
                case Constant.PUSH_OPERATION_TYPE_UPDATE_ID:
                    break;
            }

        }

        @Override
        public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

        }

        @Override
        public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

        }
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0:
//                    byte[] data = (byte[])msg.obj
                    Toast.makeText(mContext, "定时   String[][] data = msg.getData().ge;检测水质", Toast.LENGTH_SHORT).show();
                    break;
                case DATADELETE:
                    String test = msg.obj.toString();
                    Toast.makeText(mContext, "定时" + test, Toast.LENGTH_SHORT).show();
                    break;
                case Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH:
                    Log.d(TAG, "handleMessage: start deal video strategy push");
                    pushAdIndex = 0;
                    curTime = TimeUtils.getCurrentTime();
                    getPushStrategy();
                    downloadAllVideo();
                    break;

                case Constant.MSG_UPDATE_SCODE:
                    Log.e("成功", "更新二维码成功");
                    break;

                case Constant.MSG_WAITING_THEN_DOWNLOAD:
                    downloadAllVideo();
                    break;
                case Constant.MSG_ALL_DOWN_COMPLETE:
                    refreshAllAdVideoData();
                    loopPlayVideo();
                    break;
                /*case 1002:
                    // 模拟接受到推送消息
                    Log.d(TAG, "handleMessage: 33333333333333333 接到消息了");
                    onReceivePush();
                    break;*/
            }
        }
    }

    // -------------------------- 内部类 end --------------------------

    // -------------------------- View start --------------------------

    /**
     * 跳转到机器故障Activity
     *
     * @param errReason
     */
    public void moveToBreakDownActivity(String errReason) {
        dismissAllPop();
        Intent intent = new Intent(mContext, BreakDownActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_BREAK_DOWN_ERR_REASON, errReason);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 跳转到免费广告Activity
     *
     * @param adDuration
     */
    public void moveToFreeAdActivity(int adDuration) {
        Intent intent = new Intent(mContext, FreeAdActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.KEY_FREE_AD_DURATION, adDuration);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 我要喝水
     */
    public void showPopWantWater() {
        if (isActivityValidate()) {
            if (null == popWantWater) {
                popWantWater = new PopWantWater(MainActivity.this, drinkMode);
            }
            popWantWater.showPopupWindow(new View(MainActivity.this));
        }
    }

    /**
     * 售水模式
     */
    public void showPopWaterSale() {
        if (isActivityValidate()) {
            if (null == popWaterSale) {
                popWaterSale = new PopWaterSale(MainActivity.this);
            }
            popWaterSale.showPopupWindow(new View(MainActivity.this));
        }
    }

    /**
     * 租赁模式/买断模式：二维码
     */
    public void showPopQrCode() {
        if (isActivityValidate()) {
            if (null == popQrCode) {
                popQrCode = new PopQrCode(MainActivity.this);
            }
            popQrCode.showPopupWindow(new View(MainActivity.this));
        }
    }

    /**
     * 左操作面板
     */
    public void showPopLeftOperate() {
        if (isActivityValidate()) {
            if (null == popLeft) {
                popLeft = new PopLeftOperate(MainActivity.this);
            }
            popLeft.showPopupWindow(new View(MainActivity.this));
        }
    }

    /**
     * 右操作面板
     */
    public void showPopRightOperate() {
        if (isActivityValidate()) {
            if (null == popRight) {
                popRight = new PopRightOperate(MainActivity.this, popLeft);
            }
            popRight.showPopupWindow(new View(MainActivity.this));
        }
    }

    /**
     * 充值
     */
    public void showPopBuy() {
        if (isActivityValidate()) {
            if (null == popBuy) {
                popBuy = new PopBuy(MainActivity.this, "test");
            }
            popBuy.showPopupWindow(new View(MainActivity.this));
        }
    }

    /**
     * 热水警告
     */
    public void showPopWarning() {
        if (isActivityValidate()) {
            if (null == popWarning) {
                popWarning = new PopWarning(MainActivity.this);
            }
            popWarning.showPopupWindow(new View(MainActivity.this));
        }
    }

    public void dismissPop(PopupWindow pop) {
        if (isActivityValidate()) {
            if (null != pop && pop.isShowing()) {
                Log.d(TAG, "dismissPop: -----------1--------");
                pop.dismiss();
            }
        }
    }

    private void dismissAllPop() {
        dismissPop(popWantWater);
        dismissPop(popWaterSale);
        dismissPop(popQrCode);
        dismissPop(popLeft);
        dismissPop(popRight);
        dismissPop(popWarning);
        dismissPop(popBuy);
    }

    //region -------------------------- View end --------------------------

    /**
     * 模拟收到信鸽推送的回调，后面有真的写好了，这里的内容就移过去
     */
    /*private void onReceivePush() {
        // TODO: 2018/6/5 0005 判断是否是重复推送，重复则忽略
        String pushString = "{" +
                "\"deviceId\":0," +
                "\"operationContent\":[" +
                "{\"advsId\":1,\"advsPlayScene\":0,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"12:07:17\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"23:07:17\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/MarkRonson_2014.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
                "{\"advsId\":2,\"advsPlayScene\":0,\"advsPlayBeginDatetime\":1527593061000,\"advsPlayBeginDatetimes\":\"2018-05-29\",\"advsPlayBeginTime\":\"08:07:17\",\"advsPlayConfigDtlId\":2,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527593061000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"10:07:17\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/LeeSmolin_2003.mp4\",\"advsVideoLengthOfTime\":1,\"advsVideoLocaltionPath\":\"\",\"local\":false}" +
                "{\"advsId\":3,\"advsPlayScene\":1,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/AditiShankardass_2009I_480.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
//                "{\"advsId\":4,\"advsPlayScene\":0,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"12:07:17\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"23:07:17\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/KiranBirSethi_2009I.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
//                "{\"advsId\":5,\"advsPlayScene\":0,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"10:07:17\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"23:07:17\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/StephenWilkes_2016.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
//                "{\"advsId\":6,\"advsPlayScene\":1,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/AnnMarieThomas_2011-480p.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
                "{\"advsId\":7,\"advsPlayScene\":0,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"\",\"advsVideoDownloadPath\":\"http://112.253.22.165/22/i/i/h/h/iihhwacyhbbxmleszykibadklhesym/sh.yinyuetai.com/37B50162DC0FD54E945776F4162B4BB9.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
                "]," +
                "\"operationType\":2," +
                "\"pushIdList\":" +
                "[" +
                "\"5d9609025fbbd471466caaacfaeb3629816b1ef1\"" +
                "]," +
                "\"title\":\"推送广告信息\"" +
                "}";
        Log.d(TAG, "handleMessage: receive new push");
        PushEntity pushEntity = JSONObject.parseObject(pushString, PushEntity.class);
        if (null == pushEntity) {
            Log.d(TAG, "onReceivePush: 推送为空！");
            return;
        }
        Log.d(TAG, "onReceivePush: 操作类型：" + pushEntity.getOperationType());
        switch (pushEntity.getOperationType()) {
            case Constant.PUSH_OPERATION_TYPE_OPERATE:
                break;
            case Constant.PUSH_OPERATION_TYPE_CONFIG:
                // 获取内容
                String content = pushEntity.getOperationContent();
                if (TextUtils.isEmpty(content)) {
                    Log.d(TAG, "onReceivePush: 推送内容为空！");
                    Log.d(TAG, "onReceivePush: pushEntity = " + pushEntity.toString());
                    return;
                }
                // 存入本地文件
                Log.d(TAG, "onReceivePush: 444444444444444 存本地了");
                FileUtil.saveContentToSdcard(UriConstant.APP_ROOT_PATH +
                        UriConstant.VIDEO_DIR + UriConstant.VIDEO_PUSH_FILE_NAME,
                        CommonUtil.encode(Constant.VIDEO_PUSH_HANDLE_TO_DO + content));
                // 发送延时消息处理
                if (myHandler.hasMessages(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH)) {
                    myHandler.removeMessages(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH);
                }
                myHandler.sendEmptyMessageDelayed(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH,
                        Constant.RECEIVE_PUSH_VIDEO_STRATEGY_WAIT_TIME * 1000);
                break;
            case Constant.PUSH_OPERATION_TYPE_LOGIN:
                break;
            case Constant.PUSH_OPERATION_TYPE_UPDATE_ID:
                break;
        }
    }*/


    /**
     * 用来判断服务是否运行
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    /*private boolean isServiceRunning(String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }*/

    //endregion
}