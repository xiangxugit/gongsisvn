package purewater.com.leadapp;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;
import purewater.com.leadapp.beans.SysApkVersionVO;
import purewater.com.leadapp.broadcast.MessageReceiver;
import purewater.com.leadapp.utils.ApkUtils;
import purewater.com.leadapp.utils.Create2QR2;
import purewater.com.leadapp.utils.OkHttpUtils;
import purewater.com.leadapp.utils.RestUtils;
import purewater.com.leadapp.utils.XutilsHttp;

import static purewater.com.leadapp.utils.PermissionUtils.permissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView intall;
    private ProgressDialog progressDialog;//进度条
    private ProgressBar jindu;
    private LinearLayout fiststep;
    private LinearLayout twostep;
    private LinearLayout three;
    private LinearLayout four;
    private LinearLayout fivestep;
    private ImageView qcode;//扫描二维码激活系统
    private MessageReceiver messageReceiver;//信鸽Receiver

    // 权限集合
    List<String> permissionList = new ArrayList<>();
    //sd卡默认下载路径
    String sdPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    //要调用另一个APP的activity所在的包名
    String packageName = "newwater.com.newwater";
    //要调用另一个APP的activity名字
    String activity = "newwater.com.newwater.MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    //Android 自学之进度条ProgressBar:https://www.cnblogs.com/Yang-jing/p/3757219.html
    public void initView() {
        intall = (TextView) findViewById(R.id.intall);
        intall.setOnClickListener(this);
        fiststep = (LinearLayout) findViewById(R.id.fiststep);
        twostep = (LinearLayout) findViewById(R.id.twostep);
        three = (LinearLayout) findViewById(R.id.three);
        four = (LinearLayout) findViewById(R.id.four);
        fivestep = (LinearLayout) findViewById(R.id.fivestep);
        qcode = (ImageView) findViewById(R.id.qcode);
        //获得界面布局里面的进度条组件
        jindu = (ProgressBar) findViewById(R.id.jindu);

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

                //TODO 生成二维码
                //String data = XGPushConfig.getToken(MainActivity.this);
                Bitmap bit = Create2QR2.createBitmap(data.toString());
                qcode.setImageBitmap(bit);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.i("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });

        /*IntentFilter intentFilter = new IntentFilter();
        messageReceiver = new MessageReceiver();
        intentFilter.addAction(MessageReceiver.PUSHACTION); //为BroadcastReceiver指定action，即要监听的消息名字
        registerReceiver(messageReceiver, intentFilter); //注册监听
        unregisterReceiver(messageReceiver); //取消监听*/
        DynamicReceiver myReceiver = new DynamicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MessageReceiver.PUSHACTION);
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(myReceiver, filter);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.download));
        //设置信息
        progressDialog.setMessage(getString(R.string.dowanloading));
        //设置信息格式
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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
            String jsonData = "收到消息:" + xgPushTextMessage.toString();
            JSONObject jsonObject = JSON.parseObject(jsonData);
            Log.e("message", jsonData);
            String deviceId = jsonObject.getString("deviceId");

            //String url = RestUtils.getUrl(RestUtils.GET + "?deviceId=" + deviceId);
            if (deviceId != null && deviceId.length() != 0) {
                //ApkUtils.startApp("newwater.com.newwater", "MainActivity");
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("load_send_device_id", deviceId);
                intent.putExtras(bundle);
                intent.setClassName(packageName, activity);
                startActivityForResult(intent, 1);
            }
        }

        @Override
        public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

        }

        @Override
        public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

        }
    }

    /**
     * 在启动前进行一次判断：app是否在后台运行
     *
     * @return
     */
    private boolean isBackgroundRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (activityManager == null)
            return false;
        // get running application processes
        List<ActivityManager.RunningAppProcessInfo> processList = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo process : processList) {
             /*
            BACKGROUND=400 EMPTY=500 FOREGROUND=100
            GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
             */
            if (process.processName.startsWith(packageName)) {
                boolean isBackground = process.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && process.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                boolean isLockedState = keyguardManager
                        .inKeyguardRestrictedInputMode();
                if (isBackground || isLockedState)
                    return true;
                else
                    return false;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.intall:
                intall.setVisibility(View.GONE);
                Thread thread = new Thread() {
                    public void run() {
                        try {
                            getDownload(RestUtils.getUrl(RestUtils.NEWAPK));
                        } catch (Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                thread.start();
                showStep("one");

                break;
        }
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

    /**
     * 获取下载路径
     *
     * @param url
     */
    public void getDownload(String url) {
        /*XutilsHttp.getInstance().get(true, MainActivity.this, url, null, new XCallBack() {
            @Override
            public void onResponse(String result) {
                Log.e("test", "test" + result);
                JSONObject apkinfoobj = JSON.parseObject(result);
                String apkinfostring = apkinfoobj.getString("data");
                SysApkVersionVO sysApkVersionVO = JSONObject.parseObject(apkinfostring, SysApkVersionVO.class);
                install(sysApkVersionVO.getApkUrl());
            }

            @Override
            public void onFail(String result) {
                Log.e("onFail", "onFail" + result);
            }
        });*/
        try {
            OkHttpUtils.getAsyn(url, new OkHttpUtils.StringCallback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.e("onFailure", request.toString());
                }

                @Override
                public void onResponse(String result) {
                    Log.e("onResponse", result);
                    JSONObject jsonObject = JSON.parseObject(result);
                    String data = jsonObject.getString("data");
                    SysApkVersionVO sysApkVersionVO = JSONObject.parseObject(data, SysApkVersionVO.class);
                    doDownload(sysApkVersionVO.getApkUrl());
                }
            });
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    /**
     * 下载
     *
     * @param url
     */
    public void doDownload(String url) {
        url = "http://121.43.198.84:8026/upload/anjian.apk";
        /*RequestParams requestParams = new RequestParams(url);
        requestParams.setAutoResume(true);//设置为断点续传
        requestParams.setAutoRename(false);*/
        String name = getFileExtensionFromUrl(url);
        String filePath = "";
        // 判断SD卡是否装入，已装入
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            final File file = new File(sdPath);
            // 判断目录是否存在，如果不存在就创建目录
            if (!file.exists()) {
                file.mkdir();
            }
            filePath = sdPath + "/" + name;
        } else {
            String path = getFilesDir() + "/myapp";
            final File file = new File(path);
            // 判断目录是否存在，如果不存在就创建目录
            if (!file.exists()) {
                file.mkdir();
            }
            filePath = path + "/" + name;
        }
        /*requestParams.setSaveFilePath(filePath);
        requestParams.setExecutor(new PriorityExecutor(2, true));
        requestParams.setCancelFast(true);*/

        XutilsHttp.getInstance().downFile(url, filePath, new XutilsHttp.XDownLoadCallBack() {
            @Override
            public void onstart() {
                Log.e("tag", "开始下载的时候执行");
            }

            @Override
            public void onFailure(String result) {
                Log.e("tag", "下载失败:" + result);
            }

            @Override
            public void onFinished() {
                Log.e("tag", "完成,每次取消下载也会执行该方法(" + Thread.currentThread().getName() + ")");
            }

            @Override
            public void onSuccess(final File file) {
                Log.e("tag", "下载成功:" + file.getPath());
                //TODO 安装
                install(file.getPath());
                //PackageUtils.install(MainActivity.this, file.getPath());
                showStep("two");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                startActivity(intent);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (isDownloading) {
                    jindu.setProgress((int) (current * 100 / total));
                }
            }
        });
    }

    /**
     * 描述: 安装
     *
     * @param filePath
     */
    public void install(final String filePath) {
        new Thread() {
            public void run() {
                if (ApkUtils.install(filePath, getApplicationContext())) {
                    apkToast("安裝成功");
                } else {
                    apkToast("安裝失败");
                }
            }
        }.start();
    }

    /**
     * 描述: 卸载
     *
     * @param filePath
     */
    public void uninstall(final String filePath) {
        new Thread() {
            public void run() {
                if (ApkUtils.uninstall("newwater.com.newwater", getApplicationContext())) {
                    apkToast("卸載成功");
                } else {
                    apkToast("卸載失败");
                }
            }
        }.start();
    }

    private void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    public void apkToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 控制流程 控件的显示/隐藏
     *
     * @param step
     */
    public void showStep(String step) {
        if (step == "one") {
            fiststep.setVisibility(View.GONE);
            twostep.setVisibility(View.VISIBLE);
        }
        if (step == "two") {
            twostep.setVisibility(View.GONE);
            three.setVisibility(View.VISIBLE);
        }
        if (step == "three") {
            three.setVisibility(View.GONE);
            four.setVisibility(View.VISIBLE);
        }
        if (step == "four") {
            four.setVisibility(View.GONE);
            fivestep.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取文件路径中的文件名
     *
     * @param url
     * @return
     */
    public static String getFileExtensionFromUrl(String url) {
        String filename = url.trim();

        filename = filename.substring(filename.lastIndexOf("/") + 1);
        return filename;
    }
}
