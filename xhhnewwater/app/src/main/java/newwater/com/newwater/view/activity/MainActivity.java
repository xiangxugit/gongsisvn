package newwater.com.newwater.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.serialport.DevUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import newwater.com.newwater.beans.SysDeviceNoticeAO;
import newwater.com.newwater.utils.LogUtils;
import newwater.com.newwater.utils.OkHttpUtils;
import newwater.com.newwater.utils.RestUtils;
import newwater.com.newwater.utils.UploadLocalData;
import newwater.com.newwater.utils.XutilsInit;
import newwater.com.newwater.R;
import newwater.com.newwater.beans.AdvsPlayRecode;
import newwater.com.newwater.processpreserve.ComThread;
import newwater.com.newwater.processpreserve.DaemonService;
import newwater.com.newwater.beans.AdvsVideo;
import newwater.com.newwater.beans.DispenserCache;
import newwater.com.newwater.beans.PushEntity;
import newwater.com.newwater.broadcast.ConnectionChangeReceiver;
import newwater.com.newwater.broadcast.MessageReceiver;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.interfaces.DownloadCallback;
import newwater.com.newwater.manager.DownloadManager;
import newwater.com.newwater.manager.IjkManager;
import newwater.com.newwater.utils.BaseSharedPreferences;
import newwater.com.newwater.utils.CommonUtil;
import newwater.com.newwater.utils.FileUtil;
import newwater.com.newwater.utils.ControllerUtils;
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
import okhttp3.Request;

public class MainActivity extends BaseActivity implements IjkManager.PlayerStateListener {

    private static final String TAG = "MainActivity";
    public static final String FLAG = "UPDATE";
    private final static int DATA_DELETE = 2;

    // 私有变量
    private Context mContext;
    private MyHandler myHandler;
    private DbManager dbManager;
    private ComThread comThread;//comThread服务是用来获取设备数据
    private DevUtil devUtil;//设备操作的工具类
    private ConnectionChangeReceiver connectReceiver;
    private DynamicReceiver dynamicReceiver;

    // popWindow
    private PopWantWater popWantWater = null;
    private PopWaterSale popWaterSale = null;
    private PopQrCode popQrCode = null;
    private PopLeftOperate popLeft = null;
    private PopRightOperate popRight = null;
    private PopBuy popBuy = null;
    private PopWarning popWarning = null;

    // 设备信息
    private int deviceId;
    private int drinkMode/* = 1*/;
    private String rentDeadline;
    private String contractInfo;

    //左边操作窗口的组件
    private TextView hotWaterText;
    private TextView coolWaterText;
    private TextView ppmValue;
    private TextView ppm;//下方的

    // 四个使能按钮
    private LinearLayout toBeHot;
    private LinearLayout toBeCool;
    private LinearLayout zhiShui;
    private LinearLayout chongXi;

    // 设备状态
    private ImageView hotIcon;//是否加热的imageview
    private TextView hotText;//是否加热text
    private ImageView coolIcon;//是否制冷的imageView
    private TextView coolText;//是否制冷text
    private ImageView waterProduceIcon;//是否制水的imageView
    private TextView waterProduceText;//是否制水的text
    private ImageView flushIcon;//冲洗imageView
    private TextView flushText;//冲洗text;
    private Button exit;

    // 视频播放
    /*播放器*/
    private IjkManager playerManager;
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
    /*是否正在下载*/
    private boolean isDownloading;
    /*当前播放的是初始视频*/
    private boolean isPlayInitVideo;
    /*当前时间*/
    private String curTime;

    //要调用另一个APP的activity所在的包名
    String packageName = "purewater.com.leadapp";
    //要调用另一个APP的activity名字
    String activityName = ".MainActivity";

    // -------------------------- 生命周期 start --------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initData();
        initView();
        initVideo();
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
                    LogUtils.d(TAG, "dismissPop: 看完广告视频，可以喝水了");
                    dismissPop(popWantWater);
                    showPopLeftOperate();
                    showPopRightOperate();
                } else {
                    LogUtils.d(TAG, "dismissPop: “我要喝水”要出现了");
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
        if (null != dynamicReceiver) {
            unregisterReceiver(dynamicReceiver);
        }
        if (null != connectReceiver) {
            unregisterReceiver(connectReceiver);
        }
        super.onDestroy();
    }

    // -------------------------- 生命周期 end --------------------------

    private void initData() {
//        int a = 1/0;
        mContext = MainActivity.this;
        myHandler = new MyHandler();
        // 获取设备信息
        getDeviceInfo();
        // 数据库
        dbManager = new XutilsInit(MainActivity.this).getDb();
        // 广播接收
        registerMyReceiver();
        // 租期检查
        loopCheckRentTime();
        // 视频播放
        DispenserCache.freeAdVideoList = new ArrayList<>();
        DispenserCache.initAdVideoList = new ArrayList<>();
        pushAdVideoList = new ArrayList<>();
        allAdVideoList = new ArrayList<>();
        curAdVideoList = new ArrayList<>();
        // （从本地文件中取出推送数据，看是否需要处理）
        getPushStrategy();
        // （从数据库中取出数据填充列表）
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
        //监控设备

        startService(new Intent(mContext, DaemonService.class));
        setTingDevice();
        listenDevice();
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        //左边的操作界面组件获得
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(MainActivity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.pop_left, null);
        hotWaterText = (TextView) view.findViewById(R.id.hot_water_text);
        coolWaterText = (TextView) view.findViewById(R.id.cool_water_text);
        ppmValue = (TextView) view.findViewById(R.id.ppm_value);
        ppm = (TextView) view.findViewById(R.id.ppm);
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
        // 1、从SharedPreference获取
        if (BaseSharedPreferences.containInt(mContext, Constant.DEVICE_ID_KEY)) {
            deviceId = BaseSharedPreferences.getInt(mContext, Constant.DEVICE_ID_KEY);
//            deviceId = 1;
        }
        if (BaseSharedPreferences.containInt(mContext, Constant.DRINK_MODE_KEY)) {
            drinkMode = BaseSharedPreferences.getInt(mContext, Constant.DRINK_MODE_KEY);
//            drinkMode = Constant.DRINK_MODE_WATER_SALE;
        }
        if (BaseSharedPreferences.containInt(mContext, Constant.RENT_DEADLINE_KEY)) {
            rentDeadline = BaseSharedPreferences.getString(mContext, Constant.RENT_DEADLINE_KEY);
        }
        if (BaseSharedPreferences.containInt(mContext, Constant.CONTRACT_INFO_KEY)) {
            contractInfo = BaseSharedPreferences.getString(mContext, Constant.CONTRACT_INFO_KEY);
        }
        // 2、没有则跳转到BreakDown页面
        if (0 == deviceId) {
            LogUtils.d(TAG, "getDeviceInfo: 无deviceId！");
            moveToBreakDownActivity(getString(R.string.break_down_reason_no_device_id));
            return;
        }
        if (0 == drinkMode) {
            LogUtils.d(TAG, "getDeviceInfo: 无drinkMode！");
            moveToBreakDownActivity(getString(R.string.break_down_reason_no_drink_mode));
            return;
        }
        if (TextUtils.isEmpty(rentDeadline)) {
            LogUtils.d(TAG, "getDeviceInfo: rentDeadline！");
            moveToBreakDownActivity(getString(R.string.break_down_reason_no_rent_deadline));
            return;
        }
        if (TextUtils.isEmpty(contractInfo)) {
            LogUtils.d(TAG, "getDeviceInfo: contractInfo！");
            moveToBreakDownActivity(getString(R.string.break_down_reason_no_contract_info));
            return;
        }
    }

    private void registerMyReceiver() {
        // 网络变化广播接收器
        IntentFilter connectFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(connectReceiver, connectFilter);
        // 信鸽广播接收器
        dynamicReceiver = new DynamicReceiver();
        IntentFilter dynamicFilter = new IntentFilter();
        dynamicFilter.addAction(MessageReceiver.PUSHACTION);
        dynamicFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(dynamicReceiver, dynamicFilter);

    }

    /**
     * 定期检查租期
     */
    private void loopCheckRentTime() {
        Time time = new Time();
        time.setToNow();
        Date updatetime = TimeRun.tasktime(time.hour, time.minute, time.second);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (Constant.DRINK_MODE_MACHINE_RENT == drinkMode) {
                    String deadline = BaseSharedPreferences.getString(mContext, Constant.RENT_DEADLINE_KEY);
                    String currentTime = TimeUtils.getCurrentTime();
                    if (!TimeUtils.isAvailDate(deadline, currentTime)) {
                        LogUtils.i(TAG, "run: 租期已到！currentTime = " + currentTime + ", deadline = " + deadline);
                        moveToBreakDownActivity(getString(R.string.break_down_reason_expired));
                    }
                }
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(task, updatetime, Constant.CHECK_RENT_DEADLINE_PERIOD * 1000);
    }

    public void listenDevice() {
        //水质监听
        Time uploadTime = new Time();
        uploadTime.setToNow();
        Date updatetime = TimeRun.tasktime(uploadTime.hour, uploadTime.minute, uploadTime.second);
        TimeRun timeRun = new TimeRun(MainActivity.this, updatetime, myHandler, Constant.UPLOAD_TIME, DATA_DELETE, Constant.TIME_OPERATE_UPDATEWATER);
        timeRun.startTimer();

        //定时刷新二维码
        Time sCodeTime = new Time();
        sCodeTime.setToNow();
        Date sCodeTimeUpdate = TimeRun.tasktime(sCodeTime.hour, sCodeTime.minute, sCodeTime.second);
        TimeRun timeRunScode = new TimeRun(MainActivity.this, sCodeTimeUpdate, myHandler, Constant.UPDATE_SCODE, Constant.MSG_UPDATE_SCODE, Constant.TIME_OPETATE_UPDATESCODE);
        timeRunScode.startTimer();

        //预警
        Time warningTime = new Time();
        warningTime.setToNow();
        Date waringTimeUpdate = TimeRun.tasktime(warningTime.hour, warningTime.minute, warningTime.second);
        TimeRun waringRunScode = new TimeRun(MainActivity.this, waringTimeUpdate, myHandler, Constant.UPLOAD_TIME, Constant.MSG_UPDATE_SCODE, Constant.TIME_OPETATE_WARNING);
        waringRunScode.startTimer();

        //启动水质上报
        String waterqualitylist = RestUtils.getUrl(UriConstant.WATERQUALITYLIST);
//        UploadLocalData uploadLocalData = new UploadLocalData(mContext);
//        uploadLocalData.upload(waterqualitylist,Constant.TIME_OPERATE_UPDATEWATER,Constant.UPLOAD_TIME);
        long uploadwarningtime = BaseSharedPreferences.getInt(MainActivity.this,Constant.DEVICE_UP_TIME_KEY)*60*1000;
        UploadLocalData.getInstance(mContext).upload(waterqualitylist, Constant.TIME_OPERATE_UPDATEWATER, uploadwarningtime);
        //预警信息上报
        String noticequalitylist = RestUtils.getUrl(UriConstant.NOTICEQUALITYLIST);
//        UploadLocalData uploadLocalData1 = new UploadLocalData(mContext);
//        uploadLocalData1.upload(noticequalitylist,Constant.TIME_OPETATE_WARNING,Constant.UPLOAD_TIME);
        UploadLocalData.getInstance(mContext).upload(noticequalitylist, Constant.TIME_OPETATE_WARNING, Constant.UPLOAD_TIME);
    }
    public void setTingDevice(){
        //设置加热的参数
        ControllerUtils controllerUtils = new ControllerUtils(MainActivity.this);
        controllerUtils.operateDevice(11,true);
        controllerUtils.operateDevice(0,true);
        controllerUtils.operateDevice(1,true);

        //按照时段播放


    }
    /**
     * 定时上报视频播放记录
     */
    private void scheduleUploadVideo() {
        String url = RestUtils.getUrl(UriConstant.AD_VIDEO_RECORD_LIST);
//        UploadLocalData ulManager = new UploadLocalData(mContext);
//        ulManager.upload(url, Constant.TIME_OPETATE_VIDEO, Constant.UPLOAD_TIME);
        UploadLocalData.getInstance(mContext).upload(url, Constant.TIME_OPETATE_VIDEO,
                Constant.AD_RECORD_UPLOAD_PERIOD * 1000);
    }

    /**
     * 循环播放广告视频
     */
    private void loopPlayVideo() {
        LogUtils.d(TAG, "loopPlayVideo: 开始循环播放广告视频");
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
        LogUtils.d(TAG, "getCurrentVideoList: 开始获取当前应播视频列表");
        boolean hasVideo = false;
        for (AdvsVideo ad : allAdVideoList) {
            int index = VideoUtils.getAdIndexFromList(ad, curAdVideoList);
            if (-1 == index) {
                // 列表中不存在此广告，则加入此广告
                LogUtils.d(TAG, "getCurrentVideoList: 当前视频列表中无此广告，查看是否加入" + ad.getAdvsVideoLocaltionPath());
                if (TimeUtils.isCurrentDateTimeInPlan(
                        ad.getAdvsPlayBeginDatetimes(), ad.getAdvsPlayEndDatetimes(),
                        ad.getAdvsPlayBeginTime(), ad.getAdvsPlayEndTime(), curTime)) {
                    LogUtils.d(TAG, "getCurrentVideoList: 时辰已到，加入");
                    curAdVideoList.add(ad);
                    hasVideo = true;
                } else {
                    LogUtils.d(TAG, "getCurrentVideoList: 时辰未到，再等等");
                }
            } else if (-1 < index) {
                // 列表中存在此广告，则更新此广告数据
                LogUtils.d(TAG, "getCurrentVideoList: 当前视频列表中有此广告，更新");
                if (TimeUtils.isCurrentDateTimeInPlan(
                        ad.getAdvsPlayBeginDatetimes(), ad.getAdvsPlayEndDatetimes(),
                        ad.getAdvsPlayBeginTime(), ad.getAdvsPlayEndTime(), curTime)) {
                    curAdVideoList.set(index, ad);
                    hasVideo = true;
                }
            }
        }
        if (!hasVideo)
            LogUtils.d(TAG, "getCurrentVideoList: 当前时段无视频！");
        return hasVideo;
    }

    /**
     * 播放初始视频
     */
    private void playInitVideo() {
        if (null == DispenserCache.initAdVideoList || 0 == DispenserCache.initAdVideoList.size()) {
            LogUtils.d(TAG, "playInitVideo: 无初始视频！");
            return;
        }
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
        LogUtils.d(TAG, "playVideo: 本地路径：" + ad.getAdvsVideoLocaltionPath());
        playerManager.play(ad.getAdvsVideoLocaltionPath());
    }

    /**
     * 从推送中解析出pushAdVideoList
     */
    private void getPushStrategy() {
        LogUtils.d(TAG, "getPushStrategy: 开始处理strategy的json数据");
        // 从本地文件取出数据，如果是已经处理的数据，则不响应；反之则处理。
        String decode = CommonUtil.decode(FileUtil.readTxtFile(UriConstant.APP_ROOT_PATH +
                UriConstant.VIDEO_DIR + UriConstant.VIDEO_PUSH_FILE_NAME));
        if (TextUtils.isEmpty(decode)) {
            LogUtils.d(TAG, "getPushStrategy: 无推送数据！");
            return;
        }
        FileUtil.saveContentToSdcard(UriConstant.APP_ROOT_PATH +
                        UriConstant.VIDEO_DIR + UriConstant.VIDEO_PUSH_FILE_NAME,
                CommonUtil.encode(Constant.VIDEO_PUSH_HANDLE_DOING + decode.substring(1)));
        String status = decode.substring(0, 1);
        if (Constant.VIDEO_PUSH_HANDLE_DOING.equals(status)) {
            LogUtils.d(TAG, "getPushStrategy: 已经处理过推送，不响应");
            return;
        }
//        List<AdvsVideo> adList = JSONArray.parseArray(decode.substring(1), AdvsVideo.class);
        List<AdvsVideo> adList = null;
        try {
            adList = JSONArray.parseArray(decode.substring(1), AdvsVideo.class);
        } catch (JSONException e) {
            LogUtils.d(TAG, "getPushStrategy: 推送数据无法转换成 AdvsVideo！");
        }
        if (null == adList || 0 == adList.size()) {
            LogUtils.d(TAG, "getPushStrategy: 推送数据有误！");
            return;
        }
        for (AdvsVideo ad : adList) {
            if (null == ad) continue;
            String upDate = ad.getAdvsPlayBeginDatetimes();
            String downDate = ad.getAdvsPlayEndDatetimes();
            LogUtils.d(TAG, "getPushStrategy: upDate = " + upDate + ", downDate = " + downDate + ", url = " + ad.getAdvsVideoDownloadPath());
            if (TimeUtils.isFutureSchedule(upDate, downDate, curTime)) {
                LogUtils.d(TAG, "getPushStrategy: ooooooooooooooook! put!");
                pushAdVideoList.add(ad);
            }
        }
        LogUtils.d(TAG, "getPushStrategy: json处理完毕");
        // 删除本次策略中没有的广告的本地文件
        try {
            List<AdvsVideo> allDbAdList = dbManager.findAll(AdvsVideo.class);
            for (AdvsVideo ad : allDbAdList) {
                int index = VideoUtils.getAdIndexFromList(ad, pushAdVideoList);
                LogUtils.d(TAG, "refreshAllAdVideoData: index = " + index);
                if (-1 == index) {
                    String localPath = ad.getAdvsVideoLocaltionPath();
                    LogUtils.d(TAG, "refreshAllAdVideoData: 删除本地文件：" + localPath);
                    FileUtil.deleteFile(localPath);

                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void downloadAllVideo() {
        LogUtils.d(TAG, "getPushStrategy: dl_info: 进入循环下载");
        // 下载完毕，则去同步各种数据
        if (pushAdIndex >= pushAdVideoList.size()) {
            LogUtils.d(TAG, "getPushStrategy: dl_info: 全部视频状态为已下载，return");
            myHandler.sendEmptyMessageDelayed(Constant.MSG_ALL_DOWN_COMPLETE, Constant.ALL_DOWN_WAIT_TIME);
            return;
        }
        // 没下完，则下载
        AdvsVideo ad = pushAdVideoList.get(pushAdIndex);
        // 如果为空，则下载下一个
        if (null == ad) {
            LogUtils.d(TAG, "getPushStrategy: dl_info: 本条广告为空，return");
            pushAdIndex++;
            downloadAllVideo();
            return;
        }
        // 已经是本地的，则更新isLocal并下载下一个
        String localPath = VideoUtils.checkIfVideoIsLocal(ad, allAdVideoList);
        if (!TextUtils.isEmpty(localPath)) {
            LogUtils.d(TAG, "getPushStrategy: dl_info: 本条广告已有本地路径，return");
            ad.setLocal(true);
            ad.setAdvsVideoLocaltionPath(localPath);
            pushAdVideoList.set(pushAdIndex, ad);
            pushAdIndex++;
            downloadAllVideo();
            return;
        }
        // 下载地址为空，上报地址错误
        if (TextUtils.isEmpty(ad.getAdvsVideoDownloadPath())) {
            LogUtils.d(TAG, "onError: dl_info: URL为空！");
            saveWrongUrlNotice(ad);
            pushAdVideoList.remove(ad);
            return;
        }
        downloadVideo(ad);
    }

    private void downloadVideo(AdvsVideo ad) {
        if (isDownloading) {
            LogUtils.d(TAG, "downloadVideo: dl_info: 正在下载，return..");
            if (myHandler.hasMessages(Constant.MSG_WAITING_THEN_DOWNLOAD)) {
                myHandler.removeMessages(Constant.MSG_WAITING_THEN_DOWNLOAD);
            }
            myHandler.sendEmptyMessageDelayed(Constant.MSG_WAITING_THEN_DOWNLOAD,
                    Constant.IS_DOWNING_WAIT_TIME * 1000);
            return;
        }
        String downloadPath = ad.getAdvsVideoDownloadPath();
        LogUtils.d(TAG, "downloadVideo: dl_info: 开始下载广告视频 pushAdIndex = " + pushAdIndex + ", url = " + downloadPath);
        isDownloading = true;
        DownloadManager dlManager = DownloadManager.getInstance();
        dlManager.setDownloadCallback(new DownloadCallback() {
            @Override
            public void onComplete(String localPath) {
                LogUtils.d(TAG, "onComplete: dl_info: 下载完成！localPath -- " + localPath);
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
                LogUtils.d(TAG, "onError: dl_info: 下载错误！msg -- " + msg);
                isDownloading = false;
                // 网址错误则上报错误信息；其他错误则放在最后再下
                if (msg.contains(Constant.DOWN_ERROR_MSG_WRONG_URL) || msg.contains(Constant.DOWN_ERROR_MSG_WRONG_BASE_URL)) {
                    LogUtils.d(TAG, "onError: dl_info: URL有误！");
                    saveWrongUrlNotice(pushAdVideoList.get(pushAdIndex));
                    pushAdVideoList.remove(pushAdIndex);
                    downloadAllVideo();
                    return;
                }
                LogUtils.d(TAG, "onError: dl_info: 将本广告视频移动至list最后");
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
                LogUtils.d(TAG, "onProgress: dl_info: 正在下载.. progress = " + progress);
            }
        });
        dlManager.startDown(mContext, Constant.DOWNLOADAPK_ID,
                downloadPath.substring(0, downloadPath.lastIndexOf('/') + 1), downloadPath,
                UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR);
    }

    /**
     * 存储广告视频下载地址错误的预警信息
     *
     * @param ad
     */
    private void saveWrongUrlNotice(AdvsVideo ad) {
        SysDeviceNoticeAO notice = new SysDeviceNoticeAO(
                BaseSharedPreferences.getInt(mContext, Constant.DEVICE_ID_KEY),
                Constant.NOTICE_TYPE_AD_URL_WRONG, Constant.NOTICE_LEVEL_ABNORMAL,
                ad.getAdvsId() + "", ad.getAdvsVideoDownloadPath(),
                TimeUtils.getCurrentTime());
        try {
            new XutilsInit(mContext).getDb().save(notice);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void refreshAllAdVideoData() {
        LogUtils.d(TAG, "refreshAllAdVideoData: 开始更新数据库及缓存list");
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
        LogUtils.d(TAG, "refreshAllAdVideoData: 开始广告视频分类");
        if (null == adVideoList || 0 == adVideoList.size()) {
            LogUtils.d(TAG, "dividerAds: adVideoList为空");
            return;
        }
        for (AdvsVideo ad : adVideoList) {
            switch (ad.getAdvsType()) {
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

    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /**
     * 跳转到LoadApp执行更新指令
     */
    private void launchLoadApp() {
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageName);

        final PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(resolveIntent, 0);

        if (resolveInfo != null && resolveInfo.size() != 0) {
            ResolveInfo ri = resolveInfo.iterator().next();
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                activityName = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                ComponentName cn = new ComponentName(packageName, activityName);
                Bundle bundle = new Bundle();
                bundle.putString("command", "update");
                intent.putExtras(bundle);
                intent.setComponent(cn);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, activityName);
            Bundle bundle = new Bundle();
            bundle.putString("command", "update");
            intent.putExtras(bundle);
            intent.setComponent(cn);
            startActivity(intent);
        }
    }

    /**
     * 判断Activity是否可用
     *
     * @return
     */
    private boolean isActivityValidate() {
        return !(this.isDestroyed() || this.isFinishing());
//        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
//        for (ActivityManager.RunningTaskInfo info : list) {
//            // 注意这里的 topActivity 包含 packageName和className
//            if (info.topActivity.toString().equals(this) || info.baseActivity.toString().equals(this)) {
//                LogUtils.i(TAG, info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
//                return true;
//            }
//        }
//        LogUtils.d(TAG, "isActivityValidate: 不可用！");
//        return false;
    }

    // -------------------------- 回调 start --------------------------
    // --------- ijk 监听 start ---------

    @Override
    public void onComplete() {
        LogUtils.d(TAG, "onComplete: isPlayInitVideo = " + isPlayInitVideo);
        curTime = TimeUtils.getCurrentTime();
        if (isPlayInitVideo) {
            initAdIndex += 1;
        } else {
            AdvsVideo curAd = allAdVideoList.get(curAdIndex % curAdVideoList.size());
            AdvsPlayRecode curAdRecord = new AdvsPlayRecode(curAd.getAdvsId(), deviceId, TimeUtils.getCurrentTime(),
                    curAd.getAdvsVideoLengthOfTime(), curAd.getAdvsChargMode(),
                    curAd.getAdvsIndustry(), curAd.getAdvsType());
            try {
                dbManager.save(curAdRecord);
                List<AdvsPlayRecode> all = dbManager.findAll(AdvsPlayRecode.class);
                LogUtils.d(TAG, "onComplete: all.size = " + all.size());
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
//        LogUtils.d(TAG, "onError: what = " + what + ", extra = " + extra);
        if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            //媒体服务器挂掉了。此时，程序必须释放MediaPlayer 对象，并重新new 一个新的。
            LogUtils.e(TAG, "onError: 视频播放：网络服务错误");
        } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
            LogUtils.e(TAG, "onError: 视频播放：文件不存在或错误，或网络不可访问错误");
        }
        if (isPlayInitVideo) {
            initAdIndex += 1;
        } else {
            curAdIndex += 1;
        }
        playerManager.onDestroy();//释放
        loopPlayVideo();
//        playVideo();//播放
    }

    @Override
    public void onInfo(int what, int extra) {
        LogUtils.d(TAG, "onInfo: what = " + what + ", extra = " + extra);
    }
    // --------- ijk 监听 end ---------

    // -------------------------- 回调 end --------------------------

    // -------------------------- 内部类 start --------------------------

    public class DynamicReceiver extends XGPushBaseReceiver {
        @Override
        public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
            LogUtils.e(TAG, "onRegisterResult: ");

        }

        @Override
        public void onUnregisterResult(Context context, int i) {
            LogUtils.e(TAG, "onUnregisterResult: ");
        }

        @Override
        public void onSetTagResult(Context context, int i, String s) {
            LogUtils.e(TAG, "onSetTagResult: ");
        }

        @Override
        public void onDeleteTagResult(Context context, int i, String s) {
            LogUtils.e(TAG, "onDeleteTagResult: ");
        }

        @Override
        public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
            LogUtils.d(TAG, "onTextMessage: receive new push");
            String pushString = xgPushTextMessage.getContent();
            LogUtils.i(TAG, "onTextMessage: 收到消息: " + pushString);
            PushEntity pushEntity = JSONObject.parseObject(pushString, PushEntity.class);
            if (null == pushEntity) {
                LogUtils.d(TAG, "onTextMessage: 推送为空！");
                return;
            }
            // 获取内容
            String content = pushEntity.getOperationContent();
            if (TextUtils.isEmpty(content)) {
                LogUtils.d(TAG, "onTextMessage: 推送内容为空！");
                LogUtils.d(TAG, "onTextMessage: pushEntity = " + pushEntity.toString());
                return;
            }
            LogUtils.d(TAG, "onTextMessage: 操作类型：" + pushEntity.getOperationType());
            String url;
            switch (pushEntity.getOperationType()) {
                case Constant.PUSH_OPERATION_TYPE_OPERATE:
                    LogUtils.d(TAG, "onTextMessage: 推送类型为：操作。");
                    //TODO 收到推送后的操作  1冲洗 2开盖 3开关机
                    ControllerUtils controllerUtils = new ControllerUtils(MainActivity.this);
                    String operateflag = "1";
                    if (Constant.DEVICE_OPERATE_FLUSH.equals(operateflag)) {
                        controllerUtils.operateDevice(6, false);
                    }
                    if (Constant.DEVICE_OPERATE_UNCAP.equals(operateflag)) {
                        controllerUtils.operateDevice(5, false);
                    }
                    if (Constant.DEVICE_OPERATE_ON_OFF.equals(operateflag)) {
                        controllerUtils.operateDevice(2, false);
                    }
                    if(Constant.DO_HOTTING == operateflag){
                        controllerUtils.operateDevice(ControllerUtils.DO_HOTTING,false);
                    }
                    if(Constant.DO_COOLING == operateflag){
                        controllerUtils.operateDevice(ControllerUtils.DO_COOLING,false);
                    }
                    if(Constant.DO_TURNOFFHOTTING==operateflag){
                        controllerUtils.operateDevice(ControllerUtils.DO_TURNOFFHOTTING,false);
                    }
                    if(Constant.DO_TURNOFFCOOLING==operateflag){
                        controllerUtils.operateDevice(ControllerUtils.DO_TURNOFFCOOLING,false);
                    }
                    break;
                case Constant.PUSH_OPERATION_TYPE_CONFIG:
                    break;
                case Constant.PUSH_OPERATION_TYPE_TARGET:
                    LogUtils.d(TAG, "onTextMessage: 推送类型为：视频（配置/行业）。");
                    url = RestUtils.getUrl(UriConstant.GET_AD_VIDEO_LIST + content);
                    OkHttpUtils.postAsyn(url, new OkHttpUtils.StringCallback() {
                        @Override
                        public void onFailure(int errCode, Request request, IOException e) {
                            LogUtils.d(TAG, "onFailure: 获取视频策略失败！errCode = " + errCode +
                                    ", response = " + request.toString());
                        }

                        @Override
                        public void onResponse(String response) {
                            LogUtils.d(TAG, "onResponse: 获取视频策略成功！ response = " + response);
                            JSONObject jsonObject = JSONObject.parseObject(response);
                            if (null == jsonObject) {
                                LogUtils.d(TAG, "onResponse: 视频策略获取response数据错误！");
                                return;
                            }
                            Object data = jsonObject.get("data");
                            if (null == data) {
                                LogUtils.d(TAG, "onResponse: 视频策略获取response的data数据错误！");
                                return;
                            }
                            // 存入本地文件
                            LogUtils.d(TAG, "onTextMessage: 开始将push的strategy存入本地..");
                            FileUtil.saveContentToSdcard(UriConstant.APP_ROOT_PATH +
                                            UriConstant.VIDEO_DIR + UriConstant.VIDEO_PUSH_FILE_NAME,
                                    CommonUtil.encode(Constant.VIDEO_PUSH_HANDLE_TO_DO + data.toString()));
                            // 发送延时消息处理
                            if (myHandler.hasMessages(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH)) {
                                myHandler.removeMessages(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH);
                            }
                            myHandler.sendEmptyMessageDelayed(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH,
                                    Constant.RECEIVE_PUSH_VIDEO_STRATEGY_WAIT_TIME * 1000);
                        }
                    });

                    break;
                case Constant.PUSH_OPERATION_TYPE_LOGIN:
                    LogUtils.d(TAG, "onTextMessage: 推送类型为：登录。");
                    // TODO: 2018/6/20 0020 登录
                    String userId = pushEntity.getOperationContent();
                    url = RestUtils.getUrl(UriConstant.GET_USER_INFO + userId);
                    OkHttpUtils.getAsyn(url, new OkHttpUtils.StringCallback() {
                        @Override
                        public void onFailure(int errCode, Request request, IOException e) {
                            LogUtils.d(TAG, "onFailure: 获取用户登录信息失败！errCode = " + errCode +
                                    ", response = " + request.toString());
                        }

                        @Override
                        public void onResponse(String response) {
                            LogUtils.d(TAG, "onResponse: 获取用户登录信息成功！ response = " + response);
                        }
                    });
                    DispenserCache.userIdTemp = userId;
                    dismissPop(popWantWater);
                    dismissPop(popQrCode);
                    showPopLeftOperate();
                    showPopRightOperate();
                    break;
                case Constant.PUSH_OPERATION_TYPE_UPDATE_APK:
                    LogUtils.d(TAG, "onTextMessage: 推送类型为：更新。");
                    launchLoadApp();
                    break;
            }
        }

        @Override
        public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
            LogUtils.d(TAG, "onNotifactionClickedResult:");
        }

        @Override
        public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
            LogUtils.d(TAG, "onNotifactionShowedResult:");
        }
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//                    byte[] data = (byte[])msg.obj
                    Toast.makeText(mContext, "定时   String[][] data = msg.getData().ge;检测水质", Toast.LENGTH_SHORT).show();
                    break;
                case DATA_DELETE:
                    String test = msg.obj.toString();
                    Toast.makeText(mContext, "定时" + test, Toast.LENGTH_SHORT).show();
                    break;
                case Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH:
                    LogUtils.d(TAG, "handleMessage: start deal video strategy push");
                    pushAdIndex = 0;
                    curTime = TimeUtils.getCurrentTime();
                    getPushStrategy();
                    downloadAllVideo();
                    break;

                case Constant.MSG_UPDATE_SCODE:
                    LogUtils.e(TAG, "成功" + "更新二维码成功");
                    break;

                case Constant.MSG_WAITING_THEN_DOWNLOAD:
                    downloadAllVideo();
                    break;
                case Constant.MSG_ALL_DOWN_COMPLETE:
                    refreshAllAdVideoData();
                    loopPlayVideo();
                    break;
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
                popLeft = new PopLeftOperate(MainActivity.this, popRight);
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
                LogUtils.d(TAG, "dismissPop: ");
                pop.dismiss();
            }
        }
    }

    public void dismissOperatePop() {
        dismissPop(popLeft);
        dismissPop(popRight);
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

    // -------------------------- View end --------------------------

}
