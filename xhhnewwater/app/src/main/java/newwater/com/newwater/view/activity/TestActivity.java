package newwater.com.newwater.view.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.alibaba.fastjson.JSONArray;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

import newwater.com.newwater.R;
import newwater.com.newwater.utils.XutilsInit;
import newwater.com.newwater.beans.AdvsVideo;
import newwater.com.newwater.beans.MessageEvent;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.interfaces.DownloadCallback;
import newwater.com.newwater.manager.DownloadManager;
import newwater.com.newwater.manager.IjkManager;
import newwater.com.newwater.utils.TimeUtils;
import newwater.com.newwater.utils.UploadLocalData;

public class TestActivity extends Activity implements View.OnClickListener, IjkManager.PlayerStateListener {

    private static final String TAG = "TestActivity";

    private static final int DOWNLOADAPK_ID = 10;

    private TextView tvResult;
    private Button btnPause;
    private Button btnStart;
    private Button btnGoOn;
    private Button btnDwon;
    private Button btnStopDown;
    private View btnJump;
    private Button btnConvert;
    private Button btnGetDb;
    private Button btnUpload;
    private DownloadManager dlManager;
    private DbManager dbManager;
    private List<AdvsVideo> adList;
    private IjkManager player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        test();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void initData() {
        EventBus.getDefault().register(this);
        dlManager = DownloadManager.getInstance();
        dbManager = new XutilsInit(TestActivity.this).getDb();
    }

    public void initView() {
        setContentView(R.layout.activity_test);
        tvResult = findViewById(R.id.tv_test);

        btnPause = findViewById(R.id.btn_pause);
        btnStart = findViewById(R.id.btn_start);
        btnGoOn = findViewById(R.id.btn_go_on);
        btnDwon = findViewById(R.id.btn_download);
        btnStopDown = findViewById(R.id.btn_stop_down);
        btnJump = findViewById(R.id.btn_jump);
        btnConvert = findViewById(R.id.btn_convert);
        btnGetDb = findViewById(R.id.btn_get_db);
        btnUpload = findViewById(R.id.btn_upload);

        btnPause.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnGoOn.setOnClickListener(this);
        btnDwon.setOnClickListener(this);
        btnStopDown.setOnClickListener(this);
        btnJump.setOnClickListener(this);
        btnConvert.setOnClickListener(this);
        btnGetDb.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }

    public void test() {
        // 时间测试
        String currentTime = TimeUtils.getCurrentTime();
        tvResult.setText(Environment.getExternalStorageDirectory().getPath());

        player = new IjkManager(this, R.id.test_ijk_video);
        player.setFullScreenOnly(true);
        player.setScaleType(IjkManager.SCALETYPE_FILLPARENT);
        player.playInFullScreen(false);
        player.setOnPlayerStateChangeListener(this);
//        String proxyUrl = "http://mirror.aarnet.edu.au/pub/TED-talks/911Mothers_2010W-480p.mp4";
        String proxyUrl = UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR + "MarkRonson_2014.mp4";
        player.play(proxyUrl);

//        try {
//            List<Advs_Play_Recode> all = dbManager.findAll(Advs_Play_Recode.class);
//            if (null == all)
//            for (Advs_Play_Recode ad : all) {
//                Log.d(TAG, "test: ad = " + ad.toString());
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }

//        try {
//            Log.d(TAG, "test: 清除广告数据库");
//            dbManager.delete(AdvsVideo.class);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }

        String s = "[" +
                "{\"advsId\":1,\"advsPlayScene\":0,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"12:07:17\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"23:07:17\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/MarkRonson_2014.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
                "{\"advsId\":2,\"advsPlayScene\":0,\"advsPlayBeginDatetime\":1527593061000,\"advsPlayBeginDatetimes\":\"2018-05-29\",\"advsPlayBeginTime\":\"08:07:17\",\"advsPlayConfigDtlId\":2,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527593061000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"10:07:17\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/LeeSmolin_2003.mp4\",\"advsVideoLengthOfTime\":1,\"advsVideoLocaltionPath\":\"\",\"local\":false}" +
                "{\"advsId\":3,\"advsPlayScene\":1,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/AditiShankardass_2009I_480.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
                "{\"advsId\":4,\"advsPlayScene\":0,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"12:07:17\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"23:07:17\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/KiranBirSethi_2009I.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
                "{\"advsId\":5,\"advsPlayScene\":0,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"10:07:17\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"23:07:17\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/StephenWilkes_2016.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
                "{\"advsId\":6,\"advsPlayScene\":1,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"\",\"advsVideoDownloadPath\":\"http://mirror.aarnet.edu.au/pub/TED-talks/AnnMarieThomas_2011-480p.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
                "{\"advsId\":7,\"advsPlayScene\":0,\"advsPlayBeginDatetime\":1527596302000,\"advsPlayBeginDatetimes\":\"2018-05-01\",\"advsPlayBeginTime\":\"\",\"advsPlayConfigDtlId\":1,\"advsPlayConfigId\":1,\"advsPlayEndDatetime\":1527596302000,\"advsPlayEndDatetimes\":\"2018-08-31\",\"advsPlayEndTime\":\"\",\"advsVideoDownloadPath\":\"http://112.253.22.165/22/i/i/h/h/iihhwacyhbbxmleszykibadklhesym/sh.yinyuetai.com/37B50162DC0FD54E945776F4162B4BB9.mp4\",\"advsVideoLengthOfTime\":60,\"advsVideoLocaltionPath\":\"\",\"local\":false}," +
                "]";
        adList = JSONArray.parseArray(s, AdvsVideo.class);
        try {
            dbManager.saveOrUpdate(adList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        if (isFastClick()) {
            return;
        }
        Log.d(TAG, "onClick: 用户点击操作");
        int id = v.getId();
        switch (id) {
            case R.id.btn_pause:
                player.pause();
                break;
            case R.id.btn_start:
                String proxyUrl = UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR + "AditiShankardass_2009I_480.mp4";
                player.play(proxyUrl);
                break;
            case R.id.btn_go_on:
                player.start();
                break;
            case R.id.btn_download:
                downloadVideo();
                break;
            case R.id.btn_stop_down:
                dlManager.stopDown();
                break;
            case R.id.btn_jump:
                Intent intent = new Intent(TestActivity.this, FreeAdActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_convert:
                break;
            case R.id.btn_get_db:
                try {
                    List<AdvsVideo> all = dbManager.findAll(AdvsVideo.class);
                    tvResult.setText("数据库中AdvsVideo的个数：" + all.size());
                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_upload:
//                UploadLocalData.getInstance(this, "adhakdh", adList, 10000).upload();
                break;
        }
    }

    private void downloadVideo() {
        /*if (isServiceRunning(DownloadIntentService.class.getName())) {
                Toast.makeText(TestActivity.this, "正在下载", Toast.LENGTH_SHORT).show();
                return;
            }*/
        Log.d(TAG, "onClick: dl_info: 用户点击下载");
        //String downloadUrl = http://sqdd.myapp.com/myapp/qqteam/tim/down/tim.apk;
        // 方式一
        dlManager.setDownloadCallback(new DownloadCallback() {
            @Override
            public void onComplete(String localPath) {
                Log.d(TAG, "onComplete: dl_info: 下载完成！localPath -- " + localPath);
            }

            @Override
            public void onError(String msg) {
                Log.d(TAG, "onError: dl_info: 下载错误！msg -- " + msg);
            }

            @Override
            public void onProgress(int progress) {
                Log.d(TAG, "onProgress: dl_info: 正在下载.. progress = " + progress);
            }
        });
        dlManager.startDown(DOWNLOADAPK_ID,
                "http://mirror.aarnet.edu.au/pub/TED-talks/",
                "http://mirror.aarnet.edu.au/pub/TED-talks/AnnMarieThomas_2011-480p.mp4",
                UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR);

        // 方式二
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTheEvent(MessageEvent event) {

    }

    /**
     * 用来判断服务是否运行.
     *
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

    @Override
    public void onComplete() {
        Log.d(TAG, "onComplete: ");
        String proxyUrl = UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR + "AditiShankardass_2009I_480.mp4";
        player.play(proxyUrl);
    }

    @Override
    public void onError(int what, int extra) {
        Log.d(TAG, "onError: " + what);
    }

    @Override
    public void onInfo(int what, int extra) {
        Log.d(TAG, "onInfo: " + what);
    }

    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= Constant.FAST_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }
}
