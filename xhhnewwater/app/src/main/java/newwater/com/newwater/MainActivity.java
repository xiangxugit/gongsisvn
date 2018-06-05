package newwater.com.newwater;

import android.app.ActivityManager;
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
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.xutils.common.task.PriorityExecutor;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import newwater.com.newwater.DataBaseUtils.Sys_Device_Monitor_Config_DbOperate;
import newwater.com.newwater.DataBaseUtils.XutilsInit;
import newwater.com.newwater.beans.Advs_Video;
import newwater.com.newwater.beans.DeviceEntity;
import newwater.com.newwater.beans.ViewShow;
import newwater.com.newwater.beans.person;
import newwater.com.newwater.broadcast.ConnectionChangeReceiver;
import newwater.com.newwater.broadcast.UpdateBroadcast;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.interfaces.OnUpdateUI;
import newwater.com.newwater.manager.IjkManager;
import newwater.com.newwater.service.DownloadIntentService;
import newwater.com.newwater.utils.BaseSharedPreferences;
import newwater.com.newwater.utils.GetDeviceInfo;
import newwater.com.newwater.utils.OkHttpUtils;
import newwater.com.newwater.utils.RestUtils;
import newwater.com.newwater.utils.TimeBack;
import newwater.com.newwater.utils.TimeRun;
import newwater.com.newwater.utils.TimeUtils;
import newwater.com.newwater.view.PopWindow;
import newwater.com.newwater.view.PopWindowChooseWaterGetWay;
import newwater.com.newwater.view.Pop_WantWater;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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

    private DevUtil devUtil = null;

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
    private IjkManager player;
    /*推送策略中的视频列表*/
    private List<Advs_Video> pushAdVideoList;
    /*所有要播放的视频的列表*/
    private List<Advs_Video> adVideoList; //（同步后应该与push列表一致）
    /*当前要播放的闲时广告列表*/
    private List<Advs_Video> curAdVideoList;
    /*当前已下载的要播放的闲时广告列表*/
    private List<Advs_Video> curDownAdVideoList;
    /*当前播放的视频在curDownAdVideoList中的index*/
    private int videoIndex;
    /*是否正在播放视频*/
    private boolean isPlaying;
    /*是否正在下载*/
    private boolean isDownloading;
    /*当前时段的是否全部下载完毕*/
    private boolean hasDownCur;
    /*正在播放的视频的id*/
    private String curPlayAdId;
    /*正在下载的视频的id*/
    private String curDownAdId;
    /*当前时间*/
    private String curTime;

    // 权限集合
    List<String> permissionList = new ArrayList<>();

    private XutilsInit xutilsInit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        PermissionsFragment fragment = new PermissionsFragment();
//        transaction.replace(R.id.content_fragment, fragment);
//        transaction.commit();
        xutilsInit = new XutilsInit(MainActivity.this);
        initPer();
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

//        initToken();
    }

    private void initData() {
        mContext = MainActivity.this;
        // 视频播放
        proxy = App.getProxy(mContext);
        adVideoList = new ArrayList<>();
        curTime = TimeUtils.getCurrentTime();
        videoIndex = 0;
        // 配置文件解析
        getStrategy();
    }

    private void getStrategy() {
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
                adVideoList.add(advs_video);
            }
        }
       /*
        String test = alldata.getString(0);
        com.alibaba.fastjson.JSONObject testobj = JSON.parseObject(test);
        String videoListString = testobj.getString("videoList");
        videolist = JSON.parseArray(videoListString);*/

        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        ConnectionChangeReceiver myReceiver=new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);

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
        player = new IjkManager(this, this);
        player.setFullScreenOnly(true);
        player.setScaleType(IjkManager.SCALETYPE_FILLPARENT);
        player.playInFullScreen(true);
        player.setOnPlayerStateChangeListener(this);
        playInitVideo();
        // 获取当前时段应播视频的列表
        if (getCurrentVideoList()) {
            // 有视频，则播放
            playVideo();
            // 无视频，
        }
        //
        loopPlayVideo();

        downloadVideo();
    }

//    private void initToken() {
//        try {
//            String url = RestUtils.getUrl(UriConstant.NEWAPK);
//            OkHttpUtils.getAsyn(url,
//                    new OkHttpUtils.StringCallback() {
//                        @Override
//                        public void onFailure(Request request, IOException e) {
//                            Log.i("getApkInfo", request.toString());
//                        }
//
//                        @Override
//                        public void onResponse(String response) {
//                            Log.e("getApkInfo", response);
//                        }
//                    });
//        } catch (Exception e) {
//            Log.i("Error", e.getMessage());
//            e.printStackTrace();
//        }
//    }

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
        // 获取当前时段应播视频的列表
        if (getCurrentVideoList()) {
            // 有视频，则播放
            playVideo();
            // 无视频，
        }
    }

    /**
     * 返回值为当前时段是否有videoList
     *
     * @return
     */
    private boolean getCurrentVideoList() {
        boolean hasVideo = false;
        curAdVideoList = new ArrayList<>();
        // 方式一：用map存所有视频
        /*for (String period : adVideoMap.keySet()) {
            if (TimeUtils.isCurrentTimeInPeriod(period, curTime)) {
                curAdVideoList = adVideoMap.get(period);
                Log.d(TAG, "getCurrentVideoList: 当前时段有视频：" + curAdVideoList);
                hasVideo = true;
            }
        }*/
        // 方式二：用list存所有视频
        for (Advs_Video ad : adVideoList) {
            if (TimeUtils.isCurrentDateInSchedule(
                    ad.getAdvs_play_begin_date(), ad.getAdvs_play_end_date(), curTime) &&
                    TimeUtils.isCurrentTimeInPeriod(ad.getAdvs_play_begin_time(),
                            ad.getAdvs_play_end_time(), curTime)) {
                curAdVideoList.add(ad);
                hasVideo = true;
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
        String proxyUrl = proxy.getProxyUrl(UriConstant.APP_ROOT_PATH +
                UriConstant.VIDEO_DIR + UriConstant.INIT_VIDEO_PATH);
        player.play(proxyUrl);
    }

    /**
     * 播放curDownAdVideoList中的视频
     */
    private void playVideo() {
        String proxyUrl = proxy.getProxyUrl(curAdVideoList.get(videoIndex % curAdVideoList.size())
                .getAdvs_video_download_path());
        player.play(proxyUrl);
    }

    private void downloadVideo() {
        if (isServiceRunning(DownloadIntentService.class.getName())) {
            Toast.makeText(MainActivity.this, "正在下载", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "downloadVideo: 开始下载广告视频");
        Intent intent = new Intent(MainActivity.this, DownloadIntentService.class);
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.VIDEO_DOWNLOAD_ID, DOWNLOADAPK_ID);
        for (Advs_Video ad : curAdVideoList) {
            String downloadUrl = ad.getAdvs_video_download_path();
            bundle.putString(Constant.VIDEO_DOWNLOAD_URL, downloadUrl);
            bundle.putString(Constant.VIDEO_DOWNLOAD_FILE_NAME,
                    downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1));
            intent.putExtras(bundle);
            startService(intent);
        }
        // 方式一
//        String downloadUrl = "AditiShankardass_2009I_480.mp4";
//        Intent intent = new Intent(MainActivity.this, DownloadIntentService.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("download_url", downloadUrl);
//        bundle.putInt("download_id", DOWNLOADAPK_ID);
//        bundle.putString("download_file", downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1));
//        intent.putExtras(bundle);
//        startService(intent);
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
        Log.d(TAG, "onComplete: ");
        curTime = TimeUtils.getCurrentTime();
        videoIndex += 1;
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
        player.onDestroy();//释放
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
        final MyHandler myHandler = new MyHandler();
        Time t = new Time();
        Time tupload = new Time();
        tupload.setToNow();
        int houtupload = t.hour;
        int minuteupload = t.minute + 1;
        int seconduplod = t.second;

        if (houtupload > 24) {
            houtupload = 0;
        }
        if (minuteupload > 60) {
            minuteupload = 0;
        }
        Date updatetime = TimeRun.tasktime(houtupload, minuteupload, seconduplod);

        TimeRun timeRun = new TimeRun(MainActivity.this, updatetime, myHandler, Constant.UPLOAD_TIME, DATADELETE,1);


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
                    Toast.makeText(mContext, "定时"+test, Toast.LENGTH_SHORT).show();

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
            xutilsInit.getDb().save(deviceEntity);
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

    /**
     * 用来判断服务是否运行
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    private boolean isServiceRunning(String className) {

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
    }

    private void showToast(String string) {
        Toast.makeText(MainActivity.this, string, Toast.LENGTH_LONG).show();
    }

}
