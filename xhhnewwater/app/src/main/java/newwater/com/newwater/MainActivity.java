package newwater.com.newwater;

import android.content.Context;
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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.danikula.videocache.HttpProxyCacheServer;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import newwater.com.newwater.DataBaseUtils.Sys_Device_Monitor_Config_DbOperate;
import newwater.com.newwater.DataBaseUtils.XutilsInit;
import newwater.com.newwater.Processpreserving.ComThread;
import newwater.com.newwater.Processpreserving.DaemonService;
import newwater.com.newwater.beans.Advs_Video;
import newwater.com.newwater.beans.DeviceEntity;
import newwater.com.newwater.beans.DispenserCache;
import newwater.com.newwater.broadcast.ConnectionChangeReceiver;
import newwater.com.newwater.broadcast.MessageReceiver;
import newwater.com.newwater.broadcast.UpdateBroadcast;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.interfaces.DownloadCallback;
import newwater.com.newwater.manager.DownloadManager;
import newwater.com.newwater.manager.IjkManager;
import newwater.com.newwater.utils.ControllerUtils;
import newwater.com.newwater.utils.GetDeviceInfo;
import newwater.com.newwater.utils.TimeBack;
import newwater.com.newwater.utils.TimeRun;
import newwater.com.newwater.utils.TimeUtils;
import newwater.com.newwater.utils.VideoUtils;
import newwater.com.newwater.view.PopWindow;
import newwater.com.newwater.view.PopWindowChooseWaterGetWay;
import newwater.com.newwater.view.Pop_WantWater;

import static newwater.com.newwater.utils.PermissionUtils.permissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IjkManager.PlayerStateListener {
    private static final String TAG = "MainActivity";

    private static final int DOWNLOADAPK_ID = 10;
    public static final String filePath = "/sdcard/xutils/xUtils_1.avi";

    private Context mContext;
    private TextView dixieccup;//纸杯和我要饮水按钮
    private Boolean operateornot = false;
    public static LinearLayout leftoperate;//左边操作区域
    public static LinearLayout rightoperate;//右边操作区域

//    public static ImageView  wantwater;//我要饮水

    //popwindow操作
    private View contentView;
    private LinearLayout leftpop;
    private LinearLayout rightpop;


    //出杯子的提示
    private View outCupView;
    private RelativeLayout outcupleft;
    private RelativeLayout outcupright;

    private PopWindow popChooseWater;
    private PopWindowChooseWaterGetWay popChooseWatera;

    private RelativeLayout root;
    private ImageView qrcode;

    //VideoView
//    private CustomerVideoView videoplay;


    //viewpager 视频
    private ViewPager viewpager;

    private Timer timer;//定时器，用于实现轮播

    static int pos = 0;
    private HttpProxyCacheServer proxy;
    private final static int DATADELETE = 2;

    Pop_WantWater pop_wantWater = null;

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

    //四个使能按钮
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


    // 视频播放
    /*播放器*/
    private IjkManager playerManager;
    /*初始视频列表*/
    private List<Advs_Video> initAdVideoList;
    /*推送策略中的视频列表*/
    private List<Advs_Video> pushAdVideoList;
    /*所有要播放的闲时视频列表*/
    private List<Advs_Video> allVideoList; //（同步后应该与push列表一致）
    /*当前要播放的闲时视频列表*/
    private List<Advs_Video> curAdVideoList;
//    /*已下载的免费喝水视频列表*/
//    private List<Advs_Video> freeAdVideoList;  // (此列表一旦有变动需要设置DispenserCache里的对应列表！)
    /*当前播放的视频在initAdVideoList中的index*/
    private int initAdIndex;
    /*当前播放的视频在curAdVideoList中的index*/
    private int curAdIndex;
//    /*当前播放的视频在freeAdVideoList中的index*/
//    private int freeAdIndex;  // (此变量一旦有变动需要设置DispenserCache里的对应变量！)
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
    /*当前时段的是否全部下载完毕*/
    private boolean hasDownCur;
    /*正在播放的闲时视频的id*/
    private String curPlayAdId;
    /*正在下载的视频的id*/
    private String curDownAdId;
    /*当前时间*/
    private String curTime;

    // 权限集合
    List<String> permissionList = new ArrayList<>();

    private MyHandler myHandler;
    private DbManager dbManager;

    private ComThread comThread;//comThread服务是用来获取设备数据

    private DevUtil devUtil;//设备操作的工具类


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != playerManager) {
            playerManager.start();
        }
    }

    @Override
    protected void onPause() {
        if (null != playerManager) {
            playerManager.pause();
        }
        super.onPause();
    }

    /**
     * 初始化权限
     * 腾讯信鸽推送
     */
    private void initPer() {
        int i;
        for (i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }
        if (permissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            Toast.makeText(MainActivity.this, "已经授权", Toast.LENGTH_LONG).show();
        } else {//请求权限方法
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, i);
        }
        initData();
        initView();
        initVideo();
        getDevice_Monitor_Config();
        registerXinGe();

        initComThread();

        // 开启logcat输出，方便debug，发布时请关闭
        // XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        XGPushConfig.enableDebug(this, true);
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.i("TPush", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.i("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });

        //initToken();
    }

    public void initComThread(){
        if(null == DaemonService.comThread){
            comThread = new ComThread(MainActivity.this,null);
        }
        if(null==devUtil){
        devUtil = new DevUtil(null);
        }



    }

    public void registerXinGe(){
        DynamicReceiver myReceiver = new DynamicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MessageReceiver.PUSHACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(myReceiver, filter);
    }



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
            String text = "收到消息:" + xgPushTextMessage.toString();
            Log.e("收到消息", text);

            //TODO 收到推送后的操作  1冲洗 2开盖 3开关机
            String operateflag = "1";
            if("1".equals(operateflag)){
            ControllerUtils.operatedevice(6,false);
            }

            if("2".equals(operateflag)){
                ControllerUtils.operatedevice(5,false);
            }

            if("3".equals(operateflag)){
                ControllerUtils.operatedevice(2,false);
            }



        }

        @Override
        public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

        }

        @Override
        public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

        }
    }

    private void initData() {
        mContext = MainActivity.this;
        dbManager = new XutilsInit(MainActivity.this).getDb();
        // 网络变化广播接收器
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        ConnectionChangeReceiver myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
        // 视频播放
        proxy = App.getProxy(mContext);
        initAdVideoList = new ArrayList<>();
        pushAdVideoList = new ArrayList<>();
        allVideoList = new ArrayList<>();
        DispenserCache.freeAdVideoList = new ArrayList<>();
        curAdVideoList = new ArrayList<>();
//        DispenserCache.setFreeAdVideoList(new ArrayList<Advs_Video>());
        // 从数据库中取出数据填充列表
        try {
            List<Advs_Video> allAds = dbManager.findAll(Advs_Video.class);
            if (null != allAds && 0 != allAds.size()) {
                for (Advs_Video ad : allAds) {
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


        // 获取当前设备是哪一种模式
        int model = TestJSON.getModel();
        if (model == 0) {
            //售水模式
        }

        //监控设备
        listenDevice();

        //监控设备
//        startService(new Intent(mContext, Service1.class));

//        startService(new Intent(mContext, DaemonService.class));
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
    }

    /*private void initToken() {
        try {
            String url = RestUtils.getUrl(UriConstant.NEWAPK);
            OkHttpUtils.getAsyn(url,
                    new OkHttpUtils.StringCallback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.i("getApkInfo", request.toString());
                        }

                        @Override
                        public void onResponse(String response) {
                            Log.e("getApkInfo", response);
                        }
                    });
        } catch (Exception e) {
            Log.i("Error", e.getMessage());
            e.printStackTrace();
        }
    }*/

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

    private void loopPlayVideo() {
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
        boolean hasVideo = false;
        for (Advs_Video ad : allVideoList) {
            int index = VideoUtils.getAdIndexFromList(ad, curAdVideoList);
            if (-1 == index) {
                // 列表中不存在此广告，则加入此广告
                if (TimeUtils.isCurrentDateTimeInPlan(
                        ad.getAdvs_play_begin_date(), ad.getAdvs_play_end_date(),
                        ad.getAdvs_play_begin_time(), ad.getAdvs_play_end_time(), curTime)) {
                    curAdVideoList.add(ad);
                    hasVideo = true;
                }
            } else if (-1 < index) {
                // 列表中存在此广告，则更新此广告数据
                if (TimeUtils.isCurrentDateTimeInPlan(
                        ad.getAdvs_play_begin_date(), ad.getAdvs_play_end_date(),
                        ad.getAdvs_play_begin_time(), ad.getAdvs_play_end_time(), curTime)) {
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
        if (null == initAdVideoList || 0 == initAdVideoList.size()) return;
        isPlayInitVideo = true;
        Advs_Video ad = initAdVideoList.get(initAdIndex % initAdVideoList.size());
        String proxyUrl = proxy.getProxyUrl(ad.getAdvs_video_localtion_path());
        playerManager.play(proxyUrl);

    }

    /**
     * 播放curDownAdVideoList中的视频
     */
    private void playVideo() {
        if (null == curAdVideoList || 0 == curAdVideoList.size()) return;
        Advs_Video ad = curAdVideoList.get(curAdIndex % curAdVideoList.size());
        if (!TimeUtils.isCurrentDateTimeInPlan(
                ad.getAdvs_play_begin_date(), ad.getAdvs_play_end_date(),
                ad.getAdvs_play_begin_time(), ad.getAdvs_play_end_time(), curTime)) {
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
        String proxyUrl = proxy.getProxyUrl(ad.getAdvs_video_localtion_path());
        playerManager.play(proxyUrl);
    }

    /**
     * 从推送中解析出pushAdVideoList
     */
    private void getStrategy() {
        // TODO: 2018/6/6 0006 json解析成list
        String testa = TestJSON.strategy();
        JSONArray alldata = JSON.parseArray(testa);
        // 下架在未来则加入到map中
        for (int i = 0; i < alldata.size(); i++) {
            String string = alldata.getString(i);
            JSONObject testobj = JSON.parseObject(string);
            String upDate = testobj.getString("upDate");
            String downDate = testobj.getString("downDate");
            String playPeriod = testobj.getString("playPeriod");
            String videoListString = testobj.getString("videoList");
            String ad = testobj.getString("ad");
            Advs_Video advs_video = JSONObject.parseObject(ad, Advs_Video.class);
            Log.d(TAG, "initData: all: upDate = " + upDate + ", downDate = " + downDate
                    + ", playPeriod = " + playPeriod + ", videoListString = " + videoListString);
            if (TimeUtils.isFutureSchedule(upDate, downDate, curTime)) {
                Log.d(TAG, "initData: put: upDate = " + upDate + ", downDate = " + downDate
                        + ", playPeriod = " + playPeriod + ", videoListString = " + videoListString);
//                JSONArray objects = JSON.parseArray(videoListString);
                List<Advs_Video> objects = JSONArray.parseArray(videoListString, Advs_Video.class);
                pushAdVideoList.add(advs_video);
            }
        }
    }

    public void downloadAllVideo() {
        // 下载完毕，则去同步各种数据
        if (pushAdIndex >= pushAdVideoList.size()) {
            refreshAllAdVideoData();
            loopPlayVideo();
            return;
        }
        // 没下完，则下载
        Advs_Video ad = pushAdVideoList.get(pushAdIndex);
        // 如果为空，则下载下一个
        if (null == ad) {
            pushAdIndex++;
            downloadAllVideo();
            return;
        }
        // 已经是本地的，则更新isLocal并下载下一个
        String localPath = VideoUtils.checkIfVideoIsLocal(ad, allVideoList);
        if (null != localPath) {
            ad.setLocal(true);
            ad.setAdvs_video_localtion_path(localPath);
            pushAdVideoList.set(pushAdIndex, ad);
            pushAdIndex++;
            downloadAllVideo();
            return;
        }
        // 下载地址为空，上报地址错误
        if (null == ad.getAdvs_video_download_path()) {
            // TODO: 2018/6/6 0006 上报地址错误
            pushAdVideoList.remove(ad);
            return;
        }
        downloadVideo(ad);
    }

    private void downloadVideo(Advs_Video ad) {
        if (isDownloading) {
            Toast.makeText(MainActivity.this, "正在下载", Toast.LENGTH_SHORT).show();
            myHandler.sendEmptyMessageDelayed(Constant.NSG_IS_DOWNLOADING_WAITING,
                    Constant.IS_DOWNING_WAIT_TIME * 1000);
            return;
        }
        Log.d(TAG, "downloadVideo: 开始下载广告视频");
        isDownloading = true;
        DownloadManager manager = DownloadManager.getInstance();
        manager.setDownloadCallback(new DownloadCallback() {
            @Override
            public void onComplete(String localPath) {
                Log.d(TAG, "onComplete: dl_info: 下载完成！localPath -- " + localPath);
                isDownloading = false;
                Advs_Video ad = pushAdVideoList.get(pushAdIndex);
                ad.setLocal(true);
                ad.setAdvs_video_localtion_path(localPath);
                pushAdVideoList.set(pushAdIndex, ad);
                pushAdIndex ++;
                downloadAllVideo();
            }

            @Override
            public void onError(String msg) {
                Log.d(TAG, "onError: dl_info: 下载错误！msg -- " + msg);
                isDownloading = false;
                // 网址错误则上报错误信息；其他错误则放在最后再下
                if (msg.contains(Constant.DOWN_ERROR_EXCEPTION_WRONG_URL)) {
                    Log.d(TAG, "onError: dl_info: URL有误！");
                    pushAdVideoList.remove(pushAdIndex);
                    downloadAllVideo();
                    return;
                }
                Advs_Video advsVideo = pushAdVideoList.get(pushAdIndex);
                pushAdVideoList.remove(pushAdIndex);
                pushAdVideoList.add(advsVideo);
                downloadAllVideo();
            }

            @Override
            public void onProgress(int progress) {
                Log.d(TAG, "onProgress: dl_info: 正在下载.. progress = " + progress);
            }
        });
        manager.startDown(DOWNLOADAPK_ID, UriConstant.AD_VIDEO_BASE_URL, ad.getAdvs_video_download_path(),
                UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR);
    }

    private void refreshAllAdVideoData() {
        playerManager.stop();
        // 同步数据到数据库
        try {
            dbManager.delete(Advs_Video.class);
            dbManager.saveOrUpdate(pushAdVideoList);
        } catch (DbException e) {
            e.printStackTrace();
        }
        // 同步数据到各个list
        curAdIndex = 0;
        allVideoList.clear();
        DispenserCache.freeAdVideoList.clear();
//        DispenserCache.setFreeAdVideoList(new ArrayList<Advs_Video>());
        dividerAds(pushAdVideoList);
    }

    private void dividerAds(List<Advs_Video> adVideoList) {
        if (null == adVideoList || 0 ==adVideoList.size());
        for (Advs_Video ad : adVideoList) {
            switch (ad.getAdvs_type()) {
                case 0:
                    allVideoList.add(ad);
                    break;
                case 1:
                    DispenserCache.freeAdVideoList.add(ad);
                    break;
            }
        }
//        DispenserCache.setFreeAdVideoList(freeAdVideoList);
    }

    /**
     * 这个函数在Activity创建完成之后会调用。购物车悬浮窗需要依附在Activity上，如果Activity还没有完全建好就去
     * 调用showCartFloatView()，则会抛出异常
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (null == popChooseWater) {
            Pop_WantWater pop_wantWater = new Pop_WantWater(MainActivity.this);
            pop_wantWater.showPopupWindow(new View(this));
        } else {
            Pop_WantWater pop_wantWater = new Pop_WantWater(MainActivity.this);
            pop_wantWater.showPopupWindow(new View(this));
        }
    }

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
                popChooseWater.showPopupWindow(new View(mContext));
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

    // ------------ ijk 监听 start --------------
    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete: isPlayInitVideo = " + isPlayInitVideo);
        curTime = TimeUtils.getCurrentTime();
        if (isPlayInitVideo) {
            initAdIndex += 1;
        } else {
            // TODO: 2018/6/5 0005 更新播放记录，定时向服务器发送数据
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
        Log.d(TAG, "onError: what = " + what + ", extra = " + extra);

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
    public void onLoading() {

    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onInfo(int what, int extra) {
        Log.d(TAG, "onInfo: what = " + what + ", extra = " + extra);
    }

    // ------------ ijk 监听 end --------------


    public void listenDevice() {


//        Time t = new Time();
//        t.setToNow();
//        int hout = t.hour;
//        int minute = t.minute;
//        int second = t.second;
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, hout); //凌晨1点
//        calendar.set(Calendar.MINUTE, minute);
//        calendar.set(Calendar.SECOND, second);
//        Date date = calendar.getTime(); //第一次执行定时任务的时间
//
//        Timer timer = new Timer();
//        final MyHandler myHandler = new MyHandler();
//
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//
//                Message msg = new Message();
//                msg.what = 0;
//                myHandler.sendMessage(msg);
//            }
//        };
//        final long PERIOD_DAY = 2 * 1000;
//        timer.schedule(task, 1000, PERIOD_DAY);

//        try{
//            Thread.sleep(1000);
//        }catch(Exception ex){
//            timer.cancel();
//        }

        //水质监听
        myHandler = new MyHandler();
        Time t = new Time();
        Time tupload = new Time();
        tupload.setToNow();
        int houtupload = t.hour;
        int minuteupload = t.minute;
        int seconduplod = t.second;

//        if (houtupload > 24) {
//            houtupload = 0;
//        }
//        if (minuteupload > 60) {
//            minuteupload = 0;
//        }
        Date updatetime = TimeRun.tasktime(houtupload, minuteupload, seconduplod);

        TimeRun timeRun = new TimeRun(MainActivity.this, updatetime, myHandler, Constant.UPLOAD_TIME, DATADELETE, Constant.TIME_OPERATE_UPDATEWATER);

        //定时刷新二维码

        Time sCodeTime = new Time();
        sCodeTime.setToNow();
        int hourscode = t.hour;
        int minutescode = t.minute + 1;
        int secondcode = t.second;


        Date sCodeTimeUpdate = TimeRun.tasktime(houtupload, minuteupload, seconduplod);

        TimeRun timeRunScode = new TimeRun(MainActivity.this, updatetime, myHandler, Constant.UPLOAD_TIME, Constant.MSG_UPDATE_SCODE, Constant.TIME_OPETATE_UPDATESCODE);

    }

    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
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
                    pushAdIndex = 0;
                    getStrategy();
                    downloadAllVideo();
                    break;

                case Constant.MSG_UPDATE_SCODE:
                    Log.e("成功", "更新二维码成功");
                    break;

                case Constant.NSG_IS_DOWNLOADING_WAITING:
                    downloadAllVideo();
                    break;
            }
        }
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
                    showToast("权限未申请");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showToast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
    }

    private static long lastClickTime;

    /**
     * 是否是快速点击（是则return，防止快速连续点击）
     * @return
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= Constant.FAST_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    /**
     * 模拟收到信鸽推送的回调，后面有真的写好了，这里的内容就移过去
     */
    private void onReceivePush() {
        // TODO: 2018/6/5 0005 判断是否是重复推送，重复则忽略
        /*推送判断，是不是视频播放策略的推送*/
        if (true) {
            if (myHandler.hasMessages(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH)) {
                myHandler.removeMessages(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH);
            }
            myHandler.sendEmptyMessageDelayed(Constant.MSG_NEW_AD_VIDEO_STRATEGY_PUSH,
                    Constant.RECEIVE_PUSH_VIDEO_STRATEGY_WAIT_TIME * 1000);

        }
    }


    /**
     * 用来判断服务是否运行
     *
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

}
