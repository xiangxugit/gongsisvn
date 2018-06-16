package newwater.com.newwater.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import newwater.com.newwater.R;
import newwater.com.newwater.beans.AdvsVideo;
import newwater.com.newwater.beans.DispenserCache;
import newwater.com.newwater.beans.SysDeviceMonitorConfig;
import newwater.com.newwater.beans.SysDeviceNoticeAO;
import newwater.com.newwater.broadcast.MessageReceiver;
import newwater.com.newwater.constants.Constant;
import newwater.com.newwater.constants.UriConstant;
import newwater.com.newwater.interfaces.DownloadCallback;
import newwater.com.newwater.manager.DownloadManager;
import newwater.com.newwater.utils.BaseSharedPreferences;
import newwater.com.newwater.utils.Create2QR2;
import newwater.com.newwater.utils.OkHttpUtils;
import newwater.com.newwater.utils.RestUtils;
import newwater.com.newwater.utils.TimeUtils;
import newwater.com.newwater.utils.XutilsInit;
import okhttp3.Request;

import static newwater.com.newwater.utils.PermissionUtils.permissions;

public class InitActivity extends AppCompatActivity {
    private static final String TAG = "InitActivity";

    private LinearLayout fiststep;
    private LinearLayout laststep;
    private ImageView qcode;//扫描二维码激活系统
    private Context mContext;
    private MessageReceiver messageReceiver;//信鸽Receiver
    private int dlIndex;
    private List<AdvsVideo> adList;
    // 权限集合
    List<String> permissionList = new ArrayList<>();
    private boolean isDownloading;
    DynamicReceiver myReceiver;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.MSG_WAITING_THEN_DOWNLOAD:
                    downloadInitVideo(adList);
                    break;
                case Constant.MSG_ALL_DOWN_COMPLETE:
                    refreshAllAdVideoData();
                    moveToMainActivity();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    public void initView() {
        setContentView(R.layout.activity_init);
        fiststep = (LinearLayout) findViewById(R.id.fiststep);
        laststep = (LinearLayout) findViewById(R.id.laststep);
        qcode = (ImageView) findViewById(R.id.qcode);
    }

    private void initData() {
        mContext = InitActivity.this;
        adList = new ArrayList<>();
        initPermission();
        initPush();
    }

    /**
     * 初始化权限
     */
    private void initPermission() {
        int i;
        for (i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permissions[i]);
            }
        }
        if (permissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            Toast.makeText(this, "已经授权", Toast.LENGTH_LONG).show();
        } else {//请求权限方法
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(this, permissions, i);
        }
    }

    /**
     * 初始化信鸽
     */
    private void initPush() {
        // 开启logcat输出，方便debug，发布时请关闭
        // XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        XGPushConfig.enableDebug(this, true);
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(final Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                Log.i("TPush", "注册成功，设备token为：" + data);

                //TODO 生成二维码
                //String data = XGPushConfig.getToken(MainActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bit = Create2QR2.createBitmap(data.toString());
                        qcode.setImageBitmap(bit);
                    }
                });
//                getDeviceInfo(1);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.i("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });

        myReceiver = new DynamicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MessageReceiver.PUSHACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(myReceiver, filter);
    }

    /**
     * 信鸽消息透传
     */
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
            Log.e("收到消息:", xgPushTextMessage.toString());
            String jsonData = xgPushTextMessage.getContent();
            JSONObject jsonObject = JSON.parseObject(jsonData);
            int deviceId = jsonObject.getInteger("deviceId");
            if (deviceId == 0) {
                moveToBreakDownActivity(getString(R.string.break_down_reason_no_device_id));
                return;
            }
            BaseSharedPreferences.setInt(mContext, Constant.DEVICE_ID_KEY, deviceId);
            getDeviceInfo(deviceId);
        }

        @Override
        public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

        }

        @Override
        public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

        }
    }

    private void getDeviceInfo(int deviceId) {
        OkHttpUtils.getAsyn(RestUtils.getUrl(UriConstant.GET_DEVICE_CONFIG + deviceId), new OkHttpUtils.StringCallback() {
            @Override
            public void onFailure(int errCode, Request request, IOException e) {
                Log.e(TAG, "onFailure: get device info by id -- failed!");
                dealResponseError();
            }

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = JSONObject.parseObject(response);
                if (null == jsonObject) {
                    Log.e(TAG, "onResponse: get device info by id -- response cannot parse to JsonObject!");
                    dealResponseError();
                    return;
                }
                if (0 == jsonObject.getInteger("code")) {
                    String data = jsonObject.getString("data");
                    if (TextUtils.isEmpty(data)) {
                        Log.e(TAG, "onResponse: get device info by id -- data in response is empty!");
                        dealResponseError();
                        return;
                    }
                    BaseSharedPreferences.setString(mContext, Constant.DEVICE_CONFIG_STRING_KEY, data);
                    SysDeviceMonitorConfig deviceConfig = JSONObject.parseObject(data, SysDeviceMonitorConfig.class);
                    if (checkConfigAndSave(deviceConfig)) {
                        dlIndex = 0;
                        downloadInitVideo(adList);
                    }
                } else {
                    Log.e(TAG, "onResponse: get device info by id -- code in response is not 0!");
                    dealResponseError();
                }

            }
        });
    }

    /**
     * config中是否包含所有所需信息
     * @param config
     * @return
     */
    private boolean checkConfigAndSave(SysDeviceMonitorConfig config) {
        if (null == config) {
            return false;
        }
        Integer drinkMode = config.getProductChargMode();
        BaseSharedPreferences.setInt(mContext, Constant.DRINK_MODE_KEY,
                null == drinkMode ? Constant.DRINK_MODE_DEFAULT : drinkMode);
        String rentDeadline = null;
        Date productRentTime = config.getProductRentTime();
        if (null != productRentTime) {
            rentDeadline = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(productRentTime);
        }
        BaseSharedPreferences.setString(mContext, Constant.RENT_DEADLINE_KEY,
                TextUtils.isEmpty(rentDeadline) ? Constant.RENT_DEADLINE_DEFAULT : rentDeadline);
        String contractInfo = config.getAdminUserTelephone();
        BaseSharedPreferences.setString(mContext, Constant.CONTRACT_INFO_KEY,
                TextUtils.isEmpty(contractInfo) ? Constant.CONTRACT_INFO_DEFAULT : contractInfo);
        Integer ppFlow = config.getMotCfgPpFlow();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_PP_FLOW_KEY,
                null == ppFlow ? Constant.DEVICE_PP_FLOW_DEFAULT : ppFlow);
        Integer grainCarbon = config.getMotCfgGrainCarbonFlow();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_GRAIN_CARBON_KEY,
                null == grainCarbon ? Constant.DEVICE_GRAIN_CARBON_DEFAULT : grainCarbon);
        Integer pressCarbon = config.getMotCfgPressCarbonFlow();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_PRESS_CARBON_KEY,
                null == pressCarbon ? Constant.DEVICE_PRESS_CARBON_DEFAULT : pressCarbon);
        Integer poseCarbon = config.getMotCfgPoseCarbonFlow();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_POSE_CARBON_KEY,
                null == poseCarbon ? Constant.DEVICE_POSE_CARBON_DEFAULT : poseCarbon);
        Integer roFlow = config.getMotCfgRoFlow();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_RO_FLOW_KEY,
                null == roFlow ? Constant.DEVICE_RO_FLOW_DEFAULT : roFlow);
        Integer upTime = config.getMotCfgUpTime();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_UP_TIME_KEY,
                null == upTime ? Constant.DEVICE_UP_TIME_DEFAULT : upTime);
        Integer flushInterval = config.getMotCfgFlushInterval();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_FLUSH_INTERVAL_KEY,
                null == flushInterval ? Constant.DEVICE_FLUSH_INTERVAL_DEFAULT : flushInterval);
        Integer flushDuration = config.getMotCfgFlushDuration();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_FLUSH_DURATION_KEY,
                null == flushDuration ? Constant.DEVICE_FLUSH_DURATION_DEFAULT : flushDuration);
        Integer heatingTemp = config.getMotCfgHeatingTemp();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_HEATING_TEMP_KEY,
                null == heatingTemp ? Constant.DEVICE_HEATING_TEMP_DEFAULT : heatingTemp);
        Integer coolingTemp = config.getMotCfgCoolingTemp();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_COOLING_TEMP_KEY,
                null == coolingTemp ? Constant.DEVICE_COOLING_TEMP_DEFAULT : coolingTemp);
        Integer heatingAllday = config.getMotCfgHeatingAllday();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_HEATING_ALL_DAY_KEY,
                null == heatingAllday ? Constant.DEVICE_HEATING_ALL_DAY_DEFAULT : heatingAllday);
        Integer coolingAllday = config.getMotCfgCoolingAllday();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_COOLING_ALL_DAY_KEY,
                null == coolingAllday ? Constant.DEVICE_COOLING_ALL_DAY_DEFAULT : coolingAllday);
        String heatingInterval = config.getMotCfgHeatingInterval();
        BaseSharedPreferences.setString(mContext, Constant.DEVICE_HEATING_INTERVAL_KEY,
                TextUtils.isEmpty(heatingInterval) ? Constant.DEVICE_HEATING_INTERVAL_DEFAULT : heatingInterval);
        String coolingInterval = config.getMotCfgCoolingInterval();
        BaseSharedPreferences.setString(mContext, Constant.DEVICE_COOLING_INTERVAL_KEY,
                TextUtils.isEmpty(coolingInterval) ? Constant.DEVICE_COOLING_INTERVAL_DEFAULT : coolingInterval);
        Integer maxFlow = config.getMotCfgMaxFlow();
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_MAX_FLOW_KEY,
                null == maxFlow ? Constant.DEVICE_MAX_FLOW_DEFAULT : maxFlow);
        BaseSharedPreferences.setInt(mContext, Constant.DEVICE_TDS_THRESHOLD_KEY,
                Constant.DEVICE_TDS_THRESHOLD_DEFAULT);
//        public = new AdvsVideo();
         AdvsVideo testVideo = new AdvsVideo();
         testVideo.setAdvsVideoDownloadPath("http://mirror.aarnet.edu.au/pub/TED-talks/MarkRonson_2014.mp4");
         adList.add(testVideo);
//        adList = ......
        return true;

    }

    /**
     * 回应的数据有问题时的处理
     */
    private void dealResponseError() {
        Log.d(TAG, "dealResponseError: 获取设备参数失败");
        moveToBreakDownActivity(getString(R.string.break_down_reason_no_device_config));
    }

    private void downloadInitVideo(List<AdvsVideo> adList) {
        // 下载完毕，则去同步各种数据
        if (dlIndex >= adList.size()) {
            Log.d(TAG, "getPushStrategy: 全部视频状态为已下载，return");
            handler.sendEmptyMessageDelayed(Constant.MSG_ALL_DOWN_COMPLETE, Constant.ALL_DOWN_WAIT_TIME);
            return;
        }
        // 没下完，则下载
        AdvsVideo ad = adList.get(dlIndex);
        // 如果为空，则下载下一个
        if (null == ad) {
            Log.d(TAG, "getPushStrategy: 本条广告为空，return");
            dlIndex++;
            downloadInitVideo(adList);
            return;
        }
        // 下载地址为空，上报地址错误
        if (TextUtils.isEmpty(ad.getAdvsVideoDownloadPath())) {
            Log.d(TAG, "onError: dl_info: URL为空！");
            saveWrongUrlNotice(ad);
            adList.remove(ad);
            return;
        }
        downloadVideo(ad);
    }

    private void downloadVideo(final AdvsVideo ad) {
        if (isDownloading) {
            Log.d(TAG, "downloadVideo: 正在下载，return..");
            if (handler.hasMessages(Constant.MSG_WAITING_THEN_DOWNLOAD)) {
                handler.removeMessages(Constant.MSG_WAITING_THEN_DOWNLOAD);
            }
            handler.sendEmptyMessageDelayed(Constant.MSG_WAITING_THEN_DOWNLOAD,
                    Constant.IS_DOWNING_WAIT_TIME * 1000);
            return;
        }
        String downloadPath = ad.getAdvsVideoDownloadPath();
        Log.d(TAG, "downloadVideo: 开始下载广告视频 dlIndex = " + dlIndex + ", url = " + downloadPath);
        isDownloading = true;
        DownloadManager dlManager = DownloadManager.getInstance();
        dlManager.setDownloadCallback(new DownloadCallback() {
            @Override
            public void onProgress(int progress) {
                Log.d(TAG, "onProgress: dl_info: 正在下载.. progress = " + progress);

            }

            @Override
            public void onComplete(String localPath) {
                Log.d(TAG, "downloadInitVideo: 第" + dlIndex + "个初始视频下载完成。localPath -- " + localPath);
                isDownloading = false;
                AdvsVideo ad = adList.get(dlIndex);
                ad.setLocal(true);
                ad.setAdvsVideoLocaltionPath(localPath);
                adList.set(dlIndex, ad);
                dlIndex++;
                downloadInitVideo(adList);
            }

            @Override
            public void onError(String msg) {
                Log.d(TAG, "onError: dl_info: 下载错误！msg -- " + msg);
                isDownloading = false;
                // 网址错误则上报错误信息；其他错误则放在最后再下
                if (msg.contains(Constant.DOWN_ERROR_MSG_WRONG_URL) || msg.contains(Constant.DOWN_ERROR_MSG_WRONG_BASE_URL)) {
                    Log.d(TAG, "onError: dl_info: URL有误！");
                    saveWrongUrlNotice(adList.get(dlIndex));
                    adList.remove(dlIndex);
                    downloadInitVideo(adList);
                    return;
                }
                Log.d(TAG, "onError: dl_info: 将本广告视频移动至list最后");
                AdvsVideo advsVideo = adList.get(dlIndex);
                adList.remove(dlIndex);
                adList.add(advsVideo);
                if (handler.hasMessages(Constant.MSG_WAITING_THEN_DOWNLOAD)) {
                    handler.removeMessages(Constant.MSG_WAITING_THEN_DOWNLOAD);
                }
                handler.sendEmptyMessageDelayed(Constant.MSG_WAITING_THEN_DOWNLOAD,
                        Constant.IS_DOWNING_WAIT_TIME * 1000);

            }
        });
        dlManager.startDown(Constant.DOWNLOADAPK_ID, downloadPath.substring(0, downloadPath.lastIndexOf('/') + 1),
                downloadPath, UriConstant.APP_ROOT_PATH + UriConstant.VIDEO_DIR);
    }

    /**
     * 存储广告视频下载地址错误的预警信息
     * @param ad
     */
    private void saveWrongUrlNotice(AdvsVideo ad) {
        SysDeviceNoticeAO notice = new SysDeviceNoticeAO(
                BaseSharedPreferences.getInt(mContext, Constant.DEVICE_ID_KEY),
                Constant.NOTICE_TYPE_AD_URL_WRONG, Constant.NOTICE_LEVEL_ABNORMAL,
                ad.getAdvsId()+"", ad.getAdvsVideoDownloadPath(),
                TimeUtils.getCurrentTime());
        try {
            new XutilsInit(mContext).getDb().save(notice);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void refreshAllAdVideoData() {
        DbManager dbManager = new XutilsInit(mContext).getDb();
        Log.d(TAG, "refreshAllAdVideoData: 开始更新数据库及缓存list");
        // 同步数据到数据库
        try {
            dbManager.delete(AdvsVideo.class);
            dbManager.saveOrUpdate(adList);
        } catch (DbException e) {
            e.printStackTrace();
        }
        // 同步数据到各个list


        DispenserCache.initAdVideoList.clear();
        DispenserCache.initAdVideoList.addAll(adList);
    }

    /**
     * 跳转到主页
     */
    private void moveToMainActivity() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转到机器故障Activity
     *
     * @param errReason
     */
    public void moveToBreakDownActivity(String errReason) {
        Intent intent = new Intent(mContext, BreakDownActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_BREAK_DOWN_ERR_REASON, errReason);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                //判断是否勾选禁止后不再询问
                boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                if (showRequestPermission) {
                    showToast("权限未申请");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    /**
     * 控制流程 控件的显示/隐藏
     *
     * @param step
     */
    public void showStep(String step) {
        if (step == "fist") {
            fiststep.setVisibility(View.GONE);
            laststep.setVisibility(View.VISIBLE);
        }
    }
}
