package purewater.com.leadapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.xutils.x;

import purewater.com.leadapp.beans.SysApkVersionVO;
import purewater.com.leadapp.utils.BaseSharedPreferences;
import purewater.com.leadapp.utils.Create2QR2;
import purewater.com.leadapp.utils.RestUtils;
import purewater.com.leadapp.utils.XCallBack;
import purewater.com.leadapp.utils.XutilsHttp;

import static android.webkit.MimeTypeMap.getFileExtensionFromUrl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView intall;
    private ProgressDialog progressDialog;//进度条
    private ProgressBar jindu;
    private LinearLayout fiststep;
    private LinearLayout twostep;
    private LinearLayout three;
    private LinearLayout four;
    private LinearLayout fivestep;
    private ImageView qcode;//扫描二维码激活系统




    private Callback.Cancelable cancelable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    public void initView(){
          intall = (TextView) findViewById(R.id.intall);
          intall.setOnClickListener(this);
          fiststep = (LinearLayout) findViewById(R.id.fiststep);
          twostep = (LinearLayout) findViewById(R.id.twostep);
          three = (LinearLayout) findViewById(R.id.three);
          four = (LinearLayout) findViewById(R.id.four);
          fivestep = (LinearLayout) findViewById(R.id.fivestep);
          qcode = (ImageView) findViewById(R.id.qcode);
          jindu = (ProgressBar)findViewById(R.id.jindu);
          jindu.setProgress(0);

          //请求token
//        http://www.jb51.net/article/101697.htm

//        https://www.cnblogs.com/Yang-jing/p/3757219.html
          String url = RestUtils.getUrl(RestUtils.GETTOKEN);
          Map params = new HashMap<String,String>();
          params.put("loginName","1");
          params.put("loginPassword","2");

          XutilsHttp.getInstance().get(false,MainActivity.this,url, params, new XCallBack() {
            @Override
            public void onResponse(String result) {
                // 成功获取数据

                JSONObject getdata = JSON.parseObject(result);
                String data = getdata.getString("data");
                JSONObject dataobj = JSON.parseObject(data);
                String accessToken = dataobj.getString("accessToken");
                //把token存入缓存
                BaseSharedPreferences.setString(MainActivity.this,BaseSharedPreferences.TOKEN,accessToken);
//                String downloadurl = RestUtils.getUrl(RestUtils.NEWAPK);
//                doDownload(downloadurl);



                Toast.makeText(MainActivity.this,""+result,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });


//        progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setTitle(getString(R.string.download));
//        //设置信息
//        progressDialog.setMessage(getString(R.string.dowanloading));
//        //设置信息格式
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.intall:
                intall.setVisibility(View.GONE);
                String url = RestUtils.getUrl(RestUtils.NEWAPK);
//                installSilent(Environment.getExternalStorageDirectory()+"/myapp/"+"test.apk");
//                doDownload(url);
                install("test");
                showStep("one");

                break;
        }
    }

    public void showStep(String step){
        if(step=="one"){
            fiststep.setVisibility(View.GONE);
            twostep.setVisibility(View.VISIBLE);
        }
        if(step=="two"){
            twostep.setVisibility(View.GONE);
            three.setVisibility(View.VISIBLE);
        }
        if(step=="three"){
            three.setVisibility(View.GONE);
            four.setVisibility(View.VISIBLE);
        }
        if(step == "four"){
            four.setVisibility(View.GONE);
            fivestep.setVisibility(View.VISIBLE);
        }
    }


    public void doDownload(String url){
         XutilsHttp.getInstance().get(true,MainActivity.this,url,null,new XCallBack(){
             @Override
             public void onResponse(String result) {
                 String test = result;
                 Log.e("test","test"+test);
                 JSONObject apkinfoobj = JSON.parseObject(result);
                 String apkinfostring = apkinfoobj.getString("data");
                 SysApkVersionVO sysApkVersionVO = JSONObject.parseObject(apkinfostring,SysApkVersionVO.class);
                 install(sysApkVersionVO.getApkUrl());


             }

             @Override
             public void onFail(String result) {
                 Log.e("faile","faile"+result);
             }
         });
//        https://blog.csdn.net/sk719887916/article/details/46746991

    }

    public void install(String url){
        url = "http://121.43.198.84:8026/upload/anjian.apk";
        RequestParams requestParams = new RequestParams(url);
        requestParams.setAutoResume(true);//设置为断点续传
        requestParams.setAutoRename(false);
        final String name = getFileExtensionFromUrl(url);
        requestParams.setSaveFilePath("/sdcard/myapp/"+name);
        requestParams.setExecutor(new PriorityExecutor(2, true));
        requestParams.setCancelFast(true);
        cancelable = x.http().get(requestParams, new Callback.ProgressCallback<File>()  {
            @Override
            public void onCancelled(CancelledException arg0) {
                Log.e("tag", "取消"+Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                Log.e("tag", "onError: 失败"+Thread.currentThread().getName());
            }

            @Override
            public void onFinished() {
                Log.e("tag", "完成,每次取消下载也会执行该方法"+Thread.currentThread().getName());

//                https://dev.mi.com/console/doc/detail?pId=41
            }

            @Override
            public void onSuccess(File arg0) {
                Log.e("tag", "下载成功的时候执行"+Thread.currentThread().getName());
                //TODO 安装

                showStep("two");
//
                //TODO获取二维码
                String stringerweima = "erweimatst";
                Bitmap bit = Create2QR2.createBitmap(stringerweima);
                qcode.setImageBitmap(bit);
//                https://www.cnblogs.com/lr393993507/p/5543145.html
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(arg0), "application/vnd.android.package-archive");
                startActivity(intent);

//                installSilent(Environment.getExternalStorageDirectory()+"/myapp/"+name);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (isDownloading) {
                    jindu.setProgress((int) (current*100/total));
                    Log.e("tag", "下载中,会不断的进行回调:"+Thread.currentThread().getName());
                }
            }

            @Override
            public void onStarted() {
                Log.e("tag", "开始下载的时候执行"+Thread.currentThread().getName());
            }

            @Override
            public void onWaiting() {
                Log.e("tag", "等待,在onStarted方法之前执行"+Thread.currentThread().getName());
            }
        });
    }






    public static int installSilent(String filePath) {
        File file = new File(filePath);
        if (filePath == null || filePath.length() == 0 || file == null || file.length() <= 0 || !file.exists() || !file.isFile()) {
            return 1;
        }
        String[] args = { "pm", "install", "-r", filePath };
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int result;
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }

        // TODO should add memory is not enough here
        if (successMsg.toString().contains("Success") || successMsg.toString().contains("success")) {
            result = 0;
        } else {
            result = 2;
        }
        Log.d("test-test", "successMsg:" + successMsg + ", ErrorMsg:" + errorMsg);
        return result;
    }


    public static String getFileExtensionFromUrl(String url) {
        String filename = url.trim();

        filename = filename.substring(filename.lastIndexOf("/") + 1);
        return filename;

    }


}
